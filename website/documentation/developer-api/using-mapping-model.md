---
layout: documentation
title: Using a Mapping Model in the Code Generator
---
{% include base.html %}


## Using a Mapping Model in the Code Generator

If the information model's properties cannot be directly used in a code generator, then a mapping file is needed to map information model's properties to the platform specific values. Vorto mapping model supports mapping Vorto models (Information Model/Function Block/Data Type) to the platform-specific models. Please refer to [Defining an Information Model Mapping]({{base}}/documentation/editors/information-model-mapping.html) for mapping model syntax and more details. 

Following the above tutorial, the `HelloWorldGenerator` generator could already generate "Hello World from XDK!". Here, "XDK" is the name of the information model.

If you would like to use a nick name instead of the information model name, and you doesn't want to incorporate this platform-specific nick name into the existing information model, so you can use a mapping model to achieve that purpose.

#### Updating the Code Implementation

**Prerequisites**

You have implemented the code for the code generator plug-in project (refer to [Implementing the Code](./implementing-code.html)).

**Proceed as follows**

1. Stop the testing Eclipse enviroment to come back to the development Eclipse.  
   ![generator_mapping]({{base}}/img/documentation/vorto_generator_mapping.png)
2. Implement the actual code for the `generate()` method for the code to be generated - for example:

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


#### Creating the Mapping File

**Prerequisites**

You have updated the actual code for the `generate()` method (refer to [Updating the Code Implementation](#updating-the-code-implementation)).

**Proceed as follows**

1. In the **Package Explorer**, select the plug-in project (`HelloWorldGenerator` in the example).
2. From the context menu, choose **Run As > Eclipse Application**.  
   A new Eclipse instance is started.
3. Switch to the **Vorto** perspective.
4. In Vorto perspective, right click on recent created information model (e.g. `XDK`). 
5. From the context menu, choose **New Mapping Model**.  
   ![new mapping model]({{base}}/img/documentation/vorto_new_mapping_model.png)  
6. Switch to the Java perspective.  
   The new file `com.mycompany.mapping_XDK_Helloworld_Mapping_1_0_0.mapping` is generated under the `mappings` folder.  
   ![mapping model generated]({{base}}/img/documentation/vorto_mapping_file_generated.png)  
7. Implement the actual mapping file as follows:

		namespace com.mycompany.mapping
		version 1.0.0
		displayname "XDK_Helloworld_Mapping"
		description "Mapping model for XDK_Helloworld_Mapping"
		using com.bosch.demo.xdk.XDK;1.0.0
		
		infomodelmapping XDK_Helloworld_Mapping {
			targetplatform helloworldgenerator
			from XDK to hellofrom with {nickname: "My Secret Weapon"}
		}

8. Save the mapping file.

#### Testing the Code Generator with the Mapping File

**Prerequisites**

You have created the mapping file (refer to [Creating the Mapping File](#creating-the-mapping-file)).

**Proceed as follows**

1. Switch to the Vorto perspective.
2. Right click on the information model (e.g., `XDK`). 
2. From the context menu, choose **Generate Code > HelloWorldGenerator**.  
   A new project with name `XDK_helloworldgenerator_generated` is generated.
4. Open the file `sample.txt`.  
   The file shows `Hello World from My Secret Weapon !`.  
   ![generated code]({{base}}/img/documentation/vorto_generated_with_mapping.png)  
