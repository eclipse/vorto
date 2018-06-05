---
title: "Getting Started"
date: 2018-05-09T10:58:37+08:00
weight: 20
---
To get started with Vorto easily, we are going to show you how you can use the Vorto IoT Tool to describe a simple device that measures distance and integrate it with [Eclipse Hono](https://www.eclipse.org/hono).

![Material Screenshot](/images/getting-started-ar2.png)


## Prerequisites
* Maven
* Java 8
* [Eclipse for DSL Developers](https://www.eclipse.org/downloads/packages/eclipse-ide-java-and-dsl-developers/oxygen3a)
* [Eclipse Vorto Toolset Plugins](http://marketplace.eclipse.org/content/vorto-toolset)

## Creating a Vorto Project
1. Open Eclipse and switch to the **Vorto Perspective**.
2. In the Vorto Model Project Browser, click the **+** button. 
3. This opens the **New Vorto Project** Dialog:
![Material Screenshot](/images/tutorials/getting_started/vorto_create_new_vorto_project_dialog.png)
4. Enter a **Project Name**, for example, `MyVortoProject`.
5. Click on **Finish** to begin describing the device.

![Material Screenshot](/images/tutorials/getting_started/vorto_new_vorto_project_created.png)

## Defining a Function Block
> A function block provides an abstract view on a device to applications that want to employ the devices’ functionality. Thus, it is a consistent, self-contained set of (potentially re-usable) properties and capabilities.

1. Right-click in the **Functionblocks** area and choose **New Functionblock** from the context menu.
![Material Screenshot](/images/tutorials/getting_started/m2m_tc_create_function_block_designer_dialog_2.png)
2. Enter a name as **Function Block Name**, for example, Distance
3. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
4. Optionally, enter a description in the **Description** entry field.
5. Click **Finish**. ![Material Screenshot](/images/tutorials/getting_started/m2m_tc_create_function_block_generated_source_1.png)
6. Edit the function block by adding the distance specific properties:

**Example:**

```sh
namespace org.eclipse.vorto.fb
version 1.0.0
displayname "Distance"
description "Function block model for Distance"
functionblock Distance {

	status {
		mandatory distance as float
		mandatory sensor_unit as string
		optional min_value as float
		optional max_value as float
	}
}
```
## Defining an Information Model
> Information models represent the capabilities of a particular type of device entirety. An information model contains one or more function blocks.

1. Right-click in the **Information Models** area and choose **New Information Model** from the context menu.
![Material Screenshot](/images/tutorials/getting_started/m2m_tc_create_information_model_dialog.png)
2. Enter a name as **Information Model Name**, for example, DistanceSensor.
3. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
4. Optionally, enter a description in the **Description** entry field.
5. Click **Finish**.![Material Screenshot](/images/tutorials/getting_started/m2m_tc_information_model_dsl_editor.png)
6. Drag and drop the created and edited function block (distance) from the **Functionblocks** area onto the information model **DistanceSensor** to create the reference. ![Material Screenshot](/images/tutorials/getting_started/m2m_tc_drag_drop_function_block_to_information_model.png)


## Generating Device Code
- Right click on the Information Model **DistanceSensor** in the Information Model area
- Click **Generate Code** -> **Eclipse Hono Generator**
- Choose **Java** and click **Generate**. This will generate a Java source code bundle and switch to the Java perspective automatically. 
- Right click on the generated Maven Project and Choose **Maven** -> **Update Project**. This downloads all the needed Maven dependencies for the project to run.

## Registering device in Eclipse Hono
Eclipse Hono provides a sandbox infrastructure which we can use to demonstrate the device integration. Before we can send data from our generated project, we must register the device under a tenant:

### Registering the Device ID

Execute the following curl command to register the device in Eclipse Hono sandbox. We are using ```1234``` as a **device ID** and ```vortodemo``` as a **Hono tenant**, but feel free to change as you like: 

```sh
curl -X POST http://hono.eclipse.org:28080/registration/vortodemo
-i -H 'Content-Type: application/json'
-d '{"device-id": "1234"}'
```

### Adding device credentials

Execute the following curl command in order to set the device credentials in Eclipse Hono:

```sh
PWD_HASH=$(echo -n "S3cr3t" | openssl dgst -binary -sha512 | base64 | tr -d '\n') 
curl -X POST http://hono.eclipse.org:28080/credentials/vortodemo
-i -H 'Content-Type: application/json'
-d '<JSON Request payload>' 
```

JSON Request payload:
```sh
{
  "device-id": "1234",
  "auth-id": "1234",
  "type": "hashed-password",
  "secrets": [
    {
      "hash-function": "sha-512",
      "pwd-hash": "'$PWD_HASH'"
    }
  ]
}
```

## Editing device configuration

Now let's go back to our generated project and update the `src/main/java/device/distancesensor/Distancesensor.java` accordingly:

	// Hono MQTT Endpoint
	private static final String MQTT_ENDPOINT = "ssl://hono.eclipse.org:8883";

	// Your Tenant
	private static final String HONO_TENANT = "vortodemo";

	// Your DeviceId
	private static final String DEVICE_ID = "1234";
	
	// Device authentication ID
	private static final String AUTH_ID = "1234@"+HONO_TENANT;
	
	// Ditto Namespace
	private static final String DITTO_NAMESPACE = "org.eclipse.vorto";

	// Device authentication Password
	private static final String PASSWORD = "S3cr3t";

**Download Certificate**

> Copy and paste the [certificate](https://letsencrypt.org/certs/lets-encrypt-x3-cross-signed.pem.txt) into `src/main/resources/certificate/hono.crt` of your project.

## Running and verifying data in Hono

- Right click on `Distancesensor.java` in you IDE and **Run as Java Application`** to start sending data to Eclipse Hono. You should be seeing the following output in the console:

![Material Screenshot](/images/run_java.PNG)


- Follow [Consuming Messages from Java for Hono] (https://www.eclipse.org/hono/dev-guide/java_client_consumer/) to receive the device data in the Eclipse Hono backend.

## What's next? 

- **Forward** the device data to [Eclipse Ditto](https://ditto.eclipse.org) in order to update the digital twin representation for the device. 

	> The JSON data, sent by the device is already compliant to the Eclipse Ditto protocol. Therefore, you can directly modify the thing in Eclipse Ditto by sending the device JSON data via Websockets. Please take a look at the [Eclipse Ditto Sandbox](https://ditto.eclipse.org) for more information. 
	
- **Share** your Vorto Information Model in the [Vorto Repository](http://vorto.eclipse.org), that way other IoT developers can integrate your device in their IoT solutions.

	> Log in to [Vorto Repository](http://vorto.eclipse.org) with your Github account and click on **Share** and follow the instructions. 
	
- [Connect an ESP8266 - based device to Eclipse Hono](../tutorials/#connecting-a-esp8266-arduino)
- [Connect a GrovePi to Eclipse Hono](../tutorials/#connecting-a-grovepi)