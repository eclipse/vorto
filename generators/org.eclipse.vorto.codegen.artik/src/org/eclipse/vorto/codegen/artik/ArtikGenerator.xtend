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
package org.eclipse.vorto.codegen.artik

import org.eclipse.vorto.codegen.api.GenerationResultZip
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ArtikGenerator implements IVortoCodeGenerator {

	override generate(InformationModel infomodel, InvocationContext context,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		var output = new GenerationResultZip(infomodel,getServiceKey());
		
//		var metaTemplate = new GeneratorTaskFromFileTemplate(new ProtobufMetaTemplate())
//		metaTemplate.generate(infomodel,context,output)
//		
//		var imTemplate = new GeneratorTaskFromFileTemplate(new ProtobufIMTemplate())
//		imTemplate.generate(infomodel,context,output)
//		
//		for (FunctionblockProperty fbProperty : infomodel.properties) {
//			var fbTemplate = new GeneratorTaskFromFileTemplate(new ProtobufFBTemplate())
//			fbTemplate.generate(fbProperty.type,context,output)
//			
//			for (Entity entity : Utils.getReferencedEntities(fbProperty.type.functionblock)) {
//				var entityTemplate = new GeneratorTaskFromFileTemplate(new ProtobufEntityTemplate())
//				entityTemplate.generate(entity,context,output)
//			}
//			for (Enum en : Utils.getReferencedEnums(fbProperty.type.functionblock)) {
//				var enumTemplate = new GeneratorTaskFromFileTemplate(new ProtobufEnumTemplate())
//				enumTemplate.generate(en,context,output)
//			}
//		}
		
		return output
	}
	
	override getServiceKey() {
		return "protobuf";
	}
}
