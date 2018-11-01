# Vorto Payload Mapping Engine

The Payload Mapping Engine allows to map arbitrary device data to platform - specific data structure complying to Vorto Information Models. 

## Getting started

There are a couple steps you need to complete to successfully integrate the Vorto mapping engine into your project. They are outlined below just follow one after another and you will be good to go in no time.

### Create a mapping

1. Login to [the Vorto Repository](https://vorto.eclipse.org/)
2. Select an existing Information Model or optionally create a new Information Model
3. Choose 'Create Payload Mapping' and specify a new mapping key, e.g. myplatform 

5. Specify the mapping rules using (x)path expressions. You can test your mapping right away by selecting 'Test' and specify device test data.
6. Save your mapping and click 'Download Mapping'. This will download the mapping specification as JSON.


### Add Mapping Engine dependency

To use the mapping engine you need to add the dependencies to your project. Depending on what features you want to use later you might not need all, but for now let’s add all of them.

#### Maven
```maven
<dependency>
   <groupId>org.eclipse.vorto</groupId>
   <artifactId>mapping-engine-all</artifactId>
   <version>LATEST</version>
</dependency>
<dependency>
   <groupId>org.eclipse.vorto</groupId>
   <artifactId>mapping-core</artifactId>
   <version>LATEST</version>
</dependency>
<dependency>
   <groupId>org.eclipse.vorto</groupId>
   <artifactId>mapping-converter-javascript</artifactId>
   <version>LATEST</version>
</dependency>
```
#### Gradle
```gradle
implementation('org.eclipse.vorto:mapping-core-android:+')
implementation('org.eclipse.vorto:mapping-engine-android:+')
implementation('org.eclipse.vorto:mapping-converter-javascript-android:+')
```

### Use the mapping engine
You are almost done, since you already have a specification and the dependencies you now only need to add a couple lines of code where ever you want to use the mapping engine
```java
MappingEngine engine = MappingEngine.create(mappingSpecJSON);

InfomodelData mappedOutput = engine.map(DataInput.newInstance().fromObject(deviceData));

```

If there are any open questions maybe the [demo app](demo) can help you if not feel free to just drop us a note.


## Good to know

### IDataMapper

To allow a fine regulation of what the mapping engine should or should not do you can use the direct DataMapper Builder to add your own custom java functions

```java
IMappingSpecification specification = IMappingSpecification.newBuilder().fromInputStream(getAssets()
                    .open("devices_TISensorTag_1.0.1-mappingspec_test.json"))
                    .build();

IDataMapper mapper = IDataMapper.newBuilder()
                    .registerScriptEvalProvider(new JavascriptEvalProvider())
                    .registerConverterFunction(StringFunctionFactory.createFunctions())
                    .registerConditionFunction(DateFunctionFactory.createFunctions())
                    .withSpecification(specification).build();
                    
InfomodelValue value = mapper.mapSource(obj);
```

### Custom Functions

Writing all your conversions using Javascript is a little bit tedious, that’s why we offer the possibility to develop your own converter functions and register them on engine creation.

To register them add your FunctionFactory on creation time like this:

```java
IDataMapper.newBuilder()
	    .registerScriptEvalProvider(new JavascriptEvalProvider())
            .registerConverterFunction(BinaryFunctionFactory.createFunctions())
            .registerConverterFunction(DateFunctionFactory.createFunctions())
            .registerConverterFunction(StringFunctionFactory.createFunctions())
            .registerConverterFunction(TypeFunctionFactory.createFunctions())
            .registerConditionFunction(BinaryFunctionFactory.createFunctions())
            .registerConditionFunction(DateFunctionFactory.createFunctions())
            .registerConditionFunction(StringFunctionFactory.createFunctions())
            .registerConditionFunction(TypeFunctionFactory.createFunctions())
            .withSpecification(specification);
```
To see how one would develop a FunctionFactory check out the packages: 
* [mapping-converter-binary](./mapping-converter-binary)
* [mapping-converter-date](./mapping-converter-date)
* [mapping-converter-string](./mapping-converter-string)

### convenience Class

To provide working functions for each supported platform we offer a convenience class that adds most functions to the engine. Checkout out the sample code below on how to use it.

```java
IMappingSpecification specification = IMappingSpecification.newBuilder().fromInputStream(getAssets()
                    .open("devices_TISensorTag_1.0.1-mappingspec_test.json"))
                    .build();                    
MappingEngine mappingEngine = new MappingEngine(specification);
InfomodelValue value = mapper.mapSource(obj);
```

Add to maven project:
```maven
<dependency>
   <groupId>org.eclipse.vorto</groupId>
   <artifactId>mapping-engine-all</artifactId>
   <version>LATEST</version>
</dependency>
```

Add to Android Project:
```gradle
implementation('org.eclipse.vorto:mapping-engine-android')
implementation('org.eclipse.vorto:mapping-core-android')

```





