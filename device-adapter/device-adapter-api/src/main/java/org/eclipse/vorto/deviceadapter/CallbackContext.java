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
package org.eclipse.vorto.deviceadapter;

import org.eclipse.vorto.model.Infomodel;

/**
 * Context to be passed with callbacks to the application, to help interpret the result.
 */
class CallbackContext {

  private String deviceId;
  private String infomodelProperty;
  private Infomodel infomodel;

  /**
   * Instantiates a new Context.
   *
   * @param deviceId the device id
   * @param infomodelProperty name of the infomodel property
   * @param infomodel the infomodel
   */
  public CallbackContext(String deviceId, String infomodelProperty, Infomodel infomodel) {
    this.deviceId = deviceId;
    this.infomodel = infomodel;
  }

  /**
   * Gets device id.
   *
   * @return the device id
   */
  public String getDeviceId() {
    return deviceId;
  }

  /**
   * Gets infomodel.
   *
   * @return the infomodel
   */
  public Infomodel getInfomodel() {
    return infomodel;
  }

  /**
   * Gets the Information Model Property
   * 
   * @return
   */
  public String getInfomodelProperty() {
    return infomodelProperty;
  }

}
