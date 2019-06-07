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
package org.eclipse.vorto.repository.server.config.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.vorto.model.BooleanAttributeProperty;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumAttributeProperty;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IModel;
import org.eclipse.vorto.model.IPropertyAttribute;
import org.eclipse.vorto.model.IReferenceType;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class BaseConfiguration {

  @Bean
  public Jackson2ObjectMapperBuilder objectMapperBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

    builder.deserializerByType(IReferenceType.class, new JsonDeserializer<IReferenceType>() {

      @Override
      public IReferenceType deserialize(JsonParser parser, DeserializationContext context)
          throws IOException, JsonProcessingException {
        try {
          return parser.readValueAs(ModelId.class);
        } catch (IOException ioEx) {
          try {
            return parser.readValueAs(PrimitiveType.class);
          } catch (IOException ex) {
            ex.printStackTrace();
            return null;
          }
        }
      }

    });


    builder.deserializerByType(IPropertyAttribute.class,
        new JsonDeserializer<IPropertyAttribute>() {

          @Override
          public IPropertyAttribute deserialize(JsonParser parser, DeserializationContext context)
              throws IOException, JsonProcessingException {
            try {
              return parser.readValueAs(BooleanAttributeProperty.class);
            } catch (IOException ioEx) {
              try {
                return parser.readValueAs(EnumAttributeProperty.class);
              } catch (IOException ex) {
                ex.printStackTrace();
                return null;
              }
            }
          }

        });

    builder.deserializerByType(Map.class, new JsonDeserializer<HashMap<ModelId, IModel>>() {

      @Override
      public HashMap<ModelId, IModel> deserialize(JsonParser parser, DeserializationContext context)
          throws IOException, JsonProcessingException {
        try {
          HashMap<ModelId, IModel> deserialized = new HashMap<>();
          ObjectCodec oc = parser.getCodec();
          JsonNode node = oc.readTree(parser);

          Iterator<JsonNode> iterator = node.elements();
          while (iterator.hasNext()) {
            JsonNode childNode = iterator.next();
            JsonNode type = childNode.get("type");
            IModel value = null;

            if (ModelType.valueOf(type.asText()).equals(ModelType.InformationModel)) {
              value = oc.treeToValue(childNode, Infomodel.class);
            } else if (ModelType.valueOf(type.asText()).equals(ModelType.Functionblock)) {
              value = oc.treeToValue(childNode, FunctionblockModel.class);
            } else if (ModelType.valueOf(type.asText()).equals(ModelType.Datatype)
                && childNode.has("literals")) {
              value = oc.treeToValue(childNode, EnumModel.class);
            } else {
              value = oc.treeToValue(childNode, EntityModel.class);
            }

            if (value != null) {
              deserialized.put(getModelId(childNode.get("id").get("prettyFormat").asText()), value);
            }
          }

          return deserialized;
        } catch (IOException ioEx) {
          throw new RuntimeException(ioEx);
        }
      }

      private ModelId getModelId(String modelId) {
        try {
          return ModelId.fromPrettyFormat(modelId);
        } catch (IllegalArgumentException ex) {
          final int versionIndex = modelId.indexOf(":");
          return ModelId.fromReference(modelId.substring(0, versionIndex),
              modelId.substring(versionIndex + 1));
        }
      }

    });

    return builder;
  }
}
