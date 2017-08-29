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
class DeviceFilterTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»DeviceFilter.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.javaPackageBasePath»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.javaPackage»;

import java.util.function.Predicate;

import org.eclipse.kura.bluetooth.BluetoothDevice;

public class «element.name»DeviceFilter implements Predicate<BluetoothDevice> {

	@Override
	public boolean test(BluetoothDevice bluetoothDevice) {
		return bluetoothDevice.getName().contains("BCDS");
	}

}
'''		
	}
	
}