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
