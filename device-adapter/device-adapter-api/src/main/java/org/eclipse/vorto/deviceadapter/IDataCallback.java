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

import org.eclipse.vorto.model.runtime.FBEventValue;
import org.eclipse.vorto.model.runtime.FunctionblockValue;

/**
 * To be used by the Adapter to communicate data changes back to the Application.
 */
public interface IDataCallback {

    /**
     * On status.
     *
     * @param status the status
     * @param ctx    the ctx
     */
    void onStatusReceived(FunctionblockValue data, Context context);

    /**
     * On configuration.
     *
     * @param deviceProperties the device properties
     * @param ctx              the ctx
     */
    void onConfigurationReceived(FunctionblockValue data, Context context);
    
    /**
     * 
     * @param event
     * @param context
     */
    void onEventReceived(FBEventValue event, Context context);


}
