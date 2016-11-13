/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.server.devtool.utils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.eclipse.vorto.http.model.ServerResponse;
import org.eclipse.vorto.repository.model.ModelHandle;
import org.eclipse.vorto.server.devtool.web.controller.publisher.FileMessageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

@Component
public class DevtoolRestClient {

	@Value("${vorto.repository.base.path:http://vorto.eclipse.org}")
	private String basePath;
	
	@Autowired
	private RestTemplate restTemplate;

	public ModelResource getModel(ModelId modelId) {
		String json = getModelFile(modelId);
		Gson gson = createGson();
		JsonElement jsonElement = new JsonParser().parse(json);
		ModelResource modelResource = gson.fromJson(jsonElement, ModelResource.class);
		return modelResource;
	}

	public String getModelFile(ModelId modelId) {
		try {
			return restTemplate.getForObject(basePath + "/rest/model/file/{namespace}/{name}/{version}?output=DSL",
					String.class, modelId.getNamespace(), modelId.getName(), modelId.getVersion());
		} catch (RestClientException e) {
			throw new RuntimeException(e);
		}
	}

	public String getModelAsString(ModelId modelId) {
		try {
			return restTemplate.getForObject(basePath + "/rest/model/{namespace}/{name}/{version}", String.class,
					modelId.getNamespace(), modelId.getName(), modelId.getVersion());
		} catch (RestClientException e) {
			throw new RuntimeException(e);
		}
	}

	public ModelType getModelType(ModelId modelId) {
		String modelString = getModelAsString(modelId);
		String modelType = new JsonParser().parse(modelString).getAsJsonObject().get("modelType").getAsString();
		if (modelType.equalsIgnoreCase(ModelType.Datatype.toString())) {
			return ModelType.Datatype;
		} else if (modelType.equalsIgnoreCase(ModelType.Functionblock.toString())) {
			return ModelType.Functionblock;
		} else if (modelType.equalsIgnoreCase(ModelType.InformationModel.toString())) {
			return ModelType.InformationModel;
		} else if (modelType.equalsIgnoreCase(ModelType.Mapping.toString())) {
			return ModelType.Mapping;
		} else {
			throw new UnsupportedOperationException("Given ModelType is unknown");
		}
	}

	public List<ModelResource> searchByExpression(String expression) {
		String results = restTemplate.getForObject(basePath + "/rest/model/query=" + expression, String.class);
		JsonElement jsonElement = new JsonParser().parse(results);
		Gson gson = createGson();
		List<ModelResource> modelResourceList = gson.fromJson(jsonElement, new TypeToken<List<ModelResource>>() {
		}.getType());
		return modelResourceList;
	}

	public ResponseEntity<ServerResponse> uploadMultipleFiles(final String fileName, byte[] multipleFileContent){
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new FileMessageResource(multipleFileContent, fileName));
		HttpHeaders httpHeaders = getAuthorisedHeaders();
		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, httpHeaders);
		return restTemplate.postForEntity(basePath + "/rest/secure/multiple", requestEntity, ServerResponse.class);
	}
	
	public ResponseEntity<ServerResponse> checkInSingleFile(String handleId){
		HttpHeaders httpHeaders = getAuthorisedHeaders();
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(httpHeaders);
		return restTemplate.exchange(basePath + "/rest/secure/{handleId:.+}", HttpMethod.PUT, requestEntity, ServerResponse.class, handleId);
	}
		
	public ResponseEntity<ServerResponse> checkInMultipleFiles(ModelHandle[] modelHandles){
		Gson gson = new Gson();
		String json = gson.toJson(modelHandles);
		HttpHeaders httpHeaders = getAuthorisedHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		System.out.println(json);
		HttpEntity<String> requestEntity = new HttpEntity<>(json, httpHeaders);
		return restTemplate.exchange(basePath + "/rest/secure/checkInMultiple", HttpMethod.PUT, requestEntity, ServerResponse.class);
//		return null;
	}

	private HttpHeaders getAuthorisedHeaders(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String password = (String)SecurityContextHolder.getContext().getAuthentication().getCredentials();		
		String plainCreds = username+":"+password;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		return headers;
	}
	
	private Gson createGson() {
		return new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context)
					throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong());
			}
		}).create();
	}
}
