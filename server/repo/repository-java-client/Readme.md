# Vorto Repository Client API for Java

The Vorto Repository Client API for Java provides various services to interact with the Vorto Repository and use its models. 

Use the Client API to
- search for models by full-text search 
- read model content
- generate code for IoT platforms that have been registered as [Vorto Repository Code Generators](http://vorto.eclipse.org/#/generators)
- resolve a model by platform-specific identifiers, e.g. lwm2m object identifiers


## Search information models
The following code snippet searches for all devices that have 'sensors'

```
RepositoryClientBuilder builder = RepositoryClientBuilder.newBuilder()
	.setBaseUrl("http://vorto.eclipse.org");	
IModelRepository repository = builder.buildModelRepositoryClient();
Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().type(ModelType.InformationModel).freeText("sensors").build()).get();

```

## Fetch a specific information model and its content

The following code snippet shows how to fetch the [Bosch GLM100C Information Model](http://vorto.eclipse.org/#/details/com.bosch/BoschGLM100C/1.0.0)

```
ModelId boschGlm = new ModelId("BoschGLM100C", "com.bosch", "1.0.0");
InformationModel boschGlmModelContent = modelRepo.getContent(boschGlm, InformationModel.class).get();
```

## Generate code for a specific IoT platform

The following snippet shows how to generate a Eclipse Kura application for a Bosch GLM information model, that reads data via Bluetooth and sends 
the data to the Bosch IoT Suite cloud platform.  

```
IModelGeneration modelGen = builder.buildModelGenerationClient();

ModelId boschGlm = new ModelId("BoschGLM100C", "com.bosch", "1.0.0");
Map<String,String> invocationConfig = new HashMap<String,String>();
invocationConfig.put("bluetooth","true");
invocationConfig.put("boschcloud", "true");

GeneratedOutput generatedKuraApplication = modelGen.generate(boschGlm, "kura", invocationConfig).get();
```

## Resolve Vorto Models by platform-specific attributes

The following code snippet illustrates how to get the Vorto functionblock model for a [LWM2M/IPSO TemperatureSensor](http://www.openmobilealliance.org/tech/profiles/lwm2m/3303.xml) object ID:

```
IModelResolver modelResolver = builder.buildModelResolverClient();
			
// where "3303" is the object id for the OMA LWM2M/IPSO TemperatureSensor
ModelInfo temperatureSensorInfo = modelResolver.resolve(new LWM2MQuery("3303")).get();
FunctionBlockModel temperatureSensorContent = modelRepo.getContent(temperatureSensorInfo, FunctionblockModel.class).get();

```
