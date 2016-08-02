---
layout: documentation
title: Further Information for Utility Classes
---
{% include base.html %}


### Further Information for Utility Classes

`HelloworldGenerator` is a simple demo for creating a code generator, it implements `IVortoCodeGenerator` interface and directly overrides `generate()` method. It is enough for many of simple code generation tasks, but for some more complicated tasks, Vorto project provides a few utility classes to help the user implementing new code generators.

For example:

* `ChainedCodeGeneratorTask` class helps to break down a big complex task to several sub tasks
* `GeneratorTaskFromFileTemplate` class helps to generate code from file template
* `GenerationResultZip` class helps to generate compressed code.

Usually these classes work together to implement a new code generator. Here is the example code in MQTT code generator: 

	public IGenerationResult generate(InformationModel context, InvocationContext invocationContext) {
		GenerationResultZip outputter = new GenerationResultZip(context,getServiceKey());
		for (FunctionblockProperty property : context.getProperties()) {
			ChainedCodeGeneratorTask<FunctionblockModel> generator = new
				ChainedCodeGeneratorTask<FunctionblockModel>();
			
			if (property.getType().getFunctionblock().getStatus() != null) {
				generator.addTask(new GeneratorTaskFromFileTemplate<>(new IClientHandlerTemplate()));
				generator.addTask(new GeneratorTaskFromFileTemplate<>(new MqttConfigurationTemplate()));
			}
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new PomTemplate()));
			generator.generate(property.getType(),invocationContext, outputter);
		}
		return outputter;
	}
