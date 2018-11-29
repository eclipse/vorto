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
