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

import org.eclipse.vorto.model.runtime.FunctionblockValue;

/**
 * This interface deals with the data exchange between the client application and the device. The
 * data is compliant to the Vorto Information Model.
 */
public interface IDeviceData {

  /**
   * Creates a new configuration to set properties. Send the configuration to device via
   * {@link IDeviceData#setConfiguration(INewConfiguration, String)}}
   * 
   * @param infomodelProperty
   * @return new configuration which can be set on the device via
   *         {@link IDeviceData#setConfiguration(INewConfiguration, String)}}
   */
  INewConfiguration newConfiguration(String infomodelProperty);

  /**
   * Writes configuration values on the device
   *
   * @param configuration configuration to write to the device
   * @param deviceId deviceId of the device. See
   *        {@link IDeviceDiscovery#listAvailableDevices(IDeviceDiscoveryCallback, int)} } for
   *        device id discovery
   * @throws {@link DeviceConfigurationProblem}} if the given configuration cannot be written to the
   *         device
   */
  void setConfiguration(INewConfiguration configuration, String deviceId);

  /**
   * Receive device data by using some blocking mechanism.
   *
   * @param infomodelProperty name of the infomodel property
   * @param deviceId deviceId of the device. See
   *        {@link IDeviceDiscovery#listAvailableDevices(IDeviceDiscoveryCallback, int)} } for
   *        device id discovery
   * @return the functionblock data
   */
  FunctionblockValue receive(String infomodelProperty, String deviceId);

  /**
   * Receive device data asynchronously.
   *
   * @param infomodelProperty name of the infomodel property
   * @param deviceId deviceId of the device. See
   *        {@link IDeviceDiscovery#listAvailableDevices(IDeviceDiscoveryCallback, int)} } for
   *        device id discovery
   * @param dataCallback the callback handler when data is received by the device
   */
  void receiveAsync(String infomodelProperty, String deviceId, IDataCallback dataCallback);

  /**
   * Configuration Problem that may occur during writing configuration to the device
   *
   */
  public class DeviceConfigurationProblem extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DeviceConfigurationProblem(String msg, Throwable cause) {
      super(msg, cause);
    }
  }
}
