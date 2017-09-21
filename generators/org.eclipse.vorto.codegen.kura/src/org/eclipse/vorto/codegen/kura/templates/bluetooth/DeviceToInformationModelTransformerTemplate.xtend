/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.kura.templates.bluetooth

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * 
 * @author Erle Czar Mantos - Robert Bosch (SEA) Pte. Ltd.
 *
 */
class DeviceToInformationModelTransformerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''DeviceTo«context.name»Transformer.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.getJavaPackage(element)»;

import java.util.Optional;
import java.util.function.Function;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.bluetooth.BluetoothDevice;
import org.eclipse.kura.bluetooth.BluetoothGatt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import «Utils.getJavaPackage(element)».model.«element.name»;

«FOR fbProperty : element.properties»
import «Utils.getJavaPackage(element)».model.«fbProperty.type.name»;
«ENDFOR»

public class DeviceTo«element.name»Transformer implements Function<BluetoothDevice, Optional<«element.name»>> {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceTo«element.name»Transformer.class);
	private «element.name»Configuration configuration;
	
	public DeviceTo«element.name»Transformer(«element.name»Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Optional<«element.name»> apply(BluetoothDevice device) {
		try {
			BluetoothGatt gatt = device.getBluetoothGatt();
			if (gatt.connect()) {
				«element.name» «element.name.toLowerCase» = new «element.name»(getResourceId(device));
				
				«FOR fbProperty : element.properties»
				if (configuration.enable«fbProperty.name.toFirstUpper») {
					enable«fbProperty.name.toFirstUpper»(gatt);
					«element.name.toLowerCase».set«fbProperty.name.toFirstUpper»(get«fbProperty.name.toFirstUpper»(gatt));
				}
				
				«ENDFOR»
				
				gatt.disconnect();
				return Optional.of(«element.name.toLowerCase»);
			}
		} catch (KuraException e) {
			logger.error("Error in getting device data", e);
			return Optional.empty();
		}
		
		return Optional.empty();
	}
	
	/*
	 * Modify this to change how your resourceId is generated
	 */
	private String getResourceId(BluetoothDevice device) {
		return "«element.name»:" + device.getAdress().replace(":", "");
	}
	
	«FOR fbProperty : element.properties»
	/*------------------ Implement method for «fbProperty.name.toFirstUpper» here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of «fbProperty.type.name» value from bluetooth device
	 */
	private void enable«fbProperty.name.toFirstUpper»(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get «fbProperty.type.name» value from bluetooth device
	 */
	private «fbProperty.type.name» get«fbProperty.name.toFirstUpper»(BluetoothGatt gatt) {
		«fbProperty.type.name» «fbProperty.type.name.toLowerCase» = new «fbProperty.type.name»();
		try {
		
			//TODO: insert code that reads «fbProperty.type.name» and converts into «fbProperty.type.name» object
			String value = gatt.readCharacteristicValue("");
			
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return «fbProperty.type.name.toLowerCase»;
	}
	
	«ENDFOR»
}

'''
	}
	
}