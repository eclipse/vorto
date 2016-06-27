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

[Runing the Example Code Generator](#running-the-example-code-generator)

## Creating a New Code Generator Plug-in Project

**Prerequisites**

You have started your IDE.

**Proceed as follows**

1. In the main menu, click **File > New > Project > Vorto > Code Generator Project**.  
   The **New Code Generator Project** dialog opens.
2. In the field **Project name**, enter a project name.  
   ![new Code Generator plug-in project]({{base}}/img/documentation/vorto_new_codegen_project.png)
3. Click **Finish**.  
4. The new plug-in projects **org.eclipse.vorto.example.mygenerator** and **org.eclipse.vorto.example.service** are now generated. All dependencies required by the new project, as well as default classes and configuration files are generated.  

   ![Default Code Generator plug-in project]({{base}}/img/documentation/vorto_codegen_default.png)

## Implementing the Code

**Prerequisites**

You have created a plug-in project. (refer to [Create a New Code Generator Plug-in Project](#creating-a-new-code-generator-plug-in-project)).

Implement the actual code under the `generate()` method for the code to be generated - for example:

	package org.eclipse.vorto.example

	import org.eclipse.core.runtime.IProgressMonitor
	import org.eclipse.vorto.codegen.api.ICodeGenerator
	import org.eclipse.vorto.codegen.api.tasks.Generated
	import org.eclipse.vorto.codegen.api.tasks.ICodeGeneratorTask
	import org.eclipse.vorto.codegen.api.tasks.IOutputter
	import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator
	import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

	class MyGenerator implements ICodeGenerator<InformationModel> {

		override generate(InformationModel infomodel, IProgressMonitor monitor) {
			new EclipseProjectGenerator(infomodel.getName()+"Device").addTask(new EmptyJavaGeneratorTask()).javaNature().generate(infomodel,monitor);
		}

		public static class EmptyJavaGeneratorTask implements ICodeGeneratorTask<InformationModel> {
			public override generate(InformationModel infomodel, IOutputter outputter) {
		    	outputter.output(new Generated(infomodel.getName(),null, getContent));
		    }
		    def String getContent() {
		   		return '//My first code generator for vorto';
		    }
		}

		override getName() {
			return "MyGenerator";
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

1. In the **Package Explorer**, select the plug-in project (`MyGenerator` in the example).
2. From the context menu, choose **Run As > Eclipse Application**.
  A new Eclipse instance is started.
3. Switch to the **Vorto** perspective.
4. Create an information model (e.g., `PhilipsHue`) and select it.
5. From the context menu, choose **Generate Code > Invoke MyGenerator**.  
  ![testing demo generator]({{base}}/img/documentation/vorto_invoke_mygenerator.png)  
  A new project with name `PhilipsHueDevice` is generated.  
  ![testing demo generator]({{base}}/img/documentation/vorto_mygenerator_result.png)


## Runing the Example Code Generator

An example code generator is provided for you to understand code generator easier. The implementation convert all function block models in a information model to json format. Please follow below steps to run example code generator.

**Proceed as follows**

1. In the main menu, click **File > New > Project > Vorto > Code Generator Project**.
   The **New Code Generator Project** dialog opens.
2. In the field **Project name**, enter a project name. Also check the option **Add Example Code Generator Project**.  
   ![new Code Generator plug-in project]({{base}}/img/documentation/vorto_new_codegen_project.png)  
   Beside your project, an example code generator project **org.eclipse.vorto.codegen.example** will also be generated.
3. Create an information model (e.g., `PhilipsHue`).
4. Create one or more function block(s) and add it or them to the information model.
5. From the context menu, choose **Generate Code > Invoke JsonGenerator**.  
   ![testing demo generator]({{base}}/img/documentation/vorto_invoke_json_generator.png)  
   A new project with name `PhilipsHueDevice` is generated.  
   ![testing demo generator]({{base}}/img/documentation/vorto_json_generator_result.png)
