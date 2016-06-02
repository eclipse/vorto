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
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class ManifestFileTemplate implements IFileTemplate<IGeneratorProjectContext> {
		
	public override String getContent(IGeneratorProjectContext context,InvocationContext invocationContext) {
		return '''
			Manifest-Version: 1.0
			Bundle-ManifestVersion: 2
			Bundle-Name: «context.generatorName»
			Bundle-SymbolicName: «context.packageName».«context.generatorName.toLowerCase»;singleton:=true
			Bundle-Version: 1.0.0.qualifier
			Require-Bundle: org.eclipse.vorto.core,
			 com.google.guava,
			 org.eclipse.xtext.xbase.lib,
			 org.eclipse.xtend.lib,
			 org.eclipse.xtend.lib.macro,
			 org.eclipse.vorto.codegen
			Bundle-RequiredExecutionEnvironment: JavaSE-1.8
			Bundle-ActivationPolicy: lazy
		'''
	}
	
	override getFileName(IGeneratorProjectContext context) {
		return "MANIFEST.MF";
	}
	
	override getPath(IGeneratorProjectContext context) {
		return "META-INF"
	}
	
}