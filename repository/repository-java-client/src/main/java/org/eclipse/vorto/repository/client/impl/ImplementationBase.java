/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.client.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.BooleanAttributeProperty;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumAttributeProperty;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IPropertyAttribute;
import org.eclipse.vorto.model.IReferenceType;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.repository.client.RepositoryClientException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class ImplementationBase {
  protected HttpClient httpClient;
  protected RequestContext requestContext;

  protected Gson gson = new GsonBuilder()
      .registerTypeAdapter(IReferenceType.class, new JsonDeserializer<IReferenceType>() {
        public IReferenceType deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext context) throws JsonParseException {
          if (jsonElement.isJsonPrimitive()) {
            return PrimitiveType.valueOf(jsonElement.getAsString());
          }
          return context.deserialize(jsonElement, ModelId.class);
        }
      }).registerTypeAdapter(IPropertyAttribute.class, new JsonDeserializer<IPropertyAttribute>() {
        public IPropertyAttribute deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext context) throws JsonParseException {
          if (jsonElement.getAsJsonObject().get("value").isJsonPrimitive()) {
            return context.deserialize(jsonElement, BooleanAttributeProperty.class);
          } else {
            return context.deserialize(jsonElement, EnumAttributeProperty.class);
          }
        }
      }).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
          return new Date(json.getAsJsonPrimitive().getAsLong());
        }
      })
      .registerTypeAdapter(Map.class, new JsonDeserializer<HashMap<ModelId, AbstractModel>>() {
        public HashMap<ModelId, AbstractModel> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            
            if (!jsonElement.isJsonObject()) {
                return null;
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Set<Entry<String, JsonElement>> jsonEntrySet = jsonObject.entrySet();
            HashMap<ModelId, AbstractModel> deserializedMap = new HashMap<ModelId, AbstractModel>();

            for (Entry<java.lang.String, JsonElement> entry : jsonEntrySet) {
                AbstractModel value = null;
                if (entry.getValue().getAsJsonObject().has("type") && entry.getValue().getAsJsonObject().get("type").getAsJsonPrimitive().getAsString().equals("InformationModel")) {
                    value = context.deserialize(entry.getValue(), Infomodel.class);
                } else if (entry.getValue().getAsJsonObject().has("type") && entry.getValue().getAsJsonObject().get("type").getAsJsonPrimitive().getAsString().equals("Functionblock")) {
                    value = context.deserialize(entry.getValue(), FunctionblockModel.class);
                } else if (entry.getValue().getAsJsonObject().has("type") && entry.getValue().getAsJsonObject().get("type").getAsJsonPrimitive().getAsString().equals("Datatype") && ((JsonElement) entry.getValue()).getAsJsonObject().has("literals")) {
                    value = context.deserialize(entry.getValue(), EnumModel.class);
                } else if (entry.getValue().getAsJsonObject().has("type") && entry.getValue().getAsJsonObject().get("type").getAsJsonPrimitive().getAsString().equals("Datatype")) {
                    value = context.deserialize(entry.getValue(), EntityModel.class);
                } 
                if (value != null) {
                    deserializedMap.put(getModelId(entry.getKey()), value);
                }
               
            }
            return deserializedMap;
        }

        private ModelId getModelId(String modelId) {
            try {
                return ModelId.fromPrettyFormat(modelId);
            } catch(IllegalArgumentException ex) {
                final int versionIndex = modelId.indexOf(":");
                return ModelId.fromReference(modelId.substring(0,versionIndex),modelId.substring(versionIndex+1));
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
        throw new RepositoryClientException("Error in converting result to " + type.getTypeName(),
            e);
      }
    };
  }

  protected <K> Function<HttpResponse, K> transformToClass(Class<K> modelClass) {
    return response -> {
      try {
        return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"),
            modelClass);
      } catch (IOException | JsonSyntaxException e) {
        throw new RepositoryClientException("Error in converting result to " + modelClass.getName(),
            e);
      }
    };
  }

  protected <K> K requestAndTransform(String url, Function<HttpResponse, K> successFn) {
    return requestAndTransform(url, successFn, () -> null);
  }

  protected <K> K requestAndTransform(String url, Function<HttpResponse, K> successFn,
      Supplier<K> errorSupplier) {
    HttpGet query = new HttpGet(url);

    if (requestContext.getRequestConfig() != null) {
      query.setConfig(requestContext.getRequestConfig());
    }

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
  }
}
