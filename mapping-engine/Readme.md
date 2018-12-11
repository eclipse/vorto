# Getting started with Vorto Mappings

Vorto abstracts device data using Function Blocks, thus helping to reduce tight-coupling of devices in IoT solutions. But how does Vorto help to map the actual device data to these abstract Function Blocks? 

In this tutorial, we are going to walk you through the process of creating a Vorto mapping specification for an Information Model and execute it with the Vorto Payload Mapping Engine.

## Prerequisite

To work through this tutorial, you will need:

- A Github account to log in to the Vorto Repository
- A [Vorto Information Model](https://www.eclipse.org/vorto/tutorials/tisensor/), managed in the Vorto Repository


## Step 1: Create Mapping Specification

A mapping adds platform specific information to an Information Model. Since the representation of data can vary from platform to platform.

To create a mapping go to your newly created model and press the **Create Mapping Spec** Button
![create mapping spec button](./docs/create_mapping_spec_button.png)

Now add a Target Platform key for your mapping to signal, which platform this mapping belongs to, like blegatt. Each target platform should offer its own object model which is passed to the mapping engine. This ensures that a mapping is independent of the underlying driver.
![platform key](./docs/target_platform_key.png)

Now the web editor opens and allows you to add mapping expression for the Function Blocks you added. You can write XPath 2.0 like notation. Behind the scenes the engine uses [JXPath](https://commons.apache.org/proper/commons-jxpath/) to apply XPath expressions on a java object graph. To add functionality that may not be possible using jxpath, you can also add custom JavaScript or java functions (see the custom functions section).
![xpath](./docs/xpath.png)
Once you have written your xpath expressions, press Save.

## Step 2: Test the Mapping Specification

n the right handside, define the arbitrary device payload (in JSON format) and click **Map**: 

![mapping editor test](./docs/mapping_editor_test.png)


## Step 2: Download & Execute Mapping Specification

Download and save the Mapping Specification to start integrating it with the engine:

![download json spec](./docs/download_spec_button.png)

### 1. Add Maven dependency:
```
<dependency>
	<groupId>org.eclipse.vorto</groupId>
	<artifactId>mapping-engine-all</artifactId>
	<version>LATEST</version>
</dependency>
```

### 2. Initialize the mapping engine with the downloaded specification:

```Java
MappingEngine engine = MappingEngine.createFromInputStream(FileUtils.openInputStream(new File("src/main/resources/mappingspec.json")));

```

### 3. Pass the arbitrary device payload to the engine to get it converted to Vorto compliant data:

```Java
Object deviceData = ...;
InfomodelValue mappedData = engine.map(deviceData);

```

### 4. Optionally validate the mapped data to check if it complies to the Vorto model:

```Java
ValidationReport validationReport = mappedData.validate();
if (!validationReport.isValid()) {
	// handle invalid data
}

```

### 5. Convert mapped data to Digital Twin IoT compliant data
 
Convert the mapped data to IoT Platform data. The mapping engine provides a useful utility in order to create a JSON object complying to the Eclipse Ditto protocol:

```Java
import com.google.gson.JsonObject;
import org.eclipse.vorto.mapping.engine.twin.TwinPayloadFactory;
...

final String dittoNamespace = "org.mycompany";
final String dittoNamespaceSuffix = "123";
JSONObject dittoPayload = TwinPayloadFactory.toDittoProtocol(mappedData, dittoNamespace, dittoNamespaceSuffix);

sendToDitto(dittoPayload);
```

## Advanced Usage

The Vorto Mapping Engine has extension points in order to plug-in converter functions that can be used as part of your mapping rules.

### Custom functions

Custom functions, like one would expect, add the power to write your own converter functions that can be used in an xpath context.
A function always belongs to a namespace.

#### Java

To add a custom Java function call on your IDataMapperBuilder
```Java
IDataMapper.newBuilder().registerConverterFunction(IFunction [])
```
An element of such an array could look like
```Java
private static final IFunction FUNC_BASE64 = new ClassFunction("vorto_base64", Base64.class);
```
where **vorto_base64** would be the namespace that’s added and the functions contained in the Base64 Class the added functions.
So this Class would add 
```
vorto_base64:decodeString()
vorto_base64:decodeByteArray()
```
to the xpath workspace.

#### Javascript
To add a custom function for JavaScript use the web editor. The function will be register in the namespace of the function block and can be any JavaScript function, but you cannot side load libs. 
![custom function](./docs/custom_js_function.png)





