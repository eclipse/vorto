/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.deviceadapter;

import org.eclipse.vorto.model.runtime.FunctionblockValue;

/**
 * This interface presents the API on how to receive/send data from/to an Adapter. The Adapter
 * that implements this interface is responsible for implementing the functionality.
 */
public interface IDeviceData {
	
	
    /**
     * Sets the configuration on the device.
     *
     * @param configurationProperties the configuration properties
     * @param infoModelProperty       name of the infomodel property
     * @param propertyName			  name of the function block property
     * @param propertyValue			  value of the function block property
     * @param deviceId				  deviceId
     */
    void setConfiguration(String infoModelProperty, String propertyName, Object propertyValue, String deviceId);

    /**
     * Receive data by using some blocking mechanism.
     *
     * @param deviceID   the device id
     * @param fbProperty the fb property
     * @return the functionblock data
     */
    FunctionblockValue receiveFBData(String fbProperty, String deviceId);

    /**
     * Receive fb data async, starts a new read process and calls the callback once done.
     *
     * @param callback the callback
     * @param deviceId the device id
     */
    void receiveFBDataAsync(IDataCallback callback, String deviceId);
}
