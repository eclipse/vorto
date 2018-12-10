---
title: "Getting Started"
date: 2018-05-09T10:58:37+08:00
weight: 20
---

Vorto provides a simple language to describe the capabilities and functionality of devices as Information Models. But how can these models be consumed to help IoT solution development ? 

I think, it will get clearer, if we walk you through an easy example. In this example we are going to describe a TI Sensor Tag device using abstract Vorto Function Blocks which on the other hand define specific piece of functionality and data. You can translate Function Blocks of the device to source code that may run on the device and integrate it with the [Eclipse Ditto](https://www.eclipse.org/ditto) Digital Twin service using a MQTT Connector, provided by the [Eclipse Hono](https://www.eclipse.org/hono) platform.

An IoT Application (which is not part of this tutorial) then consumes the device data from Eclipse Ditto and process the humidity data. In fact the IoT Application does not make any assumption of whether this humidity data originates from a TI SensorTag or any other device or service, capable of providing humidity data, as long as it is described as a Humidity Vorto Function Block. The following illustration shows the entire components and data flow:  

![Material Screenshot](/images/getting-started-ar2.png)

So, let's get started with this, by splitting up our work into several steps:

1. Describe the TI Sensor Tag as an Information Model using re-usable, abstract Function Blocks
2. Generating Code from Model, integrating the TI Sensor Tag with Eclipse Hono & Eclipse Ditto
3. Test the integration with Eclipse Hono


## Step 1: Creating TI Sensor Tag Information Model

[Click here]({{< relref "tutorials/tisensor.md" >}}) that walks you through the process of creating an Information Model.

## Step 2: Generating Device Code

1. Click on one of the Generators, here Eclipse Hono.
	<figure class="screenshot">
  	<img src="/images/tutorials/getting_started/create_function_block_generator.png">
	</figure>
2. Choose **Java** and click **Generate Code**. This will generate a Java source code bundle and will be downloaded automatically.
	<figure class="screenshot">
  	<img src="/images/tutorials/getting_started/create_function_block_generator_hono.png">
	</figure> 

## Test the Integration with Eclipse Hono

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

### Editing device configuration

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

### Running and verifying data in Hono

- Right click on `Distancesensor.java` in you IDE and **Run as Java Application`** to start sending data to Eclipse Hono. You should be seeing the following output in the console:

![Material Screenshot](/images/run_java.PNG)


- Follow [Consuming Messages from Java for Hono] (https://www.eclipse.org/hono/dev-guide/java_client_consumer/) to receive the device data in the Eclipse Hono backend.

## What's next? 

- **Forward** the device data to [Eclipse Ditto](https://ditto.eclipse.org) in order to update the digital twin representation for the device. 

	> The JSON data, sent by the device is already compliant to the Eclipse Ditto protocol. Therefore, you can directly modify the thing in Eclipse Ditto by sending the device JSON data via Websockets. Please take a look at the [Eclipse Ditto Sandbox](https://ditto.eclipse.org) for more information. 
	
- [Connect an ESP8266 - based device to Eclipse Hono](../tutorials/#connecting-a-esp8266-arduino)
- [Connect a GrovePi to Eclipse Hono](../tutorials/#connecting-a-grovepi)
