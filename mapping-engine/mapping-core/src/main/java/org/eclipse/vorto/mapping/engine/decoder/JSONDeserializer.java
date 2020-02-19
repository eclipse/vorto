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
package org.eclipse.vorto.mapping.engine.decoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONDeserializer implements IPayloadDeserializer {

  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
  
  @Override
  public Object deserialize(String source) {
    return gson.fromJson((String)source, Object.class); 
  }

}
