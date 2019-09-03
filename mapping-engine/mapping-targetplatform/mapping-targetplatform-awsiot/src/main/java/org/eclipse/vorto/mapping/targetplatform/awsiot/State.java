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

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;

public class State {
  
  private Map<String,Object> reported = new HashMap<String, Object>();
  
  public void addReported(InfomodelValue value) {
    value.getProperties().keySet().stream().forEach(key -> {
      FunctionblockValue fbValue = value.get(key);
      reported.put(key, fbValue.serialize());
    });
  }

  public Map<String, Object> getReported() {
    return reported;
  }

  public void setReported(Map<String, Object> reported) {
    this.reported = reported;
  }
  
  
  
}
