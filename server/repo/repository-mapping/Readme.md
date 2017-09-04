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

The following steps describe how to map device-specific JSON data to Eclipse Ditto JSON format using Vorto Mappings. 

Let's look at an example by mapping the JSON data sent by an AWS IoT Button to a  data format that can be sent to Eclipse Ditto.

### Step 1: Create and Publish an Information Model for the device

First, create the [AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0) information model consisting of a button- as well as a voltage functionality described as Vorto functionblocks. 

[Read more...](../../../tutorials/tutorial_create_generator.md)

### Step 2: Create device payload mappings 

Now let's describe how the JSON properties of the device map to the Information Model.

The [AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0) mappings are defined as Vorto Mappings using XPath expressions. The mappings are target platform agnostic and merely map specific device properties to Vorto functionblock properties.
You can use the Eclipse Vorto IoT Toolset to create the data mappings:

- [Button Mapping](http://vorto.eclipse.org/#/details/devices.aws.button/ButtonPayloadMapping/1.0.0)
- [Voltage Mapping](http://vorto.eclipse.org/#/details/devices.aws.button/PayloadVoltageMapping/1.0.0)
 
### Step 3: Use Vorto Mapping Engine to map data 

Last but not least, you can use the Mapping Engine that takes the device JSON data as input and outputs the Eclipse Ditto format:

```
DataMapperBuilder builder = IDataMapper.newBuilder();
// add Loader to load Information Model and AWS IoT Button mappings from the Vorto Repository
builder.withModelLoader(new RepositoryLoader(ModelId.fromPrettyFormat("devices.aws.button.AWSIoTButton:1.0.0"), "awsiotbutton"));

JsonToDittoMapper mapper = builder.buildDittoMapper();
									
String deviceJSON = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(deviceJSON));

Feature voltageFeature = mappedDittoOutput.getFeatures().get("batteryVoltage");
		
assertEquals(4422f,voltageFeature.getProperty("sensor_value"));
assertEquals("mV",voltageFeature.getProperty("sensor_units"));

// Serialize mapped Eclipse Ditto format to JSON
System.out.println(mappedDittoOutput.toJson());

```

Mapped Eclipse Ditto JSON Output:

```
{
	"features": { 
		"button": { 
			"properties":{ 
				"digital_input_count":2,
				"digital_input_state":true
			}
		},
		"voltage":{
			"properties":{
				"sensor_units":"mV",
				"sensor_value":2322.0
		 	}
		}
	}
}

```

