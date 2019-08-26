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
package org.eclipse.vorto.mapping.engine.model.spec;

import java.io.InputStream;
import java.lang.reflect.Type;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.BooleanAttributeProperty;
import org.eclipse.vorto.model.DictionaryType;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumAttributeProperty;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IPropertyAttribute;
import org.eclipse.vorto.model.IReferenceType;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class MappingSpecBuilder {

  private InputStream input;

  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
      .registerTypeAdapter(IReferenceType.class, new JsonDeserializer<IReferenceType>() {
        public IReferenceType deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext context) throws JsonParseException {
          if (jsonElement.isJsonPrimitive()) {
            return PrimitiveType.valueOf(jsonElement.getAsString());
          } else if (jsonElement.getAsJsonObject().has("type") && jsonElement.getAsJsonObject().get("type").getAsString().equals(ModelType.Functionblock.name())) {
            return context.deserialize(jsonElement, FunctionblockModel.class);
          } else if (jsonElement.getAsJsonObject().has("type") && jsonElement.getAsJsonObject().get("type").getAsString().equals(ModelType.Datatype.name())) {
            return jsonElement.getAsJsonObject().has("literals")? context.deserialize(jsonElement, EnumModel.class): context.deserialize(jsonElement, EntityModel.class);
          } else if (jsonElement.getAsJsonObject().has("type") && jsonElement.getAsJsonObject().get("type").getAsString().equals("dictionary")) {
            return context.deserialize(jsonElement, DictionaryType.class);
          } else {
            return context.deserialize(jsonElement, ModelId.class);
          }
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
      }).create();


  public MappingSpecBuilder fromInputStream(InputStream input) {
    this.input = input;
    return this;
  }

  public IMappingSpecification build() {
    try {
      return gson.fromJson(IOUtils.toString(this.input), MappingSpecification.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
