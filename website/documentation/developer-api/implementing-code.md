---
layout: documentation
title: Implementing the Code
---
{% include base.html %}


## Implementing the Code

**Prerequisites**

You have created a plug-in project (refer to [Creating a Code Generator Plug-in Project](./creating-code-generator-plug-in.html)).

**Proceed as follows**

Implement the actual code for the `generate()` method for the code to be generated - for example:

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

