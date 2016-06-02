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

class DummyDeviceTemplate implements ITemplate<FunctionblockModel> {
	
	override getContent(FunctionblockModel model,InvocationContext context) {
		'''package «BaseDriverUtil.getDevicePackage + "." + model.name.toLowerCase»;
		
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;

import com.bosch.functionblock.dummy.internal.device.AbstractDummyDevice;
		
public class Dummy«model.name»Device extends AbstractDummyDevice {
	
	public Dummy«model.name»Device(BundleContext bundleContext, String serialNo) {
		super(bundleContext, serialNo);
	}
					
	public boolean isAlive() {
		return true;
	}
	
	@Override	
	public void sendCommand(String commandId, Object payload){
		//TODO:Please implement this method to make it communicate to actual device
		logger.trace("Received command {}, payload: {}", commandId, payload);
	}
	
	@Override
	public String getEventTopic(String eventName) {
		return "DeviceDriver/«model.name»/"  + eventName;		
	}	
	
	@Override
	public Map<String, List<String>> getAllEventPropertyNames() {
		Map<String, List<String>> allEventPropertyNames = new HashMap<String, List<String>>();
		«FOR event: model.functionblock.events»
			allEventPropertyNames.put("«event.name»", get«event.name.toFirstUpper»EventPropertyNames());
		«ENDFOR»
		return allEventPropertyNames;
	}	
		
	«FOR event: model.functionblock.events»
	private List<String> get«event.name.toFirstUpper»EventPropertyNames(){
		List<String> propertyNames = new ArrayList<String>();
		«FOR property: event.properties»
			propertyNames.add("«property.name»");	
		«ENDFOR»
		return propertyNames;
	}
	«ENDFOR»				
}'''
	}
	
}
