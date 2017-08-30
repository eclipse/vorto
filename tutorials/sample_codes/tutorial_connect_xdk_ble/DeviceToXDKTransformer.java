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
package com.example.kura;

import java.util.Optional;
import java.util.function.Function;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.bluetooth.BluetoothDevice;
import org.eclipse.kura.bluetooth.BluetoothGatt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.kura.cloud.Accelerometer;
import com.example.kura.cloud.AlertNotification;
import com.example.kura.cloud.Barometer;
import com.example.kura.cloud.Firmware_Update;
import com.example.kura.cloud.Gyrometer;
import com.example.kura.cloud.Humidity;
import com.example.kura.cloud.Illuminance;
import com.example.kura.cloud.Light_Control;
import com.example.kura.cloud.Magnetometer;
import com.example.kura.cloud.Temperature;
import com.example.kura.cloud.XDK;
import com.example.kura.device.XDKBluetoothGatt;

public class DeviceToXDKTransformer implements Function<BluetoothDevice, Optional<XDK>> {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceToXDKTransformer.class);
	private XDKConfiguration configuration;
	
	public DeviceToXDKTransformer(XDKConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Optional<XDK> apply(BluetoothDevice device) {
		try {
			//BluetoothGatt gatt = device.getBluetoothGatt();
			BluetoothGatt gatt = new XDKBluetoothGatt(device.getAdress());
			if (gatt.connect()) {
				XDK xdk = new XDK(getResourceId(device));
				
				if (configuration.enableAccelerometer) {
					enableAccelerometer(gatt);
					xdk.setAccelerometer(getAccelerometer(gatt));
				}
				
				if (configuration.enableFirmware_update) {
					enableFirmware_update(gatt);
					xdk.setFirmware_update(getFirmware_update(gatt));
				}
				
				if (configuration.enableGyrometer) {
					enableGyrometer(gatt);
					xdk.setGyrometer(getGyrometer(gatt));
				}
				
				if (configuration.enableHumidity) {
					enableHumidity(gatt);
					xdk.setHumidity(getHumidity(gatt));
				}
				
				if (configuration.enableMagnetometer) {
					enableMagnetometer(gatt);
					xdk.setMagnetometer(getMagnetometer(gatt));
				}
				
				if (configuration.enableBarometer) {
					enableBarometer(gatt);
					xdk.setBarometer(getBarometer(gatt));
				}
				
				if (configuration.enableTemperature) {
					enableTemperature(gatt);
					xdk.setTemperature(getTemperature(gatt));
				}
				
				if (configuration.enableIlluminance) {
					enableIlluminance(gatt);
					xdk.setIlluminance(getIlluminance(gatt));
				}
				
				if (configuration.enableLight_control) {
					enableLight_control(gatt);
					xdk.setLight_control(getLight_control(gatt));
				}
				
				if (configuration.enableLight_control1) {
					enableLight_control1(gatt);
					xdk.setLight_control1(getLight_control1(gatt));
				}
				
				if (configuration.enableLight_control2) {
					enableLight_control2(gatt);
					xdk.setLight_control2(getLight_control2(gatt));
				}
				
				if (configuration.enableAlertnotification) {
					enableAlertnotification(gatt);
					xdk.setAlertnotification(getAlertnotification(gatt));
				}
				
				
				gatt.disconnect();
				return Optional.of(xdk);
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
		return "xdk:" + device.getAdress().replace(":", "");
	}
	
	/*------------------ Implement method for Accelerometer here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Accelerometer value from bluetooth device
	 */
	private void enableAccelerometer(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Accelerometer value from bluetooth device
	 */
	private Accelerometer getAccelerometer(BluetoothGatt gatt) {
		Accelerometer accelerometer = new Accelerometer();
		try {
			String value = gatt.readCharacteristicValue("0x0034");
			if (value != null) {
				String[] components = value.split("\\s+");
				if (components.length > 10) {
					accelerometer.setX_value(((short) Integer.parseInt(components[6] + components[5], 16)));
					accelerometer.setY_value(((short) Integer.parseInt(components[8] + components[7], 16)));
					accelerometer.setZ_value(((short) Integer.parseInt(components[10] + components[9], 16)));
					
					logger.info("Accelerometer = (" + accelerometer.getX_value() + "," + 
							accelerometer.getY_value() + "," + accelerometer.getZ_value() + ")");
				}
			}
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return accelerometer;
	}
	
	/*------------------ Implement method for Firmware_update here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Firmware_Update value from bluetooth device
	 */
	private void enableFirmware_update(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Firmware_Update value from bluetooth device
	 */
	private Firmware_Update getFirmware_update(BluetoothGatt gatt) {
		Firmware_Update firmware_update = new Firmware_Update();
		try {
		
			//TODO: insert code that reads Firmware_Update and converts into Firmware_Update object
			//String value = gatt.readCharacteristicValue("");
			
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return firmware_update;
	}
	
	/*------------------ Implement method for Gyrometer here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Gyrometer value from bluetooth device
	 */
	private void enableGyrometer(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Gyrometer value from bluetooth device
	 */
	private Gyrometer getGyrometer(BluetoothGatt gatt) {
		Gyrometer gyrometer = new Gyrometer();
		try {
			String value = gatt.readCharacteristicValue("0x0034");
			if (value != null) {
				String[] components = value.split("\\s+");
				if (components.length > 16) {
					gyrometer.setX_value(((short) Integer.parseInt(components[12] + components[11], 16)));
					gyrometer.setY_value(((short) Integer.parseInt(components[14] + components[13], 16)));
					gyrometer.setZ_value(((short) Integer.parseInt(components[16] + components[15], 16)));
					
					logger.info("Gyrometer = (" + gyrometer.getX_value() + "," + 
							gyrometer.getY_value() + "," + gyrometer.getZ_value() + ")");
				}
			}
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return gyrometer;
	}
	
	/*------------------ Implement method for Humidity here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Humidity value from bluetooth device
	 */
	private void enableHumidity(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Humidity value from bluetooth device
	 */
	private Humidity getHumidity(BluetoothGatt gatt) {
		Humidity humidity = new Humidity();
		try {
			String value = gatt.readCharacteristicValue("0x003601");
			if (value != null) {
				String[] components = value.split("\\s+");
				if (components.length > 22) {
					humidity.setSensor_value((int) Long.parseLong(components[22] + components[21] + components[20] + components[19], 16));
					logger.info("Humidity = " + humidity.getSensor_value());
				}
			}			
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return humidity;
	}
	
	/*------------------ Implement method for Magnetometer here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Magnetometer value from bluetooth device
	 */
	private void enableMagnetometer(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Magnetometer value from bluetooth device
	 */
	private Magnetometer getMagnetometer(BluetoothGatt gatt) {
		Magnetometer magnetometer = new Magnetometer();
		try {
			String value = gatt.readCharacteristicValue("0x003602");
			if (value != null) {
				String[] components = value.split("\\s+");
				if (components.length > 13) {
					magnetometer.setX_value(((short) Integer.parseInt(components[7] + components[6], 16)));
					magnetometer.setY_value(((short) Integer.parseInt(components[9] + components[8], 16)));
					magnetometer.setZ_value(((short) Integer.parseInt(components[11] + components[10], 16)));
					magnetometer.setCompass_direction((Integer.parseInt(components[13] + components[12], 16)));
					
					logger.info("Magnetometer = (" + magnetometer.getX_value() + "," + 
							magnetometer.getY_value() + "," + magnetometer.getZ_value() + "," + magnetometer.getCompass_direction() + ")");
				}
			}	
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return magnetometer;
	}
	
	/*------------------ Implement method for Barometer here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Barometer value from bluetooth device
	 */
	private void enableBarometer(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Barometer value from bluetooth device
	 */
	private Barometer getBarometer(BluetoothGatt gatt) {
		Barometer barometer = new Barometer();
		try {
			String value = gatt.readCharacteristicValue("0x003601");
			if (value != null) {
				String[] components = value.split("\\s+");
				if (components.length > 14) {
					barometer.setSensor_value((int) Long.parseLong(components[14] + components[13] + components[12] + components[11], 16));
					logger.info("Barometer = " + barometer.getSensor_value());
				}
			}	
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return barometer;
	}
	
	/*------------------ Implement method for Temperature here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Temperature value from bluetooth device
	 */
	private void enableTemperature(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Temperature value from bluetooth device
	 */
	private Temperature getTemperature(BluetoothGatt gatt) {
		Temperature temperature = new Temperature();
		try {
			String value = gatt.readCharacteristicValue("0x003601");
			if (value != null) {
				String[] components = value.split("\\s+");
				if (components.length > 18) {
					temperature.setSensor_value(((int) Long.parseLong(components[18] + components[17] + components[16] + components[15], 16))/1000);
					temperature.setSensor_units("Celsius");
					logger.info("temperature.getSensor_value() = " + temperature.getSensor_value());
				}
			}
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return temperature;
	}
	
	/*------------------ Implement method for Illuminance here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Illuminance value from bluetooth device
	 */
	private void enableIlluminance(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Illuminance value from bluetooth device
	 */
	private Illuminance getIlluminance(BluetoothGatt gatt) {
		Illuminance illuminance = new Illuminance();
		try {
			String value = gatt.readCharacteristicValue("0x003601");
			if (value != null) {
				String[] components = value.split("\\s+");
				if (components.length > 9) {
					illuminance.setSensor_value((int) Long.parseLong(components[9] + components[8] + components[7] + components[6], 16));
					logger.info("illuminance = " + illuminance.getSensor_value());
				}
			}
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return illuminance;
	}
	
	/*------------------ Implement method for Light_control here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Light_Control value from bluetooth device
	 */
	private void enableLight_control(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Light_Control value from bluetooth device
	 */
	private Light_Control getLight_control(BluetoothGatt gatt) {
		return _getLightControl(gatt, 1);
	}
	
	/*------------------ Implement method for Light_control1 here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Light_Control value from bluetooth device
	 */
	private void enableLight_control1(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Light_Control value from bluetooth device
	 */
	private Light_Control getLight_control1(BluetoothGatt gatt) {
		return _getLightControl(gatt, 2);
	}
	
	/*------------------ Implement method for Light_control2 here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of Light_Control value from bluetooth device
	 */
	private void enableLight_control2(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get Light_Control value from bluetooth device
	 */
	private Light_Control getLight_control2(BluetoothGatt gatt) {
		return _getLightControl(gatt, 4);
	}
	
	private Light_Control _getLightControl(BluetoothGatt gatt, int mask) {
		Light_Control light_control = new Light_Control();
		try {
			String value = gatt.readCharacteristicValue("0x003602");
			if (value != null) {
				String[] components = value.split("\\s+");
				if (components.length > 14) {
					int intValue = Integer.parseInt(components[14], 16);
					light_control.setOn_off((intValue & mask) > 0);
				}
			}
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return light_control;
	}
	
	/*------------------ Implement method for Alertnotification here! (start) -----------------*/
	/*
	 * Implement here the actual code to enable getting of AlertNotification value from bluetooth device
	 */
	private void enableAlertnotification(BluetoothGatt gatt) {
		gatt.writeCharacteristicValue("", "");
	}
	
	/*
	 * Implement the actual code to get AlertNotification value from bluetooth device
	 */
	private AlertNotification getAlertnotification(BluetoothGatt gatt) {
		AlertNotification alertnotification = new AlertNotification();
		try {
		
			//TODO: insert code that reads AlertNotification and converts into AlertNotification object
			String value = gatt.readCharacteristicValue("");
			
		} catch (KuraException e) {
			 logger.error(e.toString());
		}
		return alertnotification;
	}
	
}

