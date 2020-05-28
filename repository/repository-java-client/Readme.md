# Getting Started with Repository Java Client

The Vorto Repository Client for Java provides an easy way to interact with the Vorto Repository. 

Use the Client API to
- search for models by full-text search 
- read model content
- download model attachments, such as images etc.
- generate code for IoT platforms that have been registered as [Vorto Repository Code Generators](http://vorto.eclipse.org/#!/generators)

### Maven dependency

```
<dependency>
   <groupId>org.eclipse.vorto</groupId>
   <artifactId>repository-java-client</artifactId>
   <version>{vorto.version}</version>
</dependency>

```

# Features

## Vorto Model Search

### Search by free text
The following code snippet searches for all devices that have 'sensors'

```java
IRepositoryClient repositoryClient = IRepositoryClient.newBuilder().build();	
Collection<ModelInfo> models = repositoryClient.search("sensor");
```

### Fetch a specific model 

The following code snippet shows how to fetch the [Bosch GLM100C Information Model](http://vorto.eclipse.org/#!/details/com.bosch/BoschGLM100C/1.0.0)

```java
ModelId boschGlm = new ModelId("BoschGLM100C", "com.bosch", "1.0.0");
ModelContent boschGlmModelContent = repositoryClient.getContent(boschGlm);
```

## Code Generation

### Generate code for a specific IoT platform

The following snippet shows how to generate a Eclipse Hono Java Client for a Bosch GLM 100C information model. 
The generated bundle contains source code that sends GLM specific sensor data to Eclipse Hono in a Eclipse Vorto compliant format.

```java
ModelId boschGlm = new ModelId("BoschGLM100C", "com.bosch", "1.0.0");
Map<String,String> invocationConfig = new HashMap<String,String>();
invocationConfig.put("language","java");

GeneratedOutput generatedKuraApplication = repositoryClient.generate(boschGlm, "eclipsehono", invocationConfig);
```
