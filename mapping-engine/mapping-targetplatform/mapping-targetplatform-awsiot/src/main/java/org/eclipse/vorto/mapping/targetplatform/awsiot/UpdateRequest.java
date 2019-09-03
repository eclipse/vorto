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

public class UpdateRequest {

  private State state = null;
  
  public static UpdateRequest create() {
    return new UpdateRequest();
  }
  
  public UpdateRequest withReported(InfomodelValue value) {
    State state = new State();
    state.addReported(value);
    this.state = state;
    return this;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }
  
  
}
