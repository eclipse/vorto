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

The following code snippet maps the [AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0) data to Eclipse Ditto JSON format:

The [AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0) mappings are defined as Vorto Mappings with XPath expressions. The mappings are target platform agnostic and merely map specific device properties to Vorto functionblock properties.

- [Button Mapping](http://vorto.eclipse.org/#/details/devices.aws.button/ButtonPayloadMapping/1.0.0)
- [Voltage Mapping](http://vorto.eclipse.org/#/details/devices.aws.button/PayloadVoltageMapping/1.0.0)

Example:

```
DataMapperBuilder builder = IDataMapper.newBuilder();
// add Loader to load Information Model and AWS IoT Button mappings from the Vorto Repository
builder.withModelLoader(new RepositoryLoader(ModelId.fromPrettyFormat("devices.aws.button.AWSIoTButton:1.0.0"), "awsiotbutton"));

// Add custom Converter Functions needed during runtime
builder.withConverters(MyConverterFunctions.class,"custom");

JsonToDittoMapper mapper = builder.buildDittoMapper();
									
String deviceJSON = "{\"clickType\" : \"SINGLE\", \"batteryVoltage\": \"4422mV\"}";

DittoOutput mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(deviceJSON));

Feature voltageFeature = mappedDittoOutput.getFeatures().get("batteryVoltage");
		
assertEquals(4422f,voltageFeature.getProperty("sensor_value"));
assertEquals("mV",voltageFeature.getProperty("sensor_units"));

// Serialize mapped Eclipse Ditto format to JSON
System.out.println(mappedDittoOutput.toJson());

/*
 * Custom Xpath converter function that is registered 
 */
public static class MyConverterFunctions {
		
	public static int clickType(String clickTypeValue) {
		if (clickTypeValue.equalsIgnoreCase("single")) {
			return 1;
		} else if (clickTypeValue.equalsIgnoreCase("double")) {
			return 2;
		} else {
			return 99;
		}
	}
}
```

