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
package org.eclipse.vorto.service.mapping;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.mapping.engine.MappingEngine;
import org.eclipse.vorto.mapping.engine.twin.TwinPayloadFactory;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Example {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public static void main(String[] args) throws Exception {
    MappingEngine engine = MappingEngine.createFromInputStream(
        FileUtils.openInputStream(new File("src/test/resources/mappingspec.json")));
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("distance", "100m");
    InfomodelValue result = engine.mapSource(data);
    if (result.validate().isValid()) {
      JsonObject dittoPayloadToUpdateAllFeatures =
          TwinPayloadFactory.toDittoProtocol(result, "org.eclipse.vorto", "deviceid-123");
      System.out.println(gson.toJson(dittoPayloadToUpdateAllFeatures));

      JsonObject dittoPayloadToUpdateSingleFeature = TwinPayloadFactory
          .toDittoProtocol(result.get("distance"), "distance", "org.eclipse.vorto", "deviceid-123");
      System.out.println(gson.toJson(dittoPayloadToUpdateSingleFeature));
    } else {
      System.err.println("Mapped data is not valid to Vorto Model");
    }
  }
}
