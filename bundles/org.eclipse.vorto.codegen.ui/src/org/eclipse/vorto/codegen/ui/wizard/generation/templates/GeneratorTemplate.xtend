/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *  
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *  
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
 
 package org.eclipse.vorto.codegen.ui.wizard.generation.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class GeneratorTemplate implements IFileTemplate<IGeneratorProjectContext> {

	public override String getFileName(IGeneratorProjectContext context) {
		return context.generatorName+".xtend";
	}

	public override String getPath(IGeneratorProjectContext context) {
		return "src/"+context.packageFolders;
	}


	public override String getContent(IGeneratorProjectContext context) {
		return '''
			package «context.getPackageName»
			
			import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask
			import org.eclipse.vorto.codegen.api.GenerationResultZip
			import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
			import org.eclipse.vorto.codegen.api.IFileTemplate
			import org.eclipse.vorto.codegen.api.IMappingContext
			import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
			import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
			
			class «context.generatorName» implements IVortoCodeGenerator {
			
				override generate(InformationModel infomodel, IMappingContext mappingContext) {
					var output = new GenerationResultZip(infomodel,getServiceKey());
					var generator = new ChainedCodeGeneratorTask<InformationModel>();
					generator.addTask(new GeneratorTaskFromFileTemplate(new SampleTemplate()));
					generator.generate(infomodel,mappingContext,output);
					return output
				}
				
				public static class SampleTemplate implements IFileTemplate<InformationModel> {
					override getFileName(InformationModel context) {
						return "sample.txt"
					}
						
					override getPath(InformationModel context) {
						return "output"
					}
						
					override getContent(InformationModel context) {
				   		return '//Generated by «context.generatorName»';
				    }
				}
				
				override getServiceKey() {
					return "«context.generatorName.toLowerCase»";
				}
			}
		'''
	}
	
}
