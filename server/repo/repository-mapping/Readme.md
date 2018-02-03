# Vorto Data Mapping API

The Data Mapping API allows to map arbitrary JSON device data to platform - specific data structure according to Vorto Information Models.  


### Maven dependency

```
<dependency>
   <groupId>org.eclipse.vorto</groupId>
   <artifactId>repository-mapping</artifactId>
   <version>0.10.0.M2</version>
</dependency>

```

# Map JSON to Eclipse Ditto format

### What you need:

- [Eclipse Vorto IoT Toolset Plugins](https://marketplace.eclipse.org/content/vorto-toolset)



The following steps describe how to map device-specific JSON data to [Eclipse Ditto](https://projects.eclipse.org/proposals/eclipse-ditto) JSON format using Vorto Mappings. 

Let's look at an example by mapping the JSON data sent by an AWS IoT Button to a  data format that can be sent to Eclipse Ditto.

### Step 1: Create and Publish an Information Model for the device

First, create an information model for the AWS IoT button by leveraging existing functionblocks:

- [AWS IoT Button Information Model](http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0)
	- [Push Button Functionblock](http://vorto.eclipse.org/#/details/com.ipso.smartobjects/Push_button/0.0.1)
	- [Voltage Functionblock](http://vorto.eclipse.org/#/details/com.ipso.smartobjects/Voltage/0.0.1)

### Step 2: Create a Mapping Specification 

Now, let's describe how the function blocks map to the JSON data, sent by the device. The result is a Mapping specification that is published to the Vorto Repository.

The [AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0) mapping specification is defined as XPath expressions. The mappings are target platform agnostic and merely map specific device properties to Vorto functionblock properties.
Use the Eclipse Vorto IoT Toolset to create the specification and publish them via the Vorto Repository web interface.


- AWS IoT Button Mapping Specification
	- [Button Mapping Specification](http://vorto.eclipse.org/#/details/devices.aws.button/ButtonPayloadMapping/1.0.0)
	- [Voltage Mapping Specification](http://vorto.eclipse.org/#/details/devices.aws.button/PayloadVoltageMapping/1.0.0)

Looking at the Button Mapping specification more closely, have might have spotted some javascript function that is declared as part of the mapping. This is a very powerful way of expressing custom converter functions as javascript that can be used from within your mapping. 

Excerpt of the Button Mapping:

```
... to functions with {
		convertClickType: "function convertClickType(clickType) { conversion code } "
	}

... to source with {xpath: button:convertClickType(xpathToValue)}
```

Here we declare the convertClickType function that converts the JSON clickType property value , e.g. 'DOUBLE' to an Integer value. That function is then used by the functionblock property mapping. 

The Vorto Mapping Engine already provides some standard functions that you can use in your mapping specification to do e.g. string manipulations or type conversions. For more info, click [here](docs/built_in_converters.md)

### Step 3: Use Vorto Mapping Engine to map data 

Last but not least, you can use the Mapping Engine that takes the device JSON data as input and outputs the Eclipse Ditto format:

```
DataMapperBuilder builder = IDataMapper.newBuilder();
// add Loader to load Information Model and AWS IoT Button mappings from the Vorto Repository
builder.withSpecification(IMappingSpecification.newBuilder().modelId("devices.aws.button.AWSIoTButton:1.0.0").key("awsiotbutton").build());

IDataMapper<DittoData> mapper = builder.buildDittoMapper();
									
String deviceJSON = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(deviceJSON),MappingContext.empty());

Feature voltageFeature = mappedDittoOutput.getFeatures().get("batteryVoltage");
		
assertEquals(2322f,voltageFeature.getProperty("sensor_value"));
assertEquals("mV",voltageFeature.getProperty("sensor_units"));

// Serialize mapped Eclipse Ditto format to JSON
System.out.println(mappedDittoOutput.toJson());

```

Mapped Eclipse Ditto JSON Output:

```
{
	"button": { 
		"properties":{ 
			"status: {
				"digital_input_count":2,
				"digital_input_state":true
			}
		}
	},
	"batteryVoltage":{
		"properties":{
			"status: {
				"sensor_units":"mV",
				"sensor_value":2322.0
			}
	 	}
	}
}

```

# Map data from Bluetooth Low Energy (BLE) GATT Profiles to Eclipse Ditto format

### What you need:

- [Eclipse Vorto IoT Toolset Plugins](https://marketplace.eclipse.org/content/vorto-toolset)

The following steps describe how to map binary data read from a BLE device to [Eclipse Ditto](https://projects.eclipse.org/proposals/eclipse-ditto) JSON format using Vorto Mappings. This type of mapping would typically be performed on a Bluetooth gateway which connects to the Internet, however is by no means limited to that use-case.

BLE GATT devices are structured in a very similar way as Vorto Information Models. At the top-level there is the notion of a Device. A Device consists of multiple Services, each one identified by a universally unique identifier (UUID). A service represent some functionality of the device in the same way that a Vorto Function Block represents such a functionality. Services consists of one or more Characteristic. A Characteristic is also identified by a UUID and has attributes such as readable, writeable or whether the user can be notified about changing values. Notifications are described in Vorto as eventable.

To create a mapping from a BLE device to a Vorto model follow the steps below.

### Step 1: Create and Publish an Information Model for the device

First, create an information model for your device by leveraging as much as possible existing functionblocks:

- [TiSensorTag Information Model](http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0)
    - [Barometer Functionblock](http://vorto.eclipse.org/#/details/org.omi.fb/Barometer/1.0.1)
    - [Humidity](http://vorto.eclipse.org/#/details/org.omi.fb/Humidity/1.0.0)
    - [Accelerometer Functionblock](http://vorto.eclipse.org/#/details/org.omi.fb/Accelerometer/1.0.0)
    - [Gyrometer Functionblock](http://vorto.eclipse.org/#/details/org.omi.fb/Gyrometer/1.0.0)

### Step 2: Create a Mapping Specification 

The mapping specification defines where among the BLE services and characteristics the data for a property of a property in a functionblock can be found. In some cases there is a 1:1 relation between property and characteristics, in other cases one characteristic may represent multiple properties.
The data sent by a Bluetooth device is typically an array of bytes. The values values of the properties are calculated from these bytes by using XPath expressions and conversion functions. The XPath expression can be used to extract a few bytes from the byte array and the conversion function can then be used to perform calculations based on the extracted value.
To illustrate the above, take a look at the excerpt of the Humidity functionblock mapping for the TI SensorTag:
```
    from Barometer to functions with {
        convertSensorValue: "function convertSensorValue(value) { return value*0.01; }"
    }
    
...
    
    from Barometer.status.sensor_value to source with {
        uuid: "f000aa41-0451-4000-b000-000000000000",
        offset: "3",
        length: "3",
        datatype: "uint32",
        xpath: "barometer:convertSensorValue(conversion:byteArrayToInt(characteristics/${uuid}/data, ${offset}, 0, 0, ${length}))"
    }
```
The expression `characteristics/${uuid}/data` refers to the byte array which is read from the characteristic with the UUID. The expressions `${...}` are placeholders for the attributes defined above, e.g. `${length}` would be replaced by `3` in this example.
In this example the mapping retrieves 3 bytes from the data array starting at offset 3, hence bytes 3, 4, 5, and converts them to an unsigned integer. The `convertSensorValue` function performs the conversion of the value to a real world value. 

Use the Eclipse Vorto IoT Toolset to create the specification and publish them via the Vorto Repository web interface logging into the repository and clicking on "Share".

- TI SensorTag Mapping Specification
    - [Barometer Functionblock](http://vorto.eclipse.org/#/details/org.omi.mapping/Barometer/1.0.2)
    - [Humidity](http://vorto.eclipse.org/#/details/org.omi.mapping/Humidity/1.0.1)
    - [Accelerometer Functionblock](http://vorto.eclipse.org/#/details/org.omi.mapping/Accelerometer/1.0.0)
    - [Gyrometer Functionblock](http://vorto.eclipse.org/#/details/org.omi.mapping/Gyrometer/1.0.0)

The Vorto Mapping Engine already provides some standard functions that you can use in your mapping specification to do e.g. string manipulations or type conversions. For more info, check out the Appendix chapter. 

### Step 3: Use the Vorto Mapping Engine to map data 

To apply the above mapping there are a couple of steps that need to be done:
* Create a new BLE GATT object model from your mapping specification
* Read the data from your Bluetooth devices and set the properties of the object model
* Run the mapper to execute the transformation.

```
// Create a new Bluetooth object model from your mapping
BleGattDevice bleGattDevice = BleGattDeviceBuilder.newBuilder()
                                  .withSpecification(mapping).build();
                                  
// Create a new mapper for Eclipse Ditto        
IDataMapper<DittoData> mapper = IDataMapper.newBuilder()
                                  .withSpecification(mapping).buildDittoMapper();
                                  
// Retrieve references for your Bluetooth characteristics from the object model
BleGattCharacteristic barometerValue = bleGattDevice.getCharacteristics().get("f000aa41-0451-4000-b000-000000000000");
BleGattCharacteristic accelerometerValue = bleGattDevice.getCharacteristics().get("f000aa81-0451-4000-b000-000000000000");

// Read the data from the Bluetooth device and set the property in the object model
Short[] barometerData = <...>
barometerValue.setData(barometerData);
Short[] accelerometerData = <...>
accelerometerValue.setData(accelerometerData);

// Pass the object model to the mapper and execute the mapping        
DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromObject(bleGattDevice),MappingContext.empty());

// Serialize mapped Eclipse Ditto format to JSON
System.out.println(mappedDittoOutput.toJson());
```

Mapped Eclipse Ditto JSON Output:

```
{
    accelerometer": {
        "properties": {
        	"status": {
            	"y_value":-1.0,
            	"z_value":0.0,
            	"x_value":1.0
            }
        }
    },
    "barometer": {
        "properties": {
        	"status":{
            	"sensor_value":20.0
            }
        }
    }
    ...
}
```


# What's next ?

**Great!** You have just mapped a device specific payload to Eclipse Ditto payload via an Vorto Information Model. This mapped data can now be sent to the Bosch IoT Suite (which is based on Eclipse Ditto) using HTTP:

	curl -X PUT 
	https://things.apps.bosch-iot-cloud.com/api/1/things/ADD_THING_ID_HERE/features
	-H 'authorization: Basic  ADD_CREDENTIALS_HERE' \
	-H "Accept: application/json" \
	-H 'x-cr-api-token: ADD_THINGS_API_TOKEN_HERE' \
	-d '{
    accelerometer": {
        "properties": {
        	"status":{
            	"y_value":-1.0,
            	"z_value":0.0,
            	"x_value":1.0
            }
        }
    },
    "barometer": {
        "properties": {
        	"status": {
            	"sensor_value":20.0
            }
        }
    }
	}'