/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.mapping.engine.twin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.model.runtime.PropertyValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Convenient helper that serializes the given Vorto model data to twin payloads
 * Please use bundle 'org.eclipse.vorto:mapping-targetplatform-ditto' instead
 *
 */
@Deprecated
public class TwinPayloadFactory {

  private static Gson gson = new GsonBuilder().create();

  /**
   * Creates Ditto protocol payload for modifying all features from the given Information Model Data
   * 
   * @param infomodel to set as value
   * @param dittoNamespace namespace of Ditto thing, e.g. org.mycompany
   * @param dittoIdSuffix only the suffix of the ID
   * @return ditto protocol containing vorto data, that can be sent to Ditto to update all features
   *         in one request
   */
  public static JsonObject toDittoProtocol(InfomodelValue infomodelData, String dittoNamespace,
      String dittoIdSuffix) {
    Map<String, Object> dittoPayload = new HashMap<String, Object>();
    dittoPayload.put("topic",
        dittoNamespace + "/" + dittoIdSuffix + "/things/twin/commands/modify");
    dittoPayload.put("headers", createHeader());
    dittoPayload.put("path", "/features");

    Map<String, Object> newFeatures = new HashMap<String, Object>();
    for (String infomodelProperty : infomodelData.getProperties().keySet()) {
      newFeatures.put(infomodelProperty, createFeature(infomodelData.get(infomodelProperty)));

    }
    dittoPayload.put("value", newFeatures);
    return gson.toJsonTree(dittoPayload).getAsJsonObject();
  }

  private static Object createFeature(FunctionblockValue fbData) {
    Map<String, Object> feature = new HashMap<String, Object>();
    feature.put("definition", Arrays.asList(fbData.getMeta().getId().getPrettyFormat()));
    feature.put("properties", createFunctionBlockProperties(fbData));
    return feature;
  }

  private static Object createHeader() {
    Map<String, Object> header = new HashMap<String, Object>();
    header.put("response-required", false);
    return header;
  }

  /**
   * Creates Ditto protocol payload for modifying the feature properties with the Vorto function
   * block data
   * 
   * @param functionblockData to set as value
   * @param featureId id of the feature to update
   * @param dittoNamespace namespace of Ditto thing, e.g. org.mycompany
   * @param dittoIdSuffix only the suffix of the ID
   * @return ditto protocol containing vorto data, that can be sent to Ditto to update the feature
   */
  public static JsonObject toDittoProtocol(FunctionblockValue functionblockData, String featureId,
      String dittoNamespace, String dittoIdSuffix) {
    Map<String, Object> dittoPayload = new HashMap<String, Object>();
    dittoPayload.put("topic",
        dittoNamespace + "/" + dittoIdSuffix + "/things/twin/commands/modify");
    dittoPayload.put("headers", createHeader());
    dittoPayload.put("path", "/features/" + featureId + "/properties");

    dittoPayload.put("value", createFunctionBlockProperties(functionblockData));
    return gson.toJsonTree(dittoPayload).getAsJsonObject();
  }
  
  private static Map<String,Object> createFunctionBlockProperties(FunctionblockValue fbData) {
    Map<String, Object> result = new HashMap<String, Object>();

    Map<String, Object> status = new HashMap<String, Object>();

    for (PropertyValue statusProperty : fbData.getStatus()) {
      status.put(statusProperty.getMeta().getName(), statusProperty.serialize());
    }
    
    if (!status.isEmpty()) {
      result.put("status", status);
    }

    Map<String, Object> config = new HashMap<String, Object>();

    for (PropertyValue configProperty : fbData.getConfiguration()) {
      config.put(configProperty.getMeta().getName(), configProperty.serialize());
    }
    
    if (!config.isEmpty()) {
      result.put("configuration", config);
    }
    
    return result;
  }
}
