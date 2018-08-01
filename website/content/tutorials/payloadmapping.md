---
date: 2016-03-09T20:08:11+01:00
title: Mapping device payload to standardized Vorto Function Blocks 
shortLink: "#/payloadmapping"
weight: 33
---

IoT Devices may not always send their data to the cloud in the structure and protocol that is required by the IoT platform that the devices are integrating with, e.g. AWS IoT, Azure IoT or Eclipse IoT. Eclipse Vorto provides the tools and libraries for you to achieve that.

In this tutorial you are going to learn, how you create a Vorto Mapping Specification that maps a AWS IoT Button JSON payload to standardized [IPSO](https://github.com/IPSO-Alliance/pub) Vorto Function Blocks and execute the mapping specification with the Vorto Mapping Engine. You will integrate the AWS IoT Button payload with the Eclipse Ditto Digital Twin service but also learn how to extend the Vorto Mapping Engine to map to other IoT platforms.  

<!--more-->

## Prerequisite

* [Payload Mapping Engine Documentation]({{< relref "documentation/mappingengine.md" >}})
* Maven

## 1. Create AWS IoT Button Information Model

Let's start by creating an information model for the AWS IoT Button using the [Vorto IoT Repository](http://vorto.eclipse.org)

1. Click the **Create new Model** Button and choose the type you want to create from the context menu.
	<figure class="screenshot">
	<img src="/images/tutorials/getting_started/create_function_block_designer_btn.png">
	</figure> 
2. Enter a name, for example, AWSIoTButton as a **Information Model**.
3. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
4. Click **Create**.
	<figure class="screenshot">
  	<img src="/images/tutorials/payload_mapping/create_model_awsIotButton.png">
	</figure>
5. In the Model Editor add the AWSIoTButton specific properties and click **Save**:
	<figure class="screenshot">
  	<img src="/images/tutorials/payload_mapping/edit_model.png">
	</figure>
> <a target="_blank" href="http://vorto.eclipse.org/#/details/devices.aws.button/AWSIoTButton/1.0.0">Source Code</a>


## 2. Define the Payload Mapping

Now it's time to define the actual payload mapping specification that defines how the actual AWS IoT Button payload maps to the standardized Vorto Function Blocks. 

For example, the AWS IoT button sends the following JSON payload:
	
	{"clickType" : "DOUBLE", "batteryVoltage": "2322mV"}  

Let's take this example and create a mapping specification for the batteryVoltage payload:

1. Click the **Create new Model** Button and choose the type you want to create from the context menu.
	<figure class="screenshot">
	<img src="/images/tutorials/getting_started/create_function_block_designer_btn.png">
	</figure> 
2. Enter a name, for example, PayloadVoltageMapping as a **Mapping**.
3. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
4. Click **Create**.
	<figure class="screenshot">
  	<img src="/images/tutorials/payload_mapping/create_mapping_payloadVoltageMapping.png">
	</figure>
5. In the Model Editor add the following mapping rules and click **Save**:

	<figure class="screenshot">
  	<img src="/images/tutorials/payload_mapping/edit_mapping.png">
	</figure>
> <a target="_blank" href="http://vorto.eclipse.org/#/details/devices.aws.button.mapping/BatteryVoltagePayloadMapping/1.0.0">Source Code</a>



You may have spotted, that we are using a set of different xpath functions to define the mapping rule. You can find more information about all available functions and how to use them in the [Function Reference Documentation](https://github.com/eclipse/vorto/blob/0.10.0.M3/server/repo/repository-mapping/docs/built_in_converters.md)

**Create another mapping to correlate it to the AWS IoT Button:**

1. Click the **Create new Model** Button and choose the type you want to create from the context menu.
	<figure class="screenshot">
	<img src="/images/tutorials/getting_started/create_function_block_designer_btn.png">
	</figure> 
2. Enter a name, for example, AWSIoTButtonPayloadMapping as a **Mapping**.
3. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
4. Click **Create**.
	<figure class="screenshot">
  	<img src="/images/tutorials/payload_mapping/create_mapping_AWSIoTButtonPayloadMapping.png">
	</figure>
6. In the Model Editor add add the following mapping rules and click **Save**:

	<figure class="screenshot">
  	<img src="/images/tutorials/payload_mapping/edit_mapping_BatteryVoltage.png">
	</figure>
> <a target="_blank" href="http://vorto.eclipse.org/#/details/devices.aws.button.mapping/AWSIoTButtonPayloadMapping/1.0.0">Source Code</a>

For sake of completion, we have added the mapping specification for button functionality as well:

-   [Button Mapping Specification for AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button.mapping/ButtonPayloadMapping/1.0.0)
-   [Voltage Mapping Specification for AWS IoT Button](http://vorto.eclipse.org/#/details/devices.aws.button.mapping/BatteryVoltagePayloadMapping/1.0.0)


## 3. Execute the Payload Mapping 

Now, let's process the mapping specification with the Vorto Mapping Engine. The Mapping Engine performs a two-phase mapping:

**Phase 1 (Normalization):** Maps the arbitrary device payload to standardized Vorto Function Block payload.

**Phase 2 (Target Platform Mapping):** Maps the Vorto Function Block payload to the target IoT platform - specific data model. 

The Vorto Mapping Engine supports Eclipse Ditto as the target platform mapping out-of-the-box. However, we will show you later, how can you easily plug in your own IoT platform data model mapper (see appendix).

1. First, add the following Maven dependency to your Maven Project:

		<dependency>
		   <groupId>org.eclipse.vorto</groupId>
		   <artifactId>repository-mapping</artifactId>
		   <version>0.10.0.M3</version>
		</dependency>

2. Code snippet that transforms a sample AWS IoT Button JSON to Eclipse Vorto/Eclipse Ditto compliant data:

		IMappingSpecification spec = MappingSpecificationBuilder.create()
		.infomodelId("devices.aws.button.AWSIoTButton:1.0.0")
		.targetPlatformKey("aws_ipso")
		.build();

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(spec).buildDittoMapper();

		String sampleDevicePayload = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";

		DittoData mappedResult = mapper.map(DataInput.newInstance().fromJson(sampleDevicePayload),
				MappingContext.empty());
		
		System.out.println(mappedResult.toJson());

3. The console shows the following mapped result:

		{
			"button":{
				"properties":{
					"configuration":{},
					"status":{
						"digital_input_count":2,
						"digital_input_state":true
					}
				}
			},
			"voltage":{
				"properties":{
					"configuration":{},
					"status":{
						"sensor_units":"mV",
						"sensor_value":2322.0
					}
				}
			}
		}
	
## 4. Test the integration

We are now ready to send the mapped JSON payload and modify an [Eclipse Ditto](https://ditto.eclipse.org/) managed thing:

	 curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
	 			"button":{ \
	 				"properties":{ \ 
	 					"configuration":{}, \ 
	 					"status":{ \ 
	 						"digital_input_count":2, \ 
	 						"digital_input_state":true \ 
	 					} \ 
	 				} \ 
	 			}, \ 
	 			"voltage":{ \ 
	 				"properties":{ \ 
	 					"configuration":{}, \ 
	 					"status":{ \ 
	 						"sensor_units":"mV", \ 
	 						"sensor_value":2322.0 \ 
	 					} \ 
	 				} \ 
	 			} \ 
	 		}' 'https://ditto.eclipse.org/api/1/things/org.eclipse.vorto%3aws-iot-button/features'


**Voila!** Your digital twin of the AWS IoT Button is now stored as standardized data, described as Vorto Function Blocks. 

### Appendix: Plug-in other IoT Platform Data Mapper

The Vorto Mapping Engine is IoT platform agnostic, thus allowing you to extend it in order to integrate devices in a standardized way with other IoT platforms, like AWS IoT Shadow or Azure IoT. 

Let's take a look what you need to do in order to do that:

1. Add the Mapping Engine to your Maven project, if you have not already done so:

		<dependency>
		   <groupId>org.eclipse.vorto</groupId>
		   <artifactId>repository-mapping</artifactId>
		   <version>0.10.0.M4</version>
		</dependency>

2. Extend the *org.eclipse.vorto.service.mapping.AbstractDataMapper* and override the *doMap()* method 

		import java.util.HashMap;
		import java.util.Map;
		
		import org.eclipse.vorto.service.mapping.AbstractDataMapper;
		import org.eclipse.vorto.service.mapping.MappingContext;
		import org.eclipse.vorto.service.mapping.normalized.FunctionblockData;
		import org.eclipse.vorto.service.mapping.normalized.InfomodelData;
		import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;
		
		public class MyPlatformMapper extends AbstractDataMapper<MyPlatformModel> {
			
			public MyPlatformMapper(IMappingSpecification spec) {
				super(spec);
			}
		
			@Override
			protected MyPlatformModel doMap(InfomodelData normalized, MappingContext mappingContext){
				// add your code here that maps the already normalized Vorto model to the platform specific model
			}
			
		
		}

3. Use the Mapper to convert arbitrary device data to the specific platform data model:

		IDataMapper<MyPlatformModel> mapper = new MyPlatformMapper(mappingSpecification);
		
		String sampleDeviceData = "{\"clickType\" : \"DOUBLE\", \"batteryVoltage\": \"2322mV\"}";
		
		MyPlatformModel result = mapper.map(DataInput.newInstance().fromJson(sampleDeviceData), MappingContext.empty());

		