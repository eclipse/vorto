/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks

import org.eclipse.vorto.codegen.api.Generated
import org.eclipse.vorto.codegen.api.IGeneratedWriter
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.FbServiceClassTemplate
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class FbImplGeneratorTask extends AbstractGeneratorTask<FunctionblockModel> {
	
	private String SRC_LOC = null;
	public static final String CLASS_POST_FIX = "Service";
	
	new(FbModelWrapper model) {
		super(model)
		this.SRC_LOC = '''com.bosch.« context.functionBlockName.toLowerCase»-service/src/main/java/'''
	}
			
	def getFileName() {
		return context.functionBlockName + CLASS_POST_FIX + ".java";
	}
	
	override generate(FunctionblockModel fbm,InvocationContext mappingContext, IGeneratedWriter outputter) {
		var template = FbServiceClassTemplate.generate(context);
		outputter.write(new Generated(fileName,getLocation(),template));
	}
	
	def getLocation() {
		SRC_LOC + getPackageStructure() + "/internal";
	}
	
	def String getPackageStructure() {
		return context.javaPackageName.toString().replaceAll("\\.", "/");
	}
}
