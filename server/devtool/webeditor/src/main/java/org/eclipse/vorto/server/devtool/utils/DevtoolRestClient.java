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

import java.util.Date;
import java.util.List;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.upload.ModelHandle;
import org.eclipse.vorto.repository.api.upload.ServerResponse;
import org.eclipse.vorto.server.devtool.web.controller.publisher.FileMessageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

@Component
public class DevtoolRestClient {

	@Value("${vorto.repository.base.path}")
	private String basePath;
	
	@Autowired
	private RestTemplate restTemplate;

	private Gson gson = createGson();

	public ModelInfo getModel(ModelId modelId) {
		String json = getModelAsString(modelId);
		JsonElement jsonElement = new JsonParser().parse(json);
		ModelInfo modelInfo = gson.fromJson(jsonElement, ModelInfo.class);
		return modelInfo;
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

	public List<ModelInfo> searchByExpression(String expression) {
		String results = restTemplate.getForObject(basePath + "/rest/model/query=" + expression, String.class);
		JsonElement jsonElement = new JsonParser().parse(results);
		List<ModelInfo> modelResourceList = gson.fromJson(jsonElement, new TypeToken<List<ModelInfo>>() {
		}.getType());
		return modelResourceList;
	}

	public ResponseEntity<ServerResponse> uploadMultipleFiles(final String fileName, byte[] multipleFileContent) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new FileMessageResource(multipleFileContent, fileName));
		HttpHeaders httpHeaders = getAuthorisedHeaders();
		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, httpHeaders);
		return restTemplate.postForEntity(basePath + "/rest/secure/multiple", requestEntity, ServerResponse.class);
	}

	public ResponseEntity<ServerResponse> checkInSingleFile(String handleId) {
		HttpHeaders httpHeaders = getAuthorisedHeaders();
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				httpHeaders);
		return restTemplate.exchange(basePath + "/rest/secure/{handleId:.+}", HttpMethod.PUT, requestEntity,
				ServerResponse.class, handleId);
	}

	public ResponseEntity<ServerResponse> checkInMultipleFiles(ModelHandle[] modelHandles) {
		String json = gson.toJson(modelHandles);
		HttpHeaders httpHeaders = getAuthorisedHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> requestEntity = new HttpEntity<>(json, httpHeaders);
		return restTemplate.exchange(basePath + "/rest/secure/checkInMultiple", HttpMethod.PUT, requestEntity,
				ServerResponse.class);
	}

	private HttpHeaders getAuthorisedHeaders() {
		HttpHeaders headers = new HttpHeaders();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof OAuth2Authentication) {
            Object details = auth.getDetails();
            if (details instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails oauth = (OAuth2AuthenticationDetails) details;
                headers.add("Authorization", "Bearer " + oauth.getTokenValue());
            }
        }

		return headers;
	}

	private Gson createGson() {
		return new GsonBuilder().registerTypeAdapter(Date.class, new VortoRepositoryGsonDateAdapter()).create();
	}
}
