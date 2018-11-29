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
 * Device Adapter that abstracts the communication layer between the client application and the
 * device. The data returned or set is based on Vorto compliant data structures according to the
 * Vorto Information Model
 */
public interface IDeviceAdapter extends IDeviceDiscovery, IDeviceData {

  /**
   * Gets the target platform for which this adapter is configured
   * 
   * @return
   */
  String getTargetPlatform();

  /**
   * Gets the Information model for which this adapter is configured
   * 
   * @return
   */
  Infomodel getInfomodel();

}
