/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */

package org.eclipse.vorto.repository.api.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.content.BooleanAttributeProperty;
import org.eclipse.vorto.repository.api.content.EnumAttributeProperty;
import org.eclipse.vorto.repository.api.content.IPropertyAttribute;
import org.eclipse.vorto.repository.api.content.IReferenceType;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.client.RepositoryClientException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class ImplementationBase {
	protected HttpClient httpClient;
	protected RequestContext requestContext;
	
	protected Gson gson = new GsonBuilder()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
			.registerTypeAdapter(IReferenceType.class, new JsonDeserializer<IReferenceType>() {
				public IReferenceType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
						throws JsonParseException {
					if (jsonElement.isJsonPrimitive()) {
						return PrimitiveType.valueOf(jsonElement.getAsString());
					}
					return context.deserialize(jsonElement, ModelId.class);
				}
			})
			.registerTypeAdapter(IPropertyAttribute.class, new JsonDeserializer<IPropertyAttribute>() {
				public IPropertyAttribute deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
						throws JsonParseException {
					if (jsonElement.getAsJsonObject().get("value").isJsonPrimitive()) {
						return context.deserialize(jsonElement, BooleanAttributeProperty.class);
					} else {
						return context.deserialize(jsonElement, EnumAttributeProperty.class);
					}
				}
			})
			.create();
	
	public ImplementationBase(HttpClient httpClient, RequestContext requestContext) {
		this.httpClient = httpClient;
		this.requestContext = requestContext;
	}
	
	protected RequestContext getRequestContext() {
		return requestContext;
	}
	
	protected <K> Function<HttpResponse, K> transformToType(Type type) {
		return response -> {
			try {
				return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), type);
			} catch (IOException | JsonSyntaxException e) {
				throw new RepositoryClientException("Error in converting result to " + type.getTypeName(), e);
			}
		};
	}
	
	protected <K> Function<HttpResponse, K> transformToClass(Class<K> modelClass) {
		return response -> {
			try {
				return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), modelClass);
			} catch (IOException | JsonSyntaxException e) {
				throw new RepositoryClientException("Error in converting result to " + modelClass.getName(), e);
			}
		};
	}

	protected <K> CompletableFuture<K> requestAndTransform(String url, Function<HttpResponse, K> successFn) {
		return requestAndTransform(url, successFn, () -> null);
	}
	
	protected <K> CompletableFuture<K> requestAndTransform(String url, Function<HttpResponse, K> successFn, Supplier<K> errorSupplier) {
		HttpGet query = new HttpGet(url);
		
		if (requestContext.getRequestConfig() != null) {
			query.setConfig(requestContext.getRequestConfig());
		}
		
		return CompletableFuture.supplyAsync(() -> {
			try {
				return httpClient.execute(query, response -> {
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode >= 200 && statusCode < 300) {
						return successFn.apply(response);
					}
					return errorSupplier.get();
				});
			} catch (IOException e) {
				throw new RepositoryClientException("Error in executing URL : " + url, e);
			}
		});
	}
}
