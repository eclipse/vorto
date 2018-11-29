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
 * Class to hold Device information to be passed between the Adapter and the Application using it.
 */
public class DeviceInfo {

  private String deviceId;
  private Infomodel informationModel;

  /**
   * Gets device id.
   *
   * @return the device id
   */
  public String getDeviceId() {
    return deviceId;
  }

  /**
   * Gets information model.
   *
   * @return the information model
   */
  public Infomodel getInformationModel() {
    return informationModel;
  }

  /**
   * Instantiates a new Device info.
   *
   * @param deviceID the device id
   * @param informationModel the information model
   */
  public DeviceInfo(String deviceId, Infomodel informationModel) {
    this.deviceId = deviceId;
    this.informationModel = informationModel;
  }
}
