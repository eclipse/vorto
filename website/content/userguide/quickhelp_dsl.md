---
menu:
  main:
    parent: 'User Guide'
date: 2016-03-09T20:08:11+01:00
title: Vorto DSL Help
weight: 101
---
This section gets you started with using the Vorto DSL to describe devices as well as use the Mapping DSL to map device descriptions to spec platform models, e.g. LwM2M or even IoT Digital Twin Services.
<!--more-->

## Overview

<figure class="screenshot">
	<img src="/images/documentation/help_dsl_elements.png">
</figure> 

### 1. Information Models

Information Models describe a specific device type by grouping abstract and re-usable Function Blocks.

#### Example:

	namespace com.mycompany
	version 1.0.0
	description "Information model for FabLab.eu's IoT Octopus"

	using com.ipso.smartobjects.Accelerometer ; 1.1.0
	using com.ipso.smartobjects.Barometer ; 1.1.0

	infomodel IoTOctopus {

		functionblocks {
			mandatory accelerometer as Accelerometer
			mandatory barometer as Barometer
		}
	}
    
[Complete DSL Reference]({{< ref "appendix/infomodel_dsl.md" >}}) for Information Models

### 2. Function Blocks

A Function Block provides an abstract view on a device to applications that want to employ the devices’ functionality. Thus, it is a consistent, self-contained set of (potentially re-usable) properties and capabilities.
The properties that a Function Block may define are classified as follows:

* **Configuration** Properties - _Read- and Writable_ properties that can be set on the device.
* **Status** Properties - These _read-only_ properties indicating the current state of the device.
* **Fault** Properties - _Read-only_ properties indicating fault states of the device.
* **Event** Properties - _Read-only_ properties that are published by the device, e.g. on state changes
* **Operations** - Indicate functionality that can be invoked on the device, that may lead to device state changes or give additional meta-data information.

#### Example 1:

	namespace com.mycompany.common
	version 1.0.0

	functionblock Accelerometer {

		status {
			mandatory xValue as float "The measured value along the X axis."
			optional yValue as float "The measured value along the Y axis."
			optional zValue as float "The measured value along the Z axis."
		}
	}

#### Example 2 (using inheritance):

	namespace com.mycompany.tisensortag 
	version 1.1
	using com.mycompany.common.Accelerometer; 1.0.0
	using com.mycompany.common.types.EnableDisable; 1.0.0

	functionblock Accelerometer extends com.mycompany.common.Accelerometer {

		configuration { 
			mandatory sensor_enable as EnableDisable "Configuration to enable and disable the sensor"
			mandatory sensor_period as int "Sampling period of the sensor"
		}

		status {
			extension xValue as float <MIN -250, MAX 250>
			extension yValue as float <MIN -250, MAX 250>
			extension zValue as float <MIN -250, MAX 250>
		}

	}
    
[Complete DSL Reference]({{< ref "appendix/function_block_dsl.md" >}}) for Function Blocks


### 3. Datatypes (Entity and Enums)

Data types and Enums are reusable components that describe specific data and are typically referenced by Function Blocks. Data types can reference other data types and Enums.

#### Basic Enum Example:

	namespace com.mycompany.common.types
	version 1.0.0

	enum EnableDisable {
		enabled, 
		disabled
	}
    
#### Basic Entity Example:

	namespace com.mycompany.common.types
	version 1.0.0

	entity Color {
		mandatory red as int <MIN 0, MAX 255>
		mandatory green as int <MIN 0, MAX 255>
		mandatory blue as int <MIN 0, MAX 255>
	}

[Complete DSL Reference]({{< ref "appendix/datatype_dsl.md" >}}) for Datatypes


### 4. Mappings

Mappings enrich Vorto Models with platform-specific meta-data, e.g. LwM2M, Bluetooth GATT etc.

Mappings are defined in the following pattern: 

**from** [*Vorto Model Path*] **to** [*stereotype*] or **reference** **with** {attributes[key,value]}

#### Basic Example

Example Mapping that maps a function block to a specific LwM2M Object:

	namespace com.mycompany.mapping.lwm2m
	version 1.0.0
	using com.mycompany.common.Accelerometer;1.0.0

	functionblockmapping AccelerometerMapping {
		targetplatform lwm2m

	 	from Accelerometer to Object with {ObjectID: "3313", ObjectURL: "urn:oma:lwm2m:ext:3313"}

	}

#### Example Mapping referencing another mapping

The following example defines a mapping that references another mapping for re-use:

	namespace com.mycompany.mapping.lwm2m
	version 1.0.0
	using com.mycompany.tisensortag.Accelerometer;1.0.0
	using com.mycompany.mapping.lwm2m.EnableMapping;1.0.0

	functionblockmapping AccelerometerMapping {
		targetplatform lwm2m

	 	from Accelerometer.configuration.sensor_enable to reference EnableMapping

	}

[Complete DSL Reference]({{< ref "appendix/mappings_dsl.md" >}}) for Mapping Models

### 4.1 Device Payload Mappings

Device Payload Mappings use Vorto Mappings expressing mapping rules in order to map arbitrary device data to Vorto Models.

For more information about payload mappings, please read the [Payload Mapping Documentation]({{< ref "userguide/mappingengine.md" >}})

