/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template.device

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BaseDriverUtil
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class IDummyDeviceTemplate implements ITemplate<FunctionblockModel> {
	
	override getContent(FunctionblockModel model, InvocationContext context) {
		'''package «BaseDriverUtil.getDeviceApiPackage()»;

import java.util.List;
import java.util.Map;

public interface IDummyDevice {

	/**
	 * Send a event to OSGI event bus where the device is registered
	 * @param eventName: Name of event, as defined in function block DSL
	 * @param payload
	 */
	void sendEvent(String eventName, Map<String,?> payload);
	
	/**
	 * Get serial no which should uniquely identify the device instance
	 * @return
	 */
	String getSerialNo();
	
	/**
	 * Get all event property names	 
	 * @return Map with key contains all event names, and value contain all property names of the event
	 */
	Map<String, List<String>> getAllEventPropertyNames();
	
	
	/**
	 * Send command to device
	 * @param commandId: id of command
	 * @param payload: data 
	 */
	void sendCommand(String commandId, Object payload);

	/**
	 * Get topic of device osgi event
	 * @param eventName
	 * @return
	 */
	String getEventTopic(String eventName);
}

'''
	}
	
}
