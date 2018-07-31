---
date: 2016-03-09T20:08:11+01:00
title: Vorto DSL Help
weight: 109
---
This section gets you started with using the Vorto DSL to describe devices.
<!--more-->

## Overview

<figure class="screenshot">
	<img src="/images/documentation/help_dsl_elements.png">
</figure> 

### Information Models

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
    
[Complete DSL Reference]({{< ref "documentation/infomodel_dsl.md" >}}) for Information Models

### Function Blocks

A Function Block provides an abstract view on a device to applications that want to employ the devices’ functionality. Thus, it is a consistent, self-contained set of (potentially re-usable) properties and capabilities.
The properties that a Function Block may define are classified as follows:

* **Status** Properties - These _read-only_ properties indicating the current state of the device. 
* **Configuration** Properties - _Read- and Writable_ properties that can be set on the device.
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

	functionblock TIAccelerometer extends Accelerometer {

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
    
[Complete DSL Reference]({{< ref "documentation/function_block_dsl.md" >}}) for Function Blocks


### Datatypes (Entity and Enums)

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

[Complete DSL Reference]({{< ref "documentation/datatype_dsl.md" >}}) for Datatypes