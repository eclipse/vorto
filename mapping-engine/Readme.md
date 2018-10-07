# Vorto Payload Mapping Engine

The Payload Mapping Engine allows to map arbitrary device data to platform - specific data structure complying to Vorto Information Models.  

Follow the following easy steps to get started: 

1. Login to Vorto Repository
2. Select an existing Information Model or optionally create a new Information Model
3. Choose 'Create Payload Mapping' and specify a new mapping key, e.g. myplatform
4. Specify the mapping rules using (x)path expressions. You can test your mapping right away by selecting 'Test' and specify device test data.
5. Save your mapping and click 'Download Mapping'. This will download the mapping specification as JSON.
6. Add the following Maven dependency to your project:

```
<dependency>
   <groupId>org.eclipse.vorto</groupId>
   <artifactId>mapping-engine</artifactId>
   <version>LATEST</version>
</dependency>

```

7. Configure the mapping engine with your downloaded mapping specification JSON:

```
InputStream mappingSpecJSON = ...; // read json from Classpath

IMappingSpecification spec = IMappingSpecification.newBuilder().fromInputStream(mappingSpecJSON).build();		

IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec).build();

InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromObject(deviceData), MappingContext.empty());

```
