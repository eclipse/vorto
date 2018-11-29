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
