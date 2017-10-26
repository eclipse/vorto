/*******************************************************************************
 *  Copyright (c) 2017 Oliver Meili
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Oliver Meili <omi@ieee.org>
 *******************************************************************************/
package org.eclipse.vorto.codegen.ble.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.PropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.codegen.ble.model.blegatt.Device
import org.eclipse.vorto.codegen.ble.model.blegatt.Service
import org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic
import org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

public abstract class BleGattTemplate<T extends Model> implements IFileTemplate<T> {
	
	public static String rootPath;
	
	def Service findService(Device device, FunctionblockModel fbm) {
		for (Service s : device.getServices()) {
			for (FunctionblockModel f : s.getFunctionblocks()) {
				if (fbm.equals(f)) {
					return s
				}
			}
		}
		return null;
	}
	
	def Characteristic findCharacteristic(Device device, Property property) {
		for (Service s : device.getServices()) {
			for (Characteristic ch : s.getCharacteristics()) {
				for (CharacteristicProperty cp : ch.getProperties()) {
					if (property.equals(cp.getProperty())) {
						return ch
					}
				}
			}
		}
		return null;
	}
}
