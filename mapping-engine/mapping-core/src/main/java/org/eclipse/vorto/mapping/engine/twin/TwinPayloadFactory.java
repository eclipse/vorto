/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.mapping.engine.twin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Convenient helper that serializes the given Vorto model data to twin payloads
 *
 */
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
    feature.put("properties", fbData.serialize());
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

    Map<String, Object> feature = functionblockData.serialize();
    dittoPayload.put("value", feature);
    return gson.toJsonTree(dittoPayload).getAsJsonObject();
  }
}
