# Creating a thing in the Bosch IoT Suite from a Vorto Information Model

This tutorial explains how to create a thing in the Bosch IoT Suite that was described as a Vorto Information Model.

As an example, the [Octopus board](http://vorto.eclipse.org/#/details/com.bosch.iot.suite:OctopusSuiteEdition:1.0.0) based on ESP 8266 is used. 


## Prerequisites

* Bosch ID User Account

* You have booked an asset communication package for the Bosch IoT Suite (refer to [Getting Started Guide](https://www.bosch-iot-suite.com/tutorials/getting-started-asset-communication/)).

* You have created a Vorto Information Model for the device (refer to [Describing a device](describe_tisensor.md)).


## Proceed as follows


1. Create an Octopus board thing instance for the Vorto Information Model in the Bosch IoT Suite device registry:

	- Login to the [Vorto Console](https://vorto.eclipse.org/console).

	- Click **Create thing**.
	
	- In the **Vorto Models** table, select the Octopus Board Information Model
	
	- Click **Next**.
	
	- Specify the **Namespace**. 
		> The namespace must match with the namespace that you have set in Bosch IoT Things for your solution.
	
	- Leave the default value for **Name**.
	
	- Set the **Technical Device ID** of the thing.

		> This can be a MAC address, a serial number or anything that uniquely identifies the device. For the example, use `47-11-00`.

	- Click **Next**.
	
	- Specify the value for the **Password**. For the example, use `s3cr3t`
	
	- Click **Next**.

	- Review the changes that are going to be made to the Suite.
	
	- Click **Create**.
	
		A new thing for the Octopus Board is created in the Bosch IoT Suite.
		
		
## What's next?

- [Generate Arduino sketch](connect_esp8266.md) that connects an ESP8266 based device to the Bosch IoT Suite.