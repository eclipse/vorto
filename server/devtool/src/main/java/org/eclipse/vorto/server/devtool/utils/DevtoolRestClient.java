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

import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

	@Value("${vorto.repository.rest.path}")
	private String basePath;

	public ModelResource getModel(ModelId modelId){
		String json = getModelFile(modelId);
		Gson gson = createGson();
		JsonElement jsonElement = new JsonParser().parse(json);
		ModelResource modelResource = gson.fromJson(jsonElement, ModelResource.class);
		return modelResource;
	}
	
	public String getModelFile(ModelId modelId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(basePath + "model/file/{namespace}/{name}/{version}?output=DSL",
					String.class, modelId.getNamespace(), modelId.getName(), modelId.getVersion());
		} catch (RestClientException e) {
			throw new RuntimeException(e);
		}
	}

	public String getModelAsString(ModelId modelId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(basePath + "model/{namespace}/{name}/{version}", String.class,
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
		RestTemplate restTemplate = new RestTemplate();
		String results = restTemplate.getForObject(basePath + "model/query=" + expression, String.class);
		JsonElement jsonElement = new JsonParser().parse(results);
		Gson gson = createGson();
		List<ModelResource> modelResourceList = gson.fromJson(jsonElement, new TypeToken<List<ModelResource>>() {
		}.getType());
		return modelResourceList;
	}
	
	private Gson createGson(){
		return new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context)
					throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong());
			}
		}).create();
	}
}
