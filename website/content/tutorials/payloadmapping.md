---
date: 2016-03-09T20:08:11+01:00
title: Mapping device payload to standardized Vorto Function Blocks 
shortLink: "#/payloadmapping"
weight: 33
---

IoT Devices may not always send their data to the cloud in the structure and protocol that is required by the IoT platform that the devices are integrating with, e.g. AWS IoT, Azure IoT or Eclipse IoT. Eclipse Vorto provides the tools and libraries for you to achieve that.

In this tutorial you are going to learn, how you create a Vorto Mapping Specification that maps a Distance Sensor JSON payload to standardized [IPSO](https://github.com/IPSO-Alliance/pub) Vorto Function Blocks and execute the mapping specification with the Vorto Mapping Engine. You will integrate the Distance Sensor payload with the Eclipse Ditto Digital Twin service but also learn how to extend the Vorto Mapping Engine to map to other IoT platforms.  

<!--more-->

## Prerequisite

* [Introduction to Vorto Payload Mapping]({{< relref "userguide/mappingengine.md" >}})
* Maven

## 1. Create a Payload Mapping Specification

In this step, we are going to create a mapping specification for the [Distance Sensor](http://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorial/DistanceSensor/1.0.0), that maps a very simple distance JSON payload to IPSO compliant data representation that can be immediately processed by the Eclipse Ditto Service.

For example, the Distance Sensor sends the following JSON payload:
	
	{"distance": "100m"} 

Let's take this example and create a mapping specification for it:

1. In the Vorto Repository, click the **Create Model** Button and select **Mapping**.
	<figure class="screenshot">
	<img src="/images/tutorials/getting_started/create_function_block_designer_btn.png">
	</figure> 
2. Enter a name, for example _DistancePayloadMapping_.
3. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
4. Click **Create**.
	<figure class="screenshot">
  	<img src="/images/tutorials/payload_mapping/create_mapping_payloadDistanceMapping.png">
	</figure>
5. In the Model Editor add the following mapping rules and click **Save**:

	<figure class="screenshot">
	<img src="/images/tutorials/payload_mapping/edit_mapping.png"> 	
	</figure>
> <a target="_blank" href="http://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorial.mapping/DistancePayloadMapping/1.0.0">Source Code</a>



You may have spotted, that we are using a set of different xpath functions to define the mapping rule. You can find more information about all available functions and how to use them in the [Function Reference Documentation](https://github.com/eclipse/vorto/blob/0.10.0.M4/mapping-engine/docs/built_in_converters.md)

**Create another mapping to correlate it to the Distance Sensor:**

Up to now, we have merely created a specification for the payload of the distance data. Now, we must assign it to the actual Distance Sensor Device Information Model:

1. In the Vorto Repository, click the **Create Model** Button and select **Mapping**.
	<figure class="screenshot">
	<img src="/images/tutorials/getting_started/create_function_block_designer_btn.png">
	</figure> 
2. Enter a name, for example _DistanceSensorPayloadMapping_
3. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
4. Click **Create**.
	<figure class="screenshot">
  	<img src="/images/tutorials/payload_mapping/create_mapping_payloadDistanceSensorMapping.png">
	</figure>
6. In the Model Editor add add the following mapping rules and click **Save**:

	<figure class="screenshot">
  	<img src="/images/tutorials/payload_mapping/edit_mapping_Sensor.png">
	</figure>
> <a target="_blank" href="http://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorial.mapping/DistanceSensorPayloadMapping/1.0.0">Source Code</a>

## 3. Execute the Payload Mapping 

Now, let's process the mapping specification with the Vorto Mapping Engine. The Mapping Engine performs a two-phase mapping:

**Phase 1 (Normalization):** Maps the arbitrary device payload to standardized Vorto Function Block payload.

**Phase 2 (Target Platform Mapping):** Maps the Vorto Function Block payload to the target IoT platform - specific data model. 

The Vorto Mapping Engine supports Eclipse Ditto as the target platform mapping out-of-the-box. However, we will show you later, how can you easily plug in your own IoT platform data model mapper (see appendix).

1. First, add the following Maven dependency to your Maven Project:

		<dependency>
		   <groupId>org.eclipse.vorto</groupId>
		   <artifactId>repository-mapping</artifactId>
		   <version>${vorto.version}</version>
		</dependency>

2. Code snippet that transforms a sample Distance Sensor JSON to Eclipse Vorto/Eclipse Ditto compliant data:

		IMappingSpecification spec = MappingSpecificationBuilder.create()
		.infomodelId("org.eclipse.vorto.tutorial.DistanceSensor:1.0.0")
		.targetPlatformKey("distance_ipso")
		.build();

		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(spec).buildDittoMapper();

		String sampleDevicePayload = "{\"distance\": \"100m\"}";

		DittoData mappedResult = mapper.map(DataInput.newInstance().fromJson(sampleDevicePayload),
				MappingContext.empty());
		
		System.out.println(mappedResult.toJson());

3. The console shows the following mapped result:

		{
			"distance":{
				"properties":{
					"status":{
						"sensorValue": 100,
						"sensorUnits": "m"
					}
				}
			}
		}
	
## 4. Test the integration

We are now ready to send the mapped JSON payload and modify an [Eclipse Ditto](https://ditto.eclipse.org/) managed thing:

	 curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
	 			"distance":{ \
	 				"properties":{ \ 
	 					"status":{ \ 
	 						"sensorValue":100, \ 
	 						"sensorUnits":"m" \ 
	 					} \ 
	 				} \ 
	 			} \ 
	 		}' 'https://ditto.eclipse.org/api/1/things/org.eclipse.vorto.tutorial%3distancesensor/features'


**Voila!** Your digital twin of the Distance Sensor is now stored as standardized data, described as Vorto Function Blocks. 

### Appendix: Plug-in other IoT Platform Data Mapper

The Vorto Mapping Engine is IoT platform agnostic, thus allowing you to extend it in order to integrate devices in a standardized way with other IoT platforms, like AWS IoT Shadow or Azure IoT. 

Let's take a look what you need to do in order to do that:

1. Add the Mapping Engine to your Maven project, if you have not already done so:

		<dependency>
		   <groupId>org.eclipse.vorto</groupId>
		   <artifactId>mapping-engine</artifactId>
		   <version>${vorto.version}</version>
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
		
		String sampleDeviceData = "{\"distance\": \"100m\"}";
		
		MyPlatformModel result = mapper.map(DataInput.newInstance().fromJson(sampleDeviceData), MappingContext.empty());

		