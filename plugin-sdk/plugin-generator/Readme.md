# Generator Plugin SDK

Vorto Generator plugins are meant to help you to ease integration of devices into your application. 
Generators are used to generate source code from Vorto Information Models. 
An example of a generator could be generating a piece of device firmware that connects to the AWS cloud and send device data in such a way, that the AWS cloud expects it.

## Version 2 (new) 

The version 2 is the new way of providing generators and plugging them into the Vorto Repository. Compared to Version 1, it is much simpler with less communication overhead to the Vorto Repository to load data.  

Each Generator must make sure to provide two endpoints, that are called by the Vorto Repository upon code generation:

<table>
	<tr>
		<th>Generator Info</th>
		<td>
			Gives meta information about the generator plugin, such as description, vendor, logos, etc. This meta - information is displayed in the plugins overview of the repository. 
		</td>
	</tr>
	<tr>
		<th>Generator Execution</th>
		<td>
			Performs the actual code generation for a given Vorto model and returns the generated file(s).
		</td
	</tr>
</table>

[Download OpenAPI spec](docs/generator-openapi.yml)

### Example

It is totally up to you, which language you choose to implement a generator, either Java, Go, Node.js etc.

Checkout our [generator example](https://github.com/eclipse/vorto-examples/tree/master/vorto-generators/v2), that is implemented in Node.js and deployed as a AWS Lambda function. 

### Register your plugin

Please send us an email([vorto-dev@eclipse.org](mailto:vorto-dev@eclipse.org)), containing the endpoint and the generator plugin key, and we will get in touch with you.

## Version 1 (old)

Vorto still provides the old way of creating and running Generators. The Vorto Repository still supports this API Version but it is highly recommended to use Version 2 (see above), as we might no longer support this version in future releases.

This tutorial will explain how to build a generator using API Version 1 with Java and run it as a docker container.
 

### Getting Started

#### Setup
To follow this tutorial you need a working docker instance to test your generator in the end or a working Vorto repository instance. Check the install instructions for the docker images.
Since this project also uses xtext you might want to add an xtext plugin to your IDE.

#### Hello World

To get started we have to download the generator template that is produced by the Vorto Generator Generator (That’s right we are using a generator to generate Generators ;)). 

[Source Code Template](generator-template)

Download and open it in your favorite IDE

So what are those files? What did the Generator do?

The generator created 3 maven modules. One module which contains the generator code, one module which runs the generator as a spring app and one two tie the last two together. Throughout this tutorial we will be referring to those modules as Generator module, Runner module and Parent module.

We will explain each module and the containing files one after another.

So let's get started with the Generator module.

#### Generator module

The Generator module contains the code that determines how the information model will be processed and what information will be returned to the user, when using your generator.

To get a feeling for how this works create a new Template file in the templates folder, called MyHelloWorldTemplate.xtend (In this example we are using xtend to describe our Templates, but this is not a requirement, you can of course use just plain Java.)
and it should contain the following code:
```java
package com.mycompany.plugin.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class MyHelloWorldTemplate implements IFileTemplate<InformationModel> {

	override getFileName(InformationModel model) {
		return "hello_world.txt"
	}

	override getPath(InformationModel model) {
		return null
	}

	override getContent(InformationModel model, InvocationContext context) {
		// here goes the generated content
		return 'Hello here you should put your message'
	}
}
```
This snippet of code implements the three required functions of the IFileTemplateInterface, which is the interface the generator expects for a template.
The functions getFileName and getPath together return the Path under which the return value of getContent should be saved in the output zip. So there is not much magic at work if you want to add a new template file, but to actually add this file to the generator output you need to add two more lines to the generate function in MyPlatformGenerator.xtend, these two lines register your template file with the generator and that’s pretty much it. 
```java
var myHelloworldTemplate = new GeneratorTaskFromFileTemplate(new MyHelloWorldTemplate())
myHelloworldTemplate.generate(infomodel,context,output)
```

The other two functions getServiceKey and getInfo, are called by the repository. 
getServiceKey returns the unique identifier of your generator and getInfo returns the information used in the UI.
Edit them as you see fit.

You now have a generator which returns a zip file containing two files, one is your hello_world.txt and the other one is the output.txt (which you could get rid of by removing the two lines). If you want to test it out now skip down to [run your generator](#run-your-generator)

But creating only static files is a little bit boring so to show you how easy it is to generate code from the information contained in a Vorto model, we will add some more functionality.
Lets add a markdown file to our project to easily share documentation for our device.
First we again create another Template file like before, but this time we modify the getContent method a little bit.
```java
package com.mycompany.plugin.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class DocumentationMarkdownTemplate implements IFileTemplate<InformationModel> {

	override getFileName(InformationModel model) {
		return "Readme.md"
	}

	override getPath(InformationModel model) {
		return null
	}

	override getContent(InformationModel im, InvocationContext context) {
		'''
            # Information Model *«im.name»*
			
			### Unique Identification
			<table>
				<tr><td>Name:</td><td>«im.name»</td></tr>
				<tr><td>Namespace:</td><td>«im.namespace»</td></tr>
				<tr><td>Version:</td><td>«im.version»</td></tr>
			</table>
			### Description
			«im.description»
        '''
	}
}
```
As you can see this example draws on the information model that is passed to the getContent Function. You can use both parameters as you see fit to generate the content you want.

To test your generator, read the run your generator section.

To get a little bit inspiration on what is possible head over to the [vorto-example repository](https://github.com/eclipse/vorto-examples) and checkout out the community generators.


#### Run your generator
If you used the generator generator, you are in luck, because the generator also create some Dockerfiles for you so after you edited the fields in the config.json you can just run 
```
cd generator-runner
nano docker/config.json
docker-compose up --build
```
in the generator-runner folder.
If you are running behinde a proxy server add a http_proxy argument to your docker-compose.yml like this:
```yaml
version: '3'
services:
  repository:
    image: eclipsevorto/vorto-repo
    ports:
      - "8080:8080"
    volumes:
      - "./docker:/code/config"
    environment:
    - USE_PROXY=1
    networks:
      - backend
  generators:
    depends_on: ["repository"]
    build:
      context: .
      args:
        JAR_FILE: generator-runner-0.0.1-SNAPSHOT-exec.jar
        http_proxy: "http://user:password@your.proxy.com:8080"
    volumes:
      - "./docker:/gen/config"
    networks:
      - backend
networks:
  backend:
    driver: bridge
```

#### Parameters and Generators
It's possible to pass custom parameters to a generator upon invocation. 
To add such parameters you have to pass this information to the repository for rendering, this is why you add a Parameter in the getInfo function like you can see below.
```java
@Override
public GeneratorInfo getInfo() {
	return GeneratorInfo.basicInfo("Eclipse Hono",
	"Generates device code (Arduino, Python, Java) that integrates with Eclipse Hono and Eclipse Ditto.",
	"Eclipse Vorto Team").production()
	.withChoiceConfigurationItem("language", "Device Platform", ChoiceItem.of("Arduino (ESP8266)","Arduino"), ChoiceItem.of("Python (v2)","Python"), ChoiceItem.of("Java","Java"));
}
```
![](docs/repo_generator_parameters.png)


To use this information that the user select later in you generator, you access the context variable like this
```java
context.getConfigurationProperties().getOrDefault("language", "java");
```



#### Using mapping Information

If a mapping key matches your service key, the context will be enriched by the mapping information. So using a servicekey like "ble_gatt" would at the ble_gatt mapping information to the objects that are passed to the generator.

### Share your generator with the community

To share your generator with the community please send us an email([vorto-dev@eclipse.org](mailto:vorto-dev@eclipse.org)), containing the endpoint and the service key and we will be in touch.

