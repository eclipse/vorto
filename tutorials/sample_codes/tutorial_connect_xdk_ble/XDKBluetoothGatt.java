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
package com.example.kura.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.bluetooth.BluetoothGatt;
import org.eclipse.kura.bluetooth.BluetoothGattCharacteristic;
import org.eclipse.kura.bluetooth.BluetoothGattSecurityLevel;
import org.eclipse.kura.bluetooth.BluetoothGattService;
import org.eclipse.kura.bluetooth.BluetoothLeNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XDKBluetoothGatt implements BluetoothGatt {
	
	private static final Logger logger = LoggerFactory.getLogger(XDKBluetoothGatt.class);
	
	private XDKSensorDataResponse dataResponse;
	private String bluetoothAddress;
	
	public XDKBluetoothGatt(String bluetoothAddress) {
		this.bluetoothAddress = bluetoothAddress;
	}

	public boolean connect(String adapterId) throws KuraException {
		return connect();
	}
	
	public boolean connect() throws KuraException { 
		dataResponse = getSensorData(bluetoothAddress);  
		return dataResponse.isComplete() || dataResponse.isPartiallyComplete(); 
	}
	
	public void writeCharacteristicValue(String arg0, String arg1) {}
	
	public String readCharacteristicValue(String uuid) throws KuraException  {
		if (uuid.equals("0x0034")) {
			return dataResponse.getAccAndGyro().orElse(null);
		} else if (uuid.equals("0x003601")) {
			return dataResponse.getEnvironmentSensor().orElse(null);
		} else if (uuid.equals("0x003602")) {
			return dataResponse.getMagnetometerLedStatus().orElse(null); 
		}
		
		return null;
	}
	
	private XDKSensorDataResponse getSensorData(String bluetoothAddress) {
		XDKSensorDataResponse dataResponse = new XDKSensorDataResponse();
		
		try {
			logger.info("Executing gatttool process now on " + bluetoothAddress);
			Process process = new ProcessBuilder("gatttool", 
					"--device=" + bluetoothAddress, "--char-write-req", "--value=00", 
					"--handle=0x0039", "--listen").start();
			logger.info("Process started.");
			BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			ExecutorService processExecutorService = Executors.newSingleThreadExecutor();

			boolean read = true;
			while(read) {
				try {
					Future<String> responseReader = processExecutorService.submit(new Callable<String>() {
						@Override
						public String call() throws Exception {
							return outputReader.readLine();
						}
					});
					
					dataResponse.accept(responseReader.get(5, TimeUnit.SECONDS));
					
					if (dataResponse.isComplete()) {
						read = false;
						process.destroy();
					}
					
				} catch (TimeoutException e) {
					logger.info("No more data to be read from bluetooth process.");
					read = false;
					process.destroy();
				} catch (ExecutionException | InterruptedException e) {
					logger.error("-erle- : ", e);
				} 
			}
			
			logger.info(dataResponse.toString());
			
		} catch (IOException e) {
			logger.error("-erle- : ", e);
		}
		return dataResponse;
	}
	
	/* We don't care about the rest of this functions */
	public boolean checkConnection() throws KuraException { return false; }
	public void disconnect() {}
	public List<BluetoothGattCharacteristic> getCharacteristics(String arg0, String arg1) { return null; }
	public BluetoothGattSecurityLevel getSecurityLevel() throws KuraException { return null; }
	public BluetoothGattService getService(UUID arg0) { return null; }
	public List<BluetoothGattService> getServices() { return null; }
	public String readCharacteristicValueByUuid(UUID arg0) throws KuraException { return null; }
	public void setBluetoothLeNotificationListener(BluetoothLeNotificationListener arg0) { }
	public void setSecurityLevel(BluetoothGattSecurityLevel arg0) {}
}
