---
layout: documentation
title: Implementing a Code Generator
---
{% include base.html %}
## Implementing a Code Generator

This section details the following topics:

[Creating a New Code Generator Plug-in Project](#creating-a-new-code-generator-plug-in-project)

[Implementing the Code](#implementing-the-code)

[Testing the New Code Generator](#testing-the-new-code-generator)


## Creating a New Code Generator Plug-in Project

**Prerequisites**

You have started your IDE.

**Proceed as follows**

1. In the main menu, click **File > New > Project > Vorto > Code Generator Project**.  
   The **New Code Generator Project** dialog opens.
2. In the field **Project name**, enter a project name, for example, `HelloWorldGenerator`.  
   ![new Code Generator plug-in project]({{base}}/img/documentation/vorto_new_codegen_project.png)
3. Click **Finish**.  
   The new plug-in project **org.eclipse.vorto.example.helloworldgenerator** is generated. All dependencies required by the new project, as well as default classes and configuration files are generated.  
   ![Default Code Generator plug-in project]({{base}}/img/documentation/vorto_codegen_default.png)

## Implementing the Code

**Prerequisites**

You have created a plug-in project (refer to [Create a New Code Generator Plug-in Project](#creating-a-new-code-generator-plug-in-project)).

Implement the actual code under the `generate()` method for the code to be generated - for example:

	package org.eclipse.vorto.example
	
	import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask
	import org.eclipse.vorto.codegen.api.GenerationResultZip
	import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
	import org.eclipse.vorto.codegen.api.IFileTemplate
	import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
	import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
	import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
	
	class HelloWorldGenerator implements IVortoCodeGenerator {
	
		override generate(InformationModel infomodel, InvocationContext context) {
			var output = new GenerationResultZip(infomodel,getServiceKey());
			var generator = new ChainedCodeGeneratorTask<InformationModel>();
			generator.addTask(new GeneratorTaskFromFileTemplate(new SampleTemplate()));
			generator.generate(infomodel,context,output);
			return output
		}
		
		public static class SampleTemplate implements IFileTemplate<InformationModel> {
			override getFileName(InformationModel context) {
				return "sample.txt"
			}
				
			override getPath(InformationModel context) {
				return "output"
			}
				
			override getContent(InformationModel model, InvocationContext context) {
		   		return '''Hello World from information model «model.name» !''';
		    }
		}
		
		override getServiceKey() {
			return "helloworldgenerator";
		}
	}


<table class="table table-bordered">
	<tbody>
 <tr>
   <td><i class="fa fa-info-circle info-note"></i></td>
   <td>In this example the code generator is implemented using the Xtend template language.</td>
 </tr>
 </tbody>
</table>


## Testing the New Code Generator

After packaging and deploying the plug-in in the eclipse plug-ins folder, the code can be triggered using the context menu.

**Prerequisites**

* You have created a plug-in project (refer to [Create a New Code Generator Plug-in Project](#creating-a-new-code-generator-plug-in-project)).
* You have implemented the code for the plug-in project (refer to [Implementing the Code](#implementing-the-code)).

**Proceed as follows**

1. In the **Package Explorer**, select the plug-in project (`HelloWorldGenerator` in the example).
2. From the context menu, choose **Run As > Eclipse Application**.  
   A new Eclipse instance is started.
3. Switch to the **Vorto** perspective.
4. Create a new Vorto Project.
5. Create an information model or download an existing information model from vorto repository (e.g., `XDK`) and select it.
6. From the context menu, choose **Generate Code > HelloWorldGenerator**.  
   ![testing demo generator]({{base}}/img/documentation/vorto_invoke_HelloWorldGenerator.png)  
   A new project with name `XDK_helloworldgenerator_generated` is generated.  
   ![testing demo generator]({{base}}/img/documentation/vorto_HelloWorldGenerator_result.png)
7. Open the file `sample.txt`, it shows "Hello world from information model XDK !".


This example code generator is provided for you to understand code generator easier. There are already some predefined code generators included in our code base, please see the list [here](https://github.com/eclipse/vorto/blob/development/server/generators/Readme.md). 

## Using mapping model in a code generator

If the information model's properties cannot be directly used in a code generator, then a mapping file is needed to map information model's property to the other platform specific value. Vorto mapping model supports mapping Vorto models (Information Model/Function Block/Data Type) to the platform-specific models. Please refer to here(link to be added) for mapping model syntax and more details. 

Following the above tutorial, the helloworld generator could already generate "Hello World from XDK !", here "XDK" is the name of the information model. 
Now, let's say, if user would like to use a nick name instead of information model name, and user doen't want to incorporate this platform-specific nick name into the existing information model, so we can use mapping model to acheive that purpose. 

**Proceed as follows**

* Stop the testing enviroment, and come back to development eclipse. 

![generator_mapping]({{base}}/img/documentation/vorto_generator_mapping.png)

Implement the actual code under the `generate()` method for the code to be generated - for example:

		package org.eclipse.vorto.example
		
		import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask
		import org.eclipse.vorto.codegen.api.GenerationResultZip
		import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
		import org.eclipse.vorto.codegen.api.IFileTemplate
		import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
		import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
		import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
		
		class HelloWorldGenerator implements IVortoCodeGenerator {
		
			override generate(InformationModel infomodel, InvocationContext context) {
				var output = new GenerationResultZip(infomodel,getServiceKey());
				var generator = new ChainedCodeGeneratorTask<InformationModel>();
				generator.addTask(new GeneratorTaskFromFileTemplate(new SampleTemplate()));
				generator.generate(infomodel,context,output);
				return output
			}
			
			public static class SampleTemplate implements IFileTemplate<InformationModel> {
				override getFileName(InformationModel context) {
					return "sample.txt"
				}
					
				override getPath(InformationModel context) {
					return "output"
				}
					
				override getContent(InformationModel model, InvocationContext context) {
					'''
					«var mappedElement = context.getMappedElement(model,"hellofrom")»
					«var nickname = mappedElement.getAttributeValue("nickname", "")»
					Hello World from «nickname»
					'''
			    }
			}
			
			override getServiceKey() {
				return "helloworldgenerator";
			}
		}

## Creating mapping file

1. In the **Package Explorer**, select the plug-in project (`HelloWorldGenerator` in the example).
2. From the context menu, choose **Run As > Eclipse Application**.
  A new Eclipse instance is started, Switch to the **Vorto** perspective.
3. In Vorto perspective, right click on recent created information model (e.g. `XDK`). 
4. From the context menu, choose **New Mapping Model**. 

  ![new mapping model]({{base}}/img/documentation/vorto_new_mapping_model.png)  

  Switch to Java perspective, a new file with name `com.mycompany.mapping_XDK_Helloworld_Mapping_1_0_0.mapping` is generated under mappings folder. 

  ![mapping model generated]({{base}}/img/documentation/vorto_mapping_file_generated.png)  
5. Implement the actual code like below, save it. 

		namespace com.mycompany.mapping
		version 1.0.0
		displayname "XDK_Helloworld_Mapping"
		description "Mapping model for XDK_Helloworld_Mapping"
		using com.bosch.demo.xdk.XDK;1.0.0
		
		infomodelmapping XDK_Helloworld_Mapping {
			targetplatform helloworldgenerator
			from XDK to hellofrom with {nickname: "My Secret Weapon"}
		}

## Testing the New Code Generator with mapping file

1. Switch to Vorto perspective, right click on information model (e.g. `XDK`). 
2. From the context menu, choose **Generate Code > HelloWorldGenerator**.  
3. A new project with name `XDK_helloworldgenerator_generated` is generated.  
4. Open the file `sample.txt`, it shows "Hello World from My Secret Weapon !" now.
  ![generated code]({{base}}/img/documentation/vorto_generated_with_mapping.png)  

## Futher information for utility classes

HelloworldGenerator is a simple demo for creating a code generator, it implements `IVortoCodeGenerator` interface and directly overrides `generate()` methord. It is enough for many of simple code generation tasks, but for some more complicated tasks, Vorto project provides a few utility classes to help user implement new code generators. For example, `ChainedCodeGeneratorTask` class helps to break down a big complex task to several sub tasks, `GeneratorTaskFromFileTemplate` class helps to generate code from file template, and `GenerationResultZip` class helps to generate compressed code, usually these classes work together to implement a new code generator, here is the example code in MQTT code generator: 

	public IGenerationResult generate(InformationModel context, InvocationContext invocationContext) {
		GenerationResultZip outputter = new GenerationResultZip(context,getServiceKey());
		for (FunctionblockProperty property : context.getProperties()) {			
			ChainedCodeGeneratorTask<FunctionblockModel> generator = new ChainedCodeGeneratorTask<FunctionblockModel>();
			
			if (property.getType().getFunctionblock().getStatus() != null) {
				generator.addTask(new GeneratorTaskFromFileTemplate<>(new IClientHandlerTemplate()));
				generator.addTask(new GeneratorTaskFromFileTemplate<>(new MqttConfigurationTemplate()));
			}
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new PomTemplate()));
			generator.generate(property.getType(),invocationContext, outputter);
		}
		return outputter;
	}

