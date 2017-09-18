# Connect a device via MQTT with the Vorto Python Generator

In this tutorial we are going to show you how to connect a device to Bosch IoT Suite using MQTT Python Generator. MQTT Python Generator generator allows for easy implementation of a device sending telemetry data to an MQTT broker based on Eclipse Paho for Python.

## Pre-requisites

* [Publish](tutorial-create_and_publish_with_web_editor.md) an information model for the device to the Vorto Repository.  
* [Request](https://www.bosch-iot-suite.com/) an evaluation account for the Bosch IoT Suite.  
* [Register](tutorial_register_device.md) the device in the Bosch IoT Suite.

## Tools

* [Python 3.x](https://www.python.org/)
* IDEs
	* For Python [Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python) 
	* For Vorto code generator [Eclipse IDE](https://eclipse.org/downloads/packages/release/Mars/2)
	* 
* [Paho Python Client](https://eclipse.org/paho/clients/python/)
* [Vorto Web Development Tool](http://vorto.eclipse.org/editor)
* [Vorto Repository](http://vorto.eclipse.org/)

## Steps

### 1. Setup device
We will be using raspberry pi as our device and you can use any device which can run python.

* [Install raspbian on the raspberry pi](https://www.raspberrypi.org/learning/software-guide/)
* [Connect the pi to wifi](https://www.raspberrypi.org/learning/software-guide/wifi/)
* [Enable ssh connection on your pi](https://www.raspberrypi.org/documentation/remote-access/ssh/)	
* [Install python and required modules](tutorial_install_python_and_required_python_modules.md)

### 2. Set up your development environment

Now we have setup the device, next step is to setup the development environment on your development machine. 

* [Install Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python) 
* [Install python and required modules](tutorial_install_python_and_required_python_modules.md)

### 3. Generate code using Vorto Python generator

In this step you are going to generate the application code using the python generator. Assuming you have published an infromation model already to the vorto repository prior to this step (*see the pre-prequisite section*). There are two ways to generate code:

* **Run the generator from the Eclipse IDE**
	1. Download and install the [Eclipse IDE for Java and DSL Developers]		(http://www.eclipse.org/downloads/packages/)
	2. Start the Eclipse IDE and install the Vorto Toolset
	
		2.a. [Download the package](http://www.eclipse.org/vorto/downloads/index.html) by 		     dragging the Install button onto the running IDE or
	
		2.b. By choosing the menu Help -> Eclipse Marketplace... and finding the Vorto 			  Toolset in the dialog. Click Install to install the Vorto Toolset in the 			  corresponding 	list entry.
	3. Choose the Vorto Perspective and search for the desired Information Model for your 		project. 

	4. To generate the code for your implementation right-click on your information model and select the Python MQTT Generator
	
		1. Choose the directory where you want to have your generated code reside 

* **Invoke the generator from the Repository directly**

	1. Open the [vorto repository](http://vorto.eclipse.org/) in a browser
	2. Navigate to your model using the search functionality of the repository and look for 	   the Python MQTT Generator in the panel on the right hand side of the screen. Click on 	   the generate button to generate code. 
	3. Store the ZIP file which will be downloaded and extract the source code.

### 4. Write the application
Vorto generates the necessary libraries for your project and a sample main program. The main Python program is named after the information model and stored in the main directory of the project. Open this file in your editor of choice to make the necessary adjustments.
Code sections which you'll need to adapt are marked with 

```### BEGIN SAMPLE CODE``` and ```### END SAMPLE CODE```.

Of particular interest are the following sections:

* Function periodicAction: this function is called periodically every timePeriod seconds. It is meant to periodically read your sensor's values and transmit them using the Vorto information model as a data model layer and the Paho MQTT Client. The variable timePeriod is set further down in the file.
* The function periodicAction is followed by the initialization code for the Vorto data model. You may initialize to any value you deem correct. By default all values are initialized to zero.
* For secure connectivity, you need to set the relevants paths to your server's certificate, your client's certificate and key files. Detailed documentation can be found on the [Paho MQTT website](https://pypi.python.org/pypi/paho-mqtt/1.1#option-functions).
* Set the URL of the endpoint in the call to client.connect as the first argument.


### 6. Run the application in the device

Now you have written the necessary application code. So you can run this on your device directly. 

1. Copy the code on to the device ([How to copy files to raspberry pi?](https://www.raspberrypi.org/documentation/remote-access/ssh/scp.md))
2. Open the Terminal and navigate to the source code folder
3. Run the application by typing the following command
		
		python GENERATED_MAIN_PYTHON_FILE.py 

### 7. Verify incoming sensor data

To check if the sensor data was sent successfully to the cloud, just open the <a href="https://console.bosch-iot-suite.com">Bosch IoT Developer Console</a> and navigate to the thing in the thing browser.

## What's next ?

- [How to add GrovePi sensors to raspberry pi and send data to Bosch IoT Suite via MQTT?](tutorial_how_to_add_groove_pi_sensors_to_raspberry_pi_and_send_data_via_MQTT.md)
- [Create a web application consuming the device telemetry data](tutorial_create_webapp_dashboard.md)
- [Build an Amazon Alexa Skillset to voice-control the device](tutorial_voicecontrol_alexa.md)


