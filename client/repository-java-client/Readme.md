# Vorto Repository Client API for Java

The Vorto Repository Client API for Java provides various services to interact with the Vorto Repository and use its models. 

Use the Client API to
- search for models by full-text search 
- read model content
- generate code for IoT platforms that have been registered as [Vorto Repository Code Generators](http://vorto.eclipse.org/#/generators)

### Maven dependency

```
<dependency>
   <groupId>org.eclipse.vorto</groupId>
   <artifactId>repository-java-client</artifactId>
   <version>0.10.0.M4</version>
</dependency>

```

# Features

## Vorto Model Search

### Search by free text
The following code snippet searches for all devices that have 'sensors'

```
RepositoryClientBuilder builder = RepositoryClientBuilder.newBuilder()
	.setBaseUrl("http://vorto.eclipse.org");	
IModelRepository repository = builder.buildModelRepositoryClient();
Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().type(ModelType.InformationModel).freeText("sensors").build()).get();

```

### Fetch a specific model 

The following code snippet shows how to fetch the [Bosch GLM100C Information Model](http://vorto.eclipse.org/#/details/com.bosch/BoschGLM100C/1.0.0)

```
ModelId boschGlm = new ModelId("BoschGLM100C", "com.bosch", "1.0.0");
InformationModel boschGlmModelContent = modelRepo.getContent(boschGlm, InformationModel.class).get();
```

### Fetch a model containing platform attributes 

The following code snippet illustrates how to get a Vorto functionblock model that contains LWM2M attributes [LWM2M/IPSO TemperatureSensor](http://www.openmobilealliance.org/tech/profiles/lwm2m/3303.xml):

```
IModelRepository modelRepository = builder.buildModelRepositoryClient();	
FunctionBlockModel temperatureSensorContent = modelRepo.getContent(ModelId.fromPrettyFormat("com.ipso.smartobjects.Temperature:0.0.1"), FunctionblockModel.class,"lwm2m").get();
String lwM2MObjectId = temperatureSensorContent.getMappedAttributes("ObjectID");
```

### Search model properties by platform attributes

```
IModelRepository modelRepository = builder.buildModelRepositoryClient();
			
FunctionBlockModel temperatureSensorContent = modelRepo.getContent(ModelId.fromPrettyFormat("com.ipso.smartobjects.Temperature:0.0.1"), FunctionblockModel.class,"lwm2m").get();

IMapping mapping = builder.buildMappingClient();

List<ModelProperty> properties = mapping.newPropertyQuery(temperatureSensorContent).stereotype("Resource").attribute("ID", "5602").list();

```

## Code Generation

### Generate code for a specific IoT platform

The following snippet shows how to generate a Eclipse Hono Java Client for a Bosch GLM 100C information model. 
The generated bundle contains source code that sends GLM specific sensor data to Eclipse Hono in a Eclipse Vorto compliant format.

```
IModelGeneration modelGen = builder.buildModelGenerationClient();

ModelId boschGlm = new ModelId("BoschGLM100C", "com.bosch", "1.0.0");
Map<String,String> invocationConfig = new HashMap<String,String>();
invocationConfig.put("language","java");

GeneratedOutput generatedKuraApplication = modelGen.generate(boschGlm, "eclipsehono", invocationConfig).get();
```
