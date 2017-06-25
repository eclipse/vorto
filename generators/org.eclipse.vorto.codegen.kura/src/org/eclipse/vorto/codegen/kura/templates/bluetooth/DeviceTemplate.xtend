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
 * @author Alexander Edelmann
 */
class DeviceTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»Device.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.javaPackageBasePath»/bluetooth'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package «Utils.javaPackage».bluetooth;
		
		import org.eclipse.kura.KuraException;
		import org.eclipse.kura.bluetooth.BluetoothDevice;
		import org.eclipse.kura.bluetooth.BluetoothGatt;
		import org.eclipse.kura.bluetooth.BluetoothGattSecurityLevel;
		import org.eclipse.kura.bluetooth.BluetoothLeNotificationListener;
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		
		«FOR fbProperty : element.properties»
		import «Utils.javaPackage».cloud.«fbProperty.type.name»;
		«ENDFOR»
		
		public class «element.name»Device implements BluetoothLeNotificationListener {
		
			private static final Logger logger = LoggerFactory.getLogger(«element.name»Device.class);
		
			private BluetoothGatt bluetoothGatt;
			private BluetoothDevice device;
			private boolean isConnected;
		
			public BoschGlmDevice(BluetoothDevice bluetoothDevice) {
				this.device = bluetoothDevice;
				this.isConnected = false;
			}
		
			@Override
			public void onDataReceived(String handle, String value) {
				if (checkHandleIf«element.name»(handle)) {
		            logger.info("Received «element.name» data: " + value);
		        }
			}
		
			private boolean checkHandleIf«element.name»(String handle) {
				return false;//TODO: add condition to check if data comes from «element.name» device
			}
		
			public void disconnect() {
				if (this.bluetoothGatt != null) {
					this.bluetoothGatt.disconnect();
					this.isConnected = false;
				}
		
			}
		
			public BluetoothDevice getBluetoothDevice() {
				return this.device;
			}
		
			public void setBluetoothDevice(BluetoothDevice device) {
				this.device = device;
			}
		
			public boolean isConnected() {
				this.isConnected = checkConnection();
				return this.isConnected;
			}
		
			public boolean checkConnection() {
				if (this.bluetoothGatt != null) {
					boolean connected = false;
					try {
						connected = this.bluetoothGatt.checkConnection();
					} catch (KuraException e) {
						logger.error(e.toString());
					}
					if (connected) {
						this.isConnected = true;
						return true;
					} else {
						// If connect command is not executed, close gatttool
						this.bluetoothGatt.disconnect();
						this.isConnected = false;
						return false;
					}
				} else {
					this.isConnected = false;
					return false;
				}
			}
		
			public void setSecurityLevel(BluetoothGattSecurityLevel level) {
				if (this.bluetoothGatt != null) {
					this.bluetoothGatt.setSecurityLevel(level);
				}
			}
		
			public BluetoothGattSecurityLevel getSecurityLevel() {
				BluetoothGattSecurityLevel level = BluetoothGattSecurityLevel.UNKNOWN;
				try {
					if (this.bluetoothGatt != null) {
						level = this.bluetoothGatt.getSecurityLevel();
					}
				} catch (KuraException e) {
					logger.error("Get security level failed", e);
				}
		
				return level;
			}
			
			«FOR fbProperty : element.properties»
			public «fbProperty.type.name» read«fbProperty.name.toFirstUpper»() {
				«fbProperty.type.name» «fbProperty.name» = new «fbProperty.type.name»();
				try {
				
					//TODO: insert code that reads «fbProperty.name» and converts into «fbProperty.type.name» object
					String value = this.bluetoothGatt.readCharacteristicValue("");
					
				} catch (KuraException e) {
					 logger.error(e.toString());
				}
				return «fbProperty.name»;
			}
			
			public void enable«fbProperty.name.toFirstUpper»() {
				//TODO: Insert code here to enable «fbProperty.name»
				this.bluetoothGatt.writeCharacteristicValue("", "");
			}
		«ENDFOR»
		
			public boolean connect(String adapterName) {
				 this.bluetoothGatt = this.device.getBluetoothGatt();
			        boolean connected = false;
			        try {
			            connected = this.bluetoothGatt.connect(adapterName);
			        } catch (KuraException e) {
			            logger.error(e.toString());
			        }
			        if (connected) {
			            this.bluetoothGatt.setBluetoothLeNotificationListener(this);
			            this.isConnected = true;
			            return true;
			        } else {
			            // If connect command is not executed, close gatttool
			            this.bluetoothGatt.disconnect();
			            this.isConnected = false;
			            return false;
			        }
			}
		
			public String getResourceId() {
				//TODO: insert code that reads technical device id from the device
				return getBluetoothDevice().getAdress();
			}
		
		}
		
		'''
	}
	
}