# Connecting a GrovePi Sensor to the Bosch IoT Suite via MQTT

This tutorial explains how to connect a GrovePi sensor to Bosch IoT Suite using MQTT using Vorto.

## Prerequisites

* Bosch ID User Account

* You have booked an asset communication package of the Bosch IoT Suite (refer to [Getting Started Guide](https://www.bosch-iot-suite.com/tutorials/getting-started-asset-communication/)).

* You have created a Vorto Information Model for the device (refer to [Describing a device](tisensor.md)).

* You have registered the device using the Vorto Information Model in the Bosch IoT Suite (refer to [Registering a Device in the Bosch IoT Suite](../dx_register_device)).

## Tools

* [Python 3.x](https://www.python.org/)

* IDEs - for Python: [Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python) 

* [Paho Python Client](https://eclipse.org/paho/clients/python/)

* [GrovePi Sensor Kit](https://www.dexterindustries.com/grovepi/)

## Proceed as follows

1. Setup your device.

	In this example Raspberry Pi is used but you can use any device which can run python.

	* [Install raspbian on the Raspberry Pi](https://www.raspberrypi.org/learning/software-guide/).

	* [Connect the pi to wifi](https://www.raspberrypi.org/learning/software-guide/wifi/).

	* [Enable ssh connection on your pi](https://www.raspberrypi.org/documentation/remote-access/ssh/)	.

	* [Install python and required modules](https://github.com/eclipse/vorto/blob/development/tutorials/tutorial_install_python_and_required_python_modules.md).

2. Setup your development environment (on your development machine).

	* [Install Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python).

	* [Install python and required modules](https://github.com/eclipse/vorto/blob/development/tutorials/tutorial_install_python_and_required_python_modules.md).

3. Generate application code using the Python generator.

	**Download the python source code template**

	- Open the Information Model in the [Vorto Repository](http://vorto.eclipse.org)

	- Select the **Bosch IoT Suite Generator** on the right handside.

		<img width="300" src="../img/connect_grovepi/python-generator.png" style="border:3px !important;">

	- Store the ZIP file and extract the source code.

4. Install GrovePi Python dependencies on the Raspberry Pi.

	Install necessary dependencies for GrovePi on the Raspberry Pi. To do so, just execute the commands 

		sudo curl https://raw.githubusercontent.com/DexterInd/Raspbian_For_Robots/master
		upd_script/fetch_grovepi.sh | bash

		sudo reboot

	You can find full instructions [here](https://www.dexterindustries.com/GrovePi/get-started-with-the-grovepi/setting-software/).

5. Connect the sensor to the Raspberry Pi.

	- Select a sensor from the kit.

	- Connect the selected sensor to the Raspberry Pi. In this tutorial, ultrasonic sensor is used. The ultrasonic sensor is connected to **pin "3"** on the GrovePi.

6. Update the application code.

	The ZIP file contains the necessary libraries for your project and a sample Python main program. The main program is named after the thing type and stored in the main directory of the project. Open this file in your editor of choice to make the necessary adjustments.

	Code sections which can be customized for your needs are marked with

		### BEGIN SAMPLE CODE

	and

		### END SAMPLE CODE

	The following sections are of particular interest:

	* Import the GrovePi dependencies:
	
			from grovepi import *
	
	* Update the Device Configuration values like:

		* `hono_password`

		* `hono_certificatePath`

	* Define a constant and assign the pin number of the pin to which the sensor is physically connected:
	
			# constants
			ultrasonic_ranger = 3
		
	* Function `periodicAction`.

		This function is called periodically every `timePeriod` seconds. It is meant to periodically read your sensor's values and transmit them using the thing type as a data model layer and the Paho MQTT Client.
	
		Read the data and assign it to the functional block properties:
		
			infomodel.distance.sensor_value = ultrasonicRead(ultrasonic_ranger)
			infomodel.distance.sensor_units = "cm"
		
	* The variable `timePeriod` is set further down in the file. In this example, the variable `timePeriod` is set to 10.

7. Run the application on the device.

	- Copy the code to the device ([How to copy files to Raspberry Pi?](https://www.raspberrypi.org/documentation/remote-access/ssh/scp.md)).

	- Open the Terminal and navigate to the source code folder.

	- Run the application by typing the following command:
			
			python GENERATED_MAIN_PYTHON_FILE.py

8. Verify incoming sensor data.

	- Execute the following curl command to fetch the device payload data from Bosch IoT Things:
		
			curl GET ....

	- Check if the sensor data was sent successfully to the Bosch IoT Suite.

## What's next?

Learn how to easily create a Spring-boot Web application that can consume the device data from Bosch IoT Things and display the data in a UI. Click [here](dx_create_webapp_dashboard) for the tutorial.