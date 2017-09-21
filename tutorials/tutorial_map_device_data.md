# Map arbitrary device json data to Vorto-compliant Eclipse Ditto format

Often times, you are facing the situation that a device sends its data via e.g. MQTT in a very unique format. Then you look into the Vorto Repository and find functionblocks matching the semantics of the device data, but the device data format and the functionblock data format simply do not match.

For example, a device sends voltage information in the following json format:
	
	{"batteryVoltage": "2345mV"}

The Vorto Repository contains a [standard - based IPSO Voltage functionblock](http://vorto.eclipse.org/#/details/com.ipso.smartobjects/Voltage/0.0.1) with the format:

	...
	status {
		mandatory sensor_value as float
		optional sensor_units as string
		...
	}

At this point, you have to options:

1. Create a new functionblock for the Voltage functionality, e.g. MyVoltage and define the format in the way the device sends it , or
2. Re-use a standard - based IPSO functionblock 'Voltage' 

The first option sounds appealing as it would match exactly your required data structure. But this approach comes with a cost: The repository would end up in yet another Voltage functionblock. This would become viral ending up in hundreds of different Voltage functionality flavors for every other voltage capable device, snow-balling the integration efforts for solutions that need to deal with also your specific voltage function block.

The second approach of having only a single voltage functionality would simplify the integration of devices into solutions being able to deal with different devices capable of sending voltage information. This is clearly the preferable option. But how can we we map different device formats to a specific, re-usable function block ? This is the topic we are going to cover in this tutorial.  
	

## Prerequisite

- [Create and Publish](tutorial-create_and_publish_with_web_editor.md) an information model matching the semantics of the device 
- [Request](https://www.bosch-iot-suite.com/) an evaluation account for the Bosch IoT Suite.  
- [Register](tutorial_register_device.md) the device in the Bosch IoT Suite.

## Steps

Let's look at an example by mapping the JSON data sent by an AWS IoT Button to a data format that can be sent to the Bosch IoT Suite. Take a look a the corresponding [AWS IoT Button Information Model](http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0). The Information Model is described by re-using the OMA IPSO Voltage and PushButton functionblock. 

But, the actual device sends the data like that:

	{"clickType" : "DOUBLE", "batteryVoltage": "2322mV"} 



### 1. Create and Publish a Mapping Specification for the device

Let's describe how the IPSO function blocks map to the JSON data, sent by the device. The result is a Mapping specification that is published to the Vorto Repository.

The [AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0) mapping specification is defined as XPath expressions. The mappings are target platform agnostic and merely map specific device properties to Vorto functionblock properties.
Use the Eclipse Vorto IoT Toolset to create the specification and publish them via the Vorto Repository web interface.

- AWS IoT Button Mapping Specification
	- [Button Mapping Specification for AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button/ButtonPayloadMapping/1.0.0)
	- [Voltage Mapping Specification for AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button/PayloadVoltageMapping/1.0.0)

Looking at the Button Mapping specification more closely, have might have spotted some javascript function that is declared as part of the mapping. This is a very powerful way of expressing custom converter functions as javascript that can be used from within your mapping. 

Excerpt of the Button Mapping:

	namespace devices.aws.button
	version 1.0.0
	displayname "PayloadMapping"
	description "Data Mapping model for AWS IoT Button"
	using com.ipso.smartobjects.Push_button;0.0.1

	functionblockmapping ButtonPayloadMapping {

	targetplatform devices_aws_button_AWSIoTButton_1_0_0
 
	from Push_button to functions with { 
	_namespace: "custom", 
	convertClickType : "function convertClickType(clickType) {
		if (clickType === 'SINGLE') return 1; else if (clickType === 'DOUBLE') return 2; 
		else return 99;}"}

	Push_button.status.digital_input_count to source with {xpath: custom:convertClickType(xpathToValue)}
	...


Here we declare the convertClickType function that converts the JSON clickType property value , e.g. 'DOUBLE' to an Integer value. That function is then used by the functionblock property mapping. 

The Vorto Mapping Engine already provides some standard functions that you can use in your mapping specification to do e.g. string manipulations or type conversions. For more info, checkout the [Data Mapping API Documentation](../server/repo/repository-mapping/Readme.md) 

### 3. Send the device data to Bosch IoT Suite

Once you had published the mapping specifications via the [Vorto Repository Web interface](http://vorto.eclipse.org), you can now send data to Bosch IoT Suite, e.g. via HTTP Connector:

	curl -X PUT -i -H 'Content-Type: application/json' 
	--data-binary '{"clickType" : "DOUBLE", "batteryVoltage": "2322mV"}' 
	https://<hub-endpoint-url>/telemetry/<tenantId>/<deviceId>

### Verify mapped device data in Bosch IoT Things Service

1. Via curl:

	curl -X GET https://things.apps.bosch-iot-cloud.com/api/1/things/<thingId>/features
		-H "Authorization: Basic <credentialsBase64>" 
		-H "Accept: application/json" 
		-H "x-cr-api-token: <thingsApiToken>"

2. Via [Developer Console](https://console.bosch-iot-suite.com)

The mapped JSON output is: 

	{
		"button": { 
			"properties":{ 
				"digital_input_count":2,
				"digital_input_state":true
			}
		},
		"batteryVoltage":{
			"properties":{
				"sensor_units":"mV",
				"sensor_value":2322.0
		 	}
		}
	}

**Great**! Now you can easily map your specific device format to a common abstract data representation making it very simple for device applications to consume and process the data.

## What's next ? 

- [Build a web application for a device](tutorial_create_webapp_dashboard.md)
- [Build an Amazon Alexa Skillset for a device](tutorial_voicecontrol_alexa.md)
