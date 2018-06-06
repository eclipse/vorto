---
date: 2016-03-09T20:08:11+01:00
title: Payload Mapping 
weight: 102
---

The payload mapping feature in Eclipse Vorto can be used to transform arbitrary payload to standardized Vorto messages and vice versa.

<!--more-->

## Motivation

IoT Devices may not always send their data to the cloud in the structure and protocol that is required by the IoT platform that the devices are integrating with, e.g. AWS IoT, Azure IoT or Eclipse IoT. The Eclipse Vorto Mapping feature is a light-weight Java library that is able to process formally defined Vorto Mapping Specifications to transform arbitrary device payload (Binary, JSON, XML, etc) to standardized data structures complying to Vorto Function Blocks. 

An example standardized target data structure could look like this:

	{
		"temperature":{
			"properties":{
				"status":{
					"sensor_value": 25,
					"sensor_unit" : "Celcius"
				}
			}
		}
	}


However, the device might send the data in binary hex format, like that:

	{
		"data" : "0x09CF"
	}

## Vorto Payload Mapping Engine

![Vorto Mapping Engine Overview](/images/documentation/payloadmapping.png)

To get started, please take a look at the [Payload Mapping Tutorial]({{< ref "tutorials/payloadmapping.md" >}})

### Built-in Mapping Converter functions

The Vorto Mapping Engine offers a set of built-in converter functions, that you can use in your mapping rules. Please refer to the [Converter Functions](https://github.com/eclipse/vorto/blob/0.10.0.M3/server/repo/repository-mapping/docs/built_in_converters.md) documentation.

#### Example

In the following example, a [Cayenne Low Power Payload (LPP)](https://github.com/myDevicesIoT/cayenne-docs/blob/master/docs/LORA.md) is converted to a float value for the IPSO compliant Accelerometer sensor_value property:

		namespace devices.lora.cayenne.mapping
		version 1.0.0
		displayname "Accelerometer PayloadMapping"
		description "Payload Mapping for the accelerometer property of the OctopusLoRaFeatherWing"
		category payloadmapping
		
		using com.ipso.smartobjects.Accelerometer;0.0.1
		
		functionblockmapping AccelerometerPayloadMapping {
			targetplatform lora-cayenne
			from Accelerometer to functions with {convertValue:"function convertValue(value) { return value / 1000;}"}
			from Accelerometer.status.x_value to source with {xpath:"accelerometer:convertValue( endian:swapShort(conversion:byteArrayToShort(binaryString:parseHexBinary(/payloadHex),17,0,0,2)))"}
		} 

### Create a custom Javascript Converter function

#### Security

The Vorto Payload Mapping engine uses [Nashorn](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html) as a Javascript engine to execute custom converter functions.
For security reasons, the following restrictions apply when processing these converters:

* access to Java packages and classes is not possible
* using exit, quit, is not possible
* file access is not possible
* using loops are not allowed
* no JS libraries can be loaded

#### Example

In the following example, a custom (Javascript) converter is defined in a Function Block mapping, that converts a click amount as a **String** to an **Integer** value:

		namespace devices.aws.button.mapping
		version 1.0.0
		displayname "buttonPayloadMapping"
		description "Payload Mapping for the button property of the AWS IoT Button"
		category payloadmapping
		
		using com.ipso.smartobjects.Push_button;0.0.1
		
		functionblockmapping ButtonPayloadMapping {
			targetplatform aws-ipso

			// Definition of Converter functions which can be used from within the function block mapping
			from Push_button to functions with 
				{convertClickType: "function convertClickType(clickType) { if (clickType === 'SINGLE') return 1; else if (clickType === 'DOUBLE') return 2; else return -1;}"}
			
			// Usage of the converter function in the mapping rule expression
			from Push_button.status.digital_input_count to source with {xpath: "button:convertClickType(/clickType)"}
		}

### Useful Links

- [Mapping device payload to standardized Vorto Function Blocks]({{< relref "tutorials/payloadmapping.md" >}})