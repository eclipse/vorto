---
layout: documentation
title: Using a Mapping Model in the Code Generator
---
{% include base.html %}


## Using a Mapping Model in the Code Generator

If the information model's properties cannot be directly used in a code generator, then a mapping file is needed to map information model's properties to the platform-specific values. Vorto mapping model supports mapping Vorto models (Information Model/Function Block/Data Type) to the platform-specific models. Please refer to [Defining an Information Model Mapping]({{base}}/documentation/editors/information-model-mapping.html) for mapping model syntax and more details. 

Here is the rule of thumb for you to determin if you need a mapping model, when you want to add some information to assist code generation, 

1. The information is model-related, but cannot be included in Vorto models; 
2. The information is required by code generator, but cannot be included in code generator; 

Now let's take an example to illustrate the usage of mapping models. We will create a new code generator `FunctionBlockListGenerator` to generate a html page that lists all function blocks used for a information model. Each model will display the name with a http link to Vorto repository if exists; 

In this case, the http link information is related to Vorto models, but it is not an innate character, so we cannot describe it in Vorto models; On the other hand, http link information is required by code generator, but it cannot be fixed in code generator; So a mapping model is a perfect solution here. 

#### Updating the Code Implementation

**Prerequisites**

You have understood how to create a code generator plug-in project (refer to [Creating a Code Generator Plug-in Project](./creating-code-generator-plug-in.html)). 

**Proceed as follows**

1. Create a new code generator plug-in project with name `FunctionBlockListGenerator` (refer to [Creating a Code Generator Plug-in Project](./creating-code-generator-plug-in.html)).
2. Implement the actual code as below:

		package org.eclipse.vorto.example
		
		import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask
		import org.eclipse.vorto.codegen.api.GenerationResultZip
		import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
		import org.eclipse.vorto.codegen.api.IFileTemplate
		import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
		import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
		import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
		
		class FunctionBlockListGenerator implements IVortoCodeGenerator {
		
			override generate(InformationModel infomodel, InvocationContext context) {
				var output = new GenerationResultZip(infomodel,getServiceKey());
				var generator = new ChainedCodeGeneratorTask<InformationModel>();
				generator.addTask(new GeneratorTaskFromFileTemplate(new SampleTemplate()));
				generator.generate(infomodel,context,output);
				return output
			}
			
			public static class SampleTemplate implements IFileTemplate<InformationModel> {
				override getFileName(InformationModel context) {
					return "sample.html"
				}
					
				override getPath(InformationModel context) {
					return "output"
				}
					
				override getContent(InformationModel model, InvocationContext context) {
			   		'''
					<!DOCTYPE html>
					<html>
					<body>
					<h2>FunctionBlockList Code Generator Demo</h2>
					It will list all used function blocks for an information model. 
					<ul>
						<li>«model.name»
							<ul>
						    	«FOR property : model.properties»
									«var mappedElement = context.getMappedElement(property.type,"resources")»
									«var url = mappedElement.getAttributeValue("url", "http://vorto.eclipse.org")»
									<li><a href="«url»">«property.name»</a></li>
						    	«ENDFOR»
						  	</ul>
						</li>
					</ul>
					</body>
					</html>
			   		'''
			    }
			}
			
			override getServiceKey() {
				return "functionblocklist";
			}
		}

#### Creating the Mapping File

**Prerequisites**

You have updated the actual code for the `generate()` method (refer to [Updating the Code Implementation](#updating-the-code-implementation)).

**Proceed as follows**

1. In the **Package Explorer**, select the plug-in project (`FunctionBlockListGenerator` in the example).
2. From the context menu, choose **Run As > Eclipse Application**.  
   A new Eclipse instance is started.
3. Switch to the **Vorto** perspective.
4. In Vorto perspective, right click on any function block (e.g. `Accelerometer`). 
5. From the context menu, choose **New Mapping Model**.  
   ![new mapping model]({{base}}/img/documentation/New_mapping_model_wizard.png)  
6. Switch to the Java perspective.  
   The new file `com.mycompany.mapping_Accelerometer_FunctionBlockList_Mapping_1_0_0.mapping` is generated under the `mappings` folder.  
   ![mapping model generated]({{base}}/img/documentation/Mapping_file_generated.png)  
7. Implement the actual mapping file as follows:

		namespace com.mycompany.mapping
		version 1.0.0
		displayname "Accelerometer_FunctionBlockList_Mapping"
		description "Mapping model for Accelerometer_FunctionBlockList_Mapping"
		using com.bosch.fb.sensors.Accelerometer;1.0.0
		
		functionblockmapping Accelerometer_FunctionBlockList_Mapping {
			targetplatform functionblocklist
			from Accelerometer to resources with {url: "http://vorto.eclipse.org/#/details/com.bosch.fb.sensors/Accelerometer/1.0.0"}
		}

8. Save the mapping file.

#### Testing the Code Generator with the Mapping File

**Prerequisites**

You have created the mapping file (refer to [Creating the Mapping File](#creating-the-mapping-file)).

**Proceed as follows**

1. Switch to the Vorto perspective.
2. Right click on the information model (e.g., `XDK`). 
2. From the context menu, choose **Generate Code > FunctionBlockListGenerator**.  
   A new project with name `XDK_functionblocklist_generated` is generated.
4. Open the file `sample.html` in browser. 
   It shows the list of function blocks under XDK information model, if you click on the `accelerometer` function block, it will bring you to the Accelerometer page in Vorto repository. 
   ![generated code]({{base}}/img/documentation/XDK_functionblocklist_generated_output_sample.png)  

#### Further tasks

Following the same procedures described in [Creating the Mapping File](#creating-the-mapping-file), you can add more mapping files for the rest of function blocks. 
