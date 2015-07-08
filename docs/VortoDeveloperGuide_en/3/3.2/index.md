## Implementing the Code

**Prerequisites**

You have created a plug-in project. (refer to [Create a New Code Generator Plug-in Project](../3.1/index.md)).

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

<table class="note">
  <tr>
    <td class="notesign"><img src="../../images/Note_32.png", alt="Note"></td>
    <td>In this example the code generator is implemented using the Xtend template language.</td>
  </tr>
</table>
