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
2. In the field **Project name**, enter a project name.  
   ![new Code Generator plug-in project]({{base}}/img/documentation/vorto_new_codegen_project.png)
3. Click **Finish**.  
4. The new plug-in projects **org.eclipse.vorto.example.helloworldgenerator** is now generated. All dependencies required by the new project, as well as default classes and configuration files are generated.  

   ![Default Code Generator plug-in project]({{base}}/img/documentation/vorto_codegen_default.png)

## Implementing the Code

**Prerequisites**

You have created a plug-in project. (refer to [Create a New Code Generator Plug-in Project](#creating-a-new-code-generator-plug-in-project)).

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

After packaging and deploying the plug-in in the eclipse plug-ins folder the code can be triggered using the context menu.

**Prerequisites**

* You have created a plug-in project. (refer to [Create a New Code Generator Plug-in Project](#creating-a-new-code-generator-plug-in-project)).
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

