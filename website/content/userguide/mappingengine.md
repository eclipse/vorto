---
menu:
  main:
    parent: 'User Guide'
date: 2016-03-09T20:08:11+01:00
title: Payload Mapping 
weight: 104
---

The payload mapping feature in Eclipse Vorto can be used to transform arbitrary payload to standardized Vorto messages and vice versa.

<!--more-->

## Motivation

IoT Devices may not always send their data to the cloud in the structure and protocol that is required by the IoT platform that the devices are integrating with, e.g. AWS IoT, Azure IoT or Eclipse IoT. The Eclipse Vorto Mapping feature is a light-weight Java library that is able to process Vorto Mapping Specifications to transform arbitrary device payload (Binary, JSON, XML, etc) to standardized data structures complying to Vorto Function Blocks. 

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

The Vorto Payload Mapping Engine is a very easy and light-weight component to convert device payload in different formats to a normalized and standardized Vorto Function Block representation which can then be converted further to specific IoT platform data structures. 

![Vorto Mapping Engine Overview](/images/documentation/payloadmapping.png)

It supports Eclipse Ditto as the target platform mapping out-of-the-box. A Data Mapping API is available to implement the integration with other IoT platforms.

[Get started](https://github.com/eclipse/vorto/blob/development/mapping-engine/Readme.md) of using the Vorto Payload Mapping Engine in a concrete example. 


### Built-in Mapping Converter functions

If you need to do value conversions from the device payload to the function block property value, the Vorto Mapping Engine offers a set of available built-in converter functions to choose from.

Here is an example that converts a string value to a float: 

	from Accelerometer.status.x_value to source with {xpath:"number:toFloat(/stringproperty)"}

Please refer to the complete [Converter Functions documentation](https://github.com/eclipse/vorto/blob/0.10.0.M3/server/repo/repository-mapping/docs/built_in_converters.md) for more information.

#### Example

In the following example, a [Cayenne Low Power Payload (LPP)](https://github.com/myDevicesIoT/cayenne-docs/blob/master/docs/LORA.md) is converted to a float value for the IPSO compliant Accelerometer sensor_value property:

		namespace devices.lora.cayenne.mapping
		version 1.0.0
		displayname "Accelerometer PayloadMapping"
		description "Payload Mapping for the accelerometer property of the OctopusLoRaFeatherWing"
		category payloadmapping
		
		using com.ipso.smartobjects.Accelerometer;0.0.1
		
		functionblockmapping AccelerometerPayloadMapping {
			targetplatform lora_cayenne
			from Accelerometer to functions with {convertValue:"function convertValue(value) { return value / 1000;}"}
			from Accelerometer.status.x_value to source with {xpath:"accelerometer:convertValue( endian:swapShort(conversion:byteArrayToShort(binaryString:parseHexBinary(/payloadHex),17,0,0,2)))"}
		} 

### Create a custom Javascript Converter function

The Vorto Mapping engine uses [Nashorn](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html) as a Javascript engine to execute custom converter functions.

#### Security

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
			targetplatform aws_ipso

			// Definition of Converter functions which can be used from within the function block mapping
			from Push_button to functions with 
				{convertClickType: "function convertClickType(clickType) { if (clickType === 'SINGLE') return 1; else if (clickType === 'DOUBLE') return 2; else return -1;}"}
			
			// Usage of the converter function in the mapping rule expression
			from Push_button.status.digital_input_count to source with {xpath: "button:convertClickType(/clickType)"}
		}


### Mapping Conditions

If you want to specify a condition, when mapping rules should be applied, you can do this easily with mapping conditions.

Here is an example of using conditions to map to either temperature or illuminance based on the device payload header:

Function Block Temperature Mapping

	...
	from Temperature.status.sensorValue to source with {xpath:"/value", condition:"xpath:eval('/header/type', this) == 'T'"}

Function Block Illuminance Mapping

	...
	from Illuminance.status.sensorValue to source with {xpath:"/value", condition:"xpath:eval('/header/type', this) == 'I'"}

### Useful Links

- [Getting Started with Vorto Mappings](https://github.com/eclipse/vorto/blob/development/mapping-engine/Readme.md)
