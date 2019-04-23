# Integrating a GrovePi Sensor to the Bosch IoT Hub using Vorto

This tutorial explains how to use Vorto to connect a GrovePi sensor to the Bosch IoT Suite using MQTT.

## Prerequisites

* [Bosch ID User Account](https://accounts.bosch-iot-suite.com)

* Subscription to [Asset Communication for Bosch IoT Suite](https://www.bosch-iot-suite.com/asset-communication/) (Free plan, no credit card required)

* You have created a thing in the Bosch IoT Suite (refer to [Creating a Thing in the Bosch IoT Suite](create_thing.md)).

## Tools

* [Python 3.x](https://www.python.org/)

* IDEs - for Python: [Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python) 

* [GrovePi Sensor Kit](https://www.dexterindustries.com/grovepi/)

## Proceed as follows

1. Setup your device.

	In this example Raspberry Pi is used but you can use any device which can run python.

	* [Install raspbian on the Raspberry Pi](https://www.raspberrypi.org/learning/software-guide/).

	* [Connect the pi to wifi](https://www.raspberrypi.org/learning/software-guide/wifi/).

	* [Enable ssh connection on your pi](https://www.raspberrypi.org/documentation/remote-access/ssh/)	.

	* [Install python and required modules](https://github.com/eclipse/vorto/blob/development/docs/tutorials/tutorial_install_python_and_required_python_modules.md).

2. Setup your development environment (on your development machine).

	* [Install Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python).

	* [Install python and required modules](https://github.com/eclipse/vorto/blob/development/docs/tutorials/tutorial_install_python_and_required_python_modules.md).

3. Generate application code using the Python generator.

	**Download the python source code template**

	- Head over to the [Vorto Repository](https://vorto.eclipse.org).

	- Navigate to your Information Model and click on it.

	- Click on the **Bosch IoT Suite** Generator.

	- At the **Integrate device with Python** template, click **Source Code**.

		<img width="800" src="../images/tutorials/create_thing/provision_device_dl.PNG">

	- Store the ZIP file and extract the source code.

4. Install Python dependencies.   
The bundled generated code contains a file called `requirements.txt`. This file describes the dependencies that still have to be installed.   
Install the dependencies using the `pip install .` command which looks at the current directory and installs the dependecies stated in this file.

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

	- The simplest way to quickly visualize your sensor data is to use the [Vorto Dashboard](create_webapp_dashboard.md).

	- Alternatively, you can also use the [SwaggerUI](https://apidocs.bosch-iot-suite.com/?urls.primaryName=Bosch%20IoT%20Things%20-%20API%20v2) to get a quick insight into whether your data is updating,

	- Once you can see your data updating, you have successfully connected your ESP8266 device to the Bosch IoT Suite!

## What's next ?

 - [Generate a Vorto Dashboard](create_webapp_dashboard.md) that visualizes the device data in UI widgets.
