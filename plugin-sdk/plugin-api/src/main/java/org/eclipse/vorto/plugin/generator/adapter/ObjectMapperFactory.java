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
package org.eclipse.vorto.plugin.generator.adapter;

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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperFactory {

  private static ObjectMapper mapper = null;


  public static ObjectMapper getInstance(ObjectMapper existingMapper) {
    if (mapper == null) {
      mapper = existingMapper != null ? existingMapper : new ObjectMapper();
      init(mapper);
    }

    return mapper;
  }

  public static ObjectMapper getInstance() {
    return getInstance(null);
  }

  private static void init(ObjectMapper mapper) {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(IPropertyAttribute.class, new PropertyAttributeDeserializer());
    module.addDeserializer(IReferenceType.class, new ModelReferenceDeserializer());
    module.addDeserializer(Map.class, new ModelMapDeserializer());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(module);
  }

  @SuppressWarnings("serial")
  public static class PropertyAttributeDeserializer extends StdDeserializer<IPropertyAttribute> {

    public PropertyAttributeDeserializer() {
      this(null);
    }

    public PropertyAttributeDeserializer(Class<?> vc) {
      super(vc);
    }


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

  }

  @SuppressWarnings("serial")
  public static class ModelReferenceDeserializer extends StdDeserializer<IReferenceType> {

    public ModelReferenceDeserializer() {
      this(null);
    }

    public ModelReferenceDeserializer(Class<?> vc) {
      super(vc);
    }


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
  }
  
  @SuppressWarnings("serial")
  public static class ModelMapDeserializer extends StdDeserializer<HashMap<ModelId, IModel>> {

    public ModelMapDeserializer() {
      this(null);
    }

    public ModelMapDeserializer(Class<?> vc) {
      super(vc);
    }


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
  }
}
