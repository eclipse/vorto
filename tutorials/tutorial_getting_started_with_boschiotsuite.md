# Getting started with Vorto and the Bosch IoT Suite

Vorto provides a variety of existing Bosch IoT Suite generators, for you to get started very easily. In this guide, we are going to show you:

1. Create a very simple Information Model, describing a few properties of the device and publish the model to the Vorto Repository
2. Register a specific device by creating a new thing in Bosch IoT Things Service
3. Generate a Python Code that sends telemetry device data to the thing in the Bosch IoT Suite via MQTT
4. Generate an AngularJS / SpringBoot Java Application that consumes thing data from Bosch IoT Things and displays the data in a User Interface

## Prerequisite

- [Request](https://www.bosch-iot-suite.com/) an evaluation account for the Bosch IoT Suite.  

## Steps

### 1. Create a simple Information Model for a device

- Open the [Bosch IoT Suite Developer Console](https://console.bosch-iot-suite.com) and log in with the credentials from your evaluation confirmation email.
- Select **Connect device** in the home screen
- Select **Create a new thing type** in the first wizard page
- Define a 
	- **namespace**, e.g. 'demo'
	- **name** for the thing type, e.g. SensorXYZ
	- Choose the properties **Accelerometer** and **Temperature** from the multi-select box
	- Confirm with **Create** that takes you back to the Thing Type Selections

**Great!** You have just created a Vorto Information Model for your device. If you want to create more sophisticated models, feel free to use the [Model Builder](http://vorto.eclipse.org/editor).

### 2. Register device in the Bosch IoT Suite

- [Register](tutorial_register_device.md) the device for your Information Model in the Bosch IoT Suite.

### 3. Generate Python Code for MQTT 

- Go to the [Vorto Repository](http://vorto.eclipse.org)
- Filter for the information model from Step 1 and click for details
- In the details page, choose **Python MQTT** from the list of **Generators**
- Confirm with **Generate**. This will generate python code for a device that sends data to the Bosch IoT Suite via MQTT
- Read the [Python MQTT Tutorial](tutorial_connect_device_using_mqtt_python.md) to learn how to configure and run the generated code.

Once you are sending data to the Suite, you can verify the telemetry data in the [Bosch IoT Developer Console](https://console.bosch-iot-suite). For that just browse to the thing you had created in Step 2.

### 4. Consume the data in a AngularJS/ Java Web Application

Once the data is in the cloud, you can use the same Vorto Information Model and generate a full-fledged SpringBoot / AngularJS App that is able to receive the telemetry data and display them in a User Interface.

For more info, please visit the [Web Application Tutorial](tutorial_create_webapp_dashboard).

**Congratulations**. With just a single Vorto Information Model, you can connect a device and consume the device data in an application. 
Feel free to check out the other tutorials from the [Tutorial Section](Readme.md)
