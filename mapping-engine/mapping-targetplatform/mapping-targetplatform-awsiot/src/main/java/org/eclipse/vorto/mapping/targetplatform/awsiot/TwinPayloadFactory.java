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
package org.eclipse.vorto.mapping.targetplatform.awsiot;

import org.eclipse.vorto.model.runtime.InfomodelValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Convenient helper that serializes the given Vorto model, required to update an AWS IoT Shadow thing
 *
 */ 
public class TwinPayloadFactory {
  
  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

  
  /**
   * Creates a JSON payload that updates the entire shadow for the given Information Model data
   * 
   * @param infomodel to set as value
   * @return payload containing all data defined in the Vorto model, that can be sent to AWS IoT to update reported state
   *         in one request
   */
  public static JsonObject toShadowUpdateRequest(InfomodelValue infomodelData) {
    return gson.toJsonTree(UpdateRequest.create().withReported(infomodelData)).getAsJsonObject();
  }
  
  
}
