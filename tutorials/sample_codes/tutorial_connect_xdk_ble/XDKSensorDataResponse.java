package com.example.kura.device;

import java.util.Optional;

/**
 * Let's give this the responsibility to accept a string and decide what string
 * it is.
 * 
 * @author emantos@gmail.com
 *
 */
public class XDKSensorDataResponse {
	private Optional<String> accAndGyro = Optional.empty();
	private Optional<String> environmentSensor = Optional.empty();
	private Optional<String> magnetometerLedStatus = Optional.empty();

	public boolean accept(String response) {
		if (response != null) {
			String[] components = response.split("\\s+");
			if (components.length > 5 && components[3].equals("0x0036") && components[5].equals("01")) {
				environmentSensor = Optional.of(response);
			} else if (components.length > 3 && components[3].equals("0x0034")) {
				accAndGyro = Optional.of(response);
			} else if (components.length > 5 && components[3].equals("0x0036") && components[5].equals("02")) {
				magnetometerLedStatus = Optional.of(response);
			} else {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public boolean isComplete() {
		return accAndGyro.isPresent() && environmentSensor.isPresent() && magnetometerLedStatus.isPresent();
	}
	
	public boolean isPartiallyComplete() {
		return accAndGyro.isPresent() || environmentSensor.isPresent() || magnetometerLedStatus.isPresent();
	}
	
	public Optional<String> getAccAndGyro() {
		return accAndGyro;
	}

	public Optional<String> getEnvironmentSensor() {
		return environmentSensor;
	}

	public Optional<String> getMagnetometerLedStatus() {
		return magnetometerLedStatus;
	}

	@Override
	public String toString() {
		return "\nXDKSensorDataResponse [\naccAndGyro='" + accAndGyro.orElse("null") + "', \nenvironmentSensor='" + environmentSensor.orElse("null")
				+ "', \nmagnetometerLedStatus='" + magnetometerLedStatus.orElse("null") + "'\n]";
	}
}
