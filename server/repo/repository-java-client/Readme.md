# Java Client API for Eclipse Vorto

Here We introduce to our Java Client API for Eclipse Vorto Repository which is able to search information models and get its details. We can also trigger generation of code to a specific platform target.     
Eg. Client code for Kura using Eclipse Kura generator.

Here are a few use cases where you could use our Java Client API

- To query information models in Vorto Repository
  - Fetch all available Information models
  - Find a specific Information model
- Code generation for a specific client platform
- Resolve model ids to information models

## To query information models
The following code snippet is a set up snippet for the above use cases.

```
RepositoryClientBuilder builder = RepositoryClientBuilder.newBuilder()
	.setBaseUrl("http://vorto.eclipse.org");
		
IModelRepository modelRepo = builder.buildModelRepositoryClient();
```

### Fetch all available Information models
We can get all the available models by using the following code snippet. Here we are using a free text search, where we pass a wild card (*) as the search parameter.

```
Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().freeText("*").build()).get();
			
```

### Find a specific Information model
To find an specific information model for specific model Id, we need to know the model Id and pass that Model Id in the search query as shown in the following code snippet.

```
/** 
 * name -> "XDK"
 * namespace -> "com.bosch.devices"
 * version -> "1.0.0"
 *
 */
ModelId xdk = new ModelId("XDK", "com.bosch.devices", "1.0.0");
ModelInfo xdkModelInfo = modelRepo.getById(xdk).get();
```

To get the function block model, try the following

```
FunctionblockModel xdkFbModel = modelRepo.getContent(xdk, FunctionblockModel.class).get();
```

## Code generation for a specific client platform
Example: The following snippet shows how to generate code for Bluetooth and Bosch Cloud clients using Eclipse Kura generator. 

```
IModelGeneration modelGen = builder.buildModelGenerationClient();

ModelId xdk = new ModelId("XDK", "com.bosch.devices", "1.0.0");
Map<String,String> invocationParams = new HashMap<String,String>();
invocationParams.put("bluetooth","true");
invocationParams.put("boschcloud", "true");

GeneratedOutput generatedOutput = modelGen.generate(xkd, "kura", invocationParams).get();
```

## To Resolve Ids to Information Models

Here we show you how to resolve LWM2M (Light Weight M2M) model id to LWM2M information models using LWM2M model resolvers.

```
IModelResolver modelResolver = builder.buildModelResolverClient();
			
// where "3300" is the object id for the Generic_Sensor_lwm2m found in the Mappings
ModelInfo lwm2mModelInfo = modelResolver.resolve(new LWM2MQuery("3300")).get();
```
