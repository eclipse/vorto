# Generator for the Alpwise Bluetooth LE Stack

This code generator can be used to generate a Vorto interface for the [Alpwise](http://www.alpwise.com/) [Bluetooth Low-Energy Stack](http://www.alpwise.com/bluetooth-protocol-stack/software/bluetooth-stack/bluetooth-5-stack/) as used in the [Bosch XDK](http://xdk.io).

The generator requires mapping models for the targetplatform 'blegatt' to be present for the function blocks referenced by the information model.

```
...

functionblockmapping VirtualXdkHumidity {
	targetplatform blegatt

...
```

The function block needs to be mapped to a stereotype Service with a required parameter 'uuid'. The name of the service is inferred from the name of the functionblock, but can be overwritten by setting the serviceName attribute to the desired name. Setting the serviceName attribute is recommended if the service is referenced by multiple functionblocks. It should then be set for all functionblocks referencing the same uuid and service in order to ensure deterministic behavior of the generator.

```
...
	from Humidity to Service with {
		uuid: "92dab060-7634-11e4-82f8-0800200c9a66",
		serviceName: "Environment"
	}
...
```

The properties of the functionblock are usually mapped to one BLE characteristic. It is possible to map multiple properties to the same characteristic, similar to mapping multiple functionblocks to the same service as indicated above. Functionblocks have to be mapped to a stereotype named 'source'. The attribute 'uuid' represents the UUID of the Bluetooth characteristic, whereas 'offset' and 'length' indicate the position of the property within the characteristic's data. The attribute 'datatype' represents the datatype as used in the programming environment of the Bluetooth stack. E.g. below the property 'sensor_value' below is a a 'uint32' stored in bytes 0 to 3 of the characteristic.  

```
...
	from Humidity.status.sensor_value to source with {
		uuid: "92dab063-7634-11e4-82f8-0800200c9a66",
		offset: "0",
		length: "4",
		datatype: "uint32",
	}
...
```

The code generator was built for and tested with the XDK Workbench version 3.0.1. The generated code should work with the Alpwise Bluetooth Stack for different devices, however the user will be responsible to make sure the stack is correctly initialized, started and event handling is periodically triggered. 

----------

List of other available [Code Generators](../Readme.md).
