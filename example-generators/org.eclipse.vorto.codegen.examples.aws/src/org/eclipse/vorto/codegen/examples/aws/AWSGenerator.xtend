/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.examples.aws

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask
import org.eclipse.vorto.codegen.api.GenerationResultZip
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.codegen.api.IGeneratedWriter
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException
import org.eclipse.vorto.codegen.examples.aws.templates.alexa.AlexaIndentSchemaTemplate
import org.eclipse.vorto.codegen.examples.aws.templates.alexa.AlexaSkillLambdaTemplate
import org.eclipse.vorto.codegen.examples.aws.templates.alexa.AlexaSlotTypeTemplate
import org.eclipse.vorto.codegen.examples.aws.templates.alexa.AlexaUtterancesTemplate
import org.eclipse.vorto.codegen.examples.aws.templates.shadow.ThingShadowForOperationsTask
import org.eclipse.vorto.codegen.utils.Utils
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 */
class AWSGenerator implements IVortoCodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		var output = new GenerationResultZip(infomodel,getServiceKey());
		var chainedGenerators = new ChainedCodeGeneratorTask<InformationModel>();
		
		chainedGenerators.addTask(new ThingShadowForOperationsTask());
		
		// Adds Generators for Speech to Command using Alexa Skill Service.
		// That way it is possible to update a thing shadow by various speech command variations defined as mapping rules
		chainedGenerators.addTask(new GeneratorTaskFromFileTemplate(new AlexaIndentSchemaTemplate()));
		chainedGenerators.addTask(new GeneratorTaskFromFileTemplate(new AlexaSkillLambdaTemplate()));
		chainedGenerators.addTask(new GeneratorTaskFromFileTemplate(new AlexaUtterancesTemplate()));
		
		chainedGenerators.generate(infomodel,context,output);
		
		generateCustomSlotTypes(infomodel,context,output);
	
		return output
	}
	
	/**
	 * Generates Alexa Custom Slot Types for every Infomodel Enumeration
	 */
	def generateCustomSlotTypes(InformationModel infomodel, InvocationContext context, IGeneratedWriter output) {
		for (FunctionblockProperty fbModel : infomodel.properties) {
			var enums = Utils.getReferencedEnums(fbModel.type.functionblock)
			for (Enum enumeration : enums) {
				var _template = new GeneratorTaskFromFileTemplate(new AlexaSlotTypeTemplate())
				_template.generate(enumeration,context,output)
			}
		}
	}
	
	override getServiceKey() {
		return "aws";
	}
}
