---
title: "Getting Started"
date: 2018-05-09T10:58:37+08:00
weight: 20
---
To get started with Vorto easily, we are going to show you how you can use the Vorto IoT Tool to describe a simple device that measures distance and integrate it with [Eclipse Hono](https://www.eclipse.org/hono).

![Material Screenshot](/images/getting-started-ar2.png)


## Different types of Models
### Infomation Model

Information Models represent the capabilities of a particular type of device in its entirety. An Information Model is assembled from abstract and re-usable Function Blocks.

### Function Block

A Function Block provides an abstract view on a device to applications that want to employ the devices’ functionality. Thus, it is a consistent, self-contained set of (potentially re-usable) properties and capabilities.
The properties that a Function Block may define are classified as follows:

* **Status** Properties - These _read-only_ properties indicating the current state of the device. 
* **Configuration** Properties - _Read- and Writable_ properties required to be set in order for the device to work properly.
* **Fault** Properties - _Read-only_ properties indicating fault states of the device.
* **Event** Properties - _Read-only_ properties that are published by the device, e.g. on state changes
* **Operations** - Indicate functionality that can be invoked on the device, that may lead to device state changes or merely give additional meta-data information.

[Get more information]({{< relref "userguide/quickhelp_dsl.md" >}}) on the Vorto Meta-Model and DSL.

## Defining a Model

1. Click the **Create new Model** Button and choose the type you want to create from the context menu.
	<figure class="screenshot">
	<img src="/images/tutorials/getting_started/create_function_block_designer_btn.png">
	</figure> 
2. Enter a name, for example, Distance as a **Function Block**.
3. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
4. Optionally, enter a description in the **Description** entry field.
5. Click **Create**.
	<figure class="screenshot">
  	<img src="/images/tutorials/getting_started/create_function_block_designer_name.png">
	</figure>
6. In the Model Editor add the distance specific properties and click **Save**:
	<figure class="screenshot">
  	<img src="/images/tutorials/getting_started/create_function_block_editor.png">
	</figure>


## Generating Device Code
1. Click on one of the Generators, here Eclipse Hono.
	<figure class="screenshot">
  	<img src="/images/tutorials/getting_started/create_function_block_generator.png">
	</figure>
2. Choose **Java** and click **Generate Code**. This will generate a Java source code bundle and will be downloaded automatically.
	<figure class="screenshot">
  	<img src="/images/tutorials/getting_started/create_function_block_generator_hono.png">
	</figure> 

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
	
- [Connect an ESP8266 - based device to Eclipse Hono](../tutorials/#connecting-a-esp8266-arduino)
- [Connect a GrovePi to Eclipse Hono](../tutorials/#connecting-a-grovepi)
