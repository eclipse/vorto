/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

/**
 * Callback Discovery Handler that a client application implements when a device is discovered by
 * the adapter.
 */
public interface IDeviceDiscoveryCallback {

  /**
   * Is invoked when by the adapter when a device is found.
   *
   * @param deviceInfo the device info
   */
  void onDeviceFound(DeviceInfo deviceInfo);
}
