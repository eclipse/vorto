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
