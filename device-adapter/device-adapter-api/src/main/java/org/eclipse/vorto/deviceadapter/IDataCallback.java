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

import org.eclipse.vorto.model.runtime.FBEventValue;
import org.eclipse.vorto.model.runtime.FunctionblockValue;

/**
 * To be implemented by the client application to handle the device data. The device data is already
 * mapped to Vorto compliant data structures.
 */
public interface IDataCallback {

  /**
   * Is Invoked when the adapter receives status data from the device
   *
   * @param status the status
   * @param ctx the ctx
   */
  void onStatusReceived(FunctionblockValue deviceData, CallbackContext context);

  /**
   * Is Invoked when the adapter receives configuration data from the device
   *
   * @param deviceProperties the device properties
   * @param ctx the ctx
   */
  void onConfigurationReceived(FunctionblockValue deviceData, CallbackContext context);

  /**
   * Is Invoked when the adapter receives event data from the device
   * 
   * @param event
   * @param context
   */
  void onEventReceived(FBEventValue event, CallbackContext context);


}
