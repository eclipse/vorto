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

import java.util.ArrayList
import java.util.List
import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class ManifestFileTemplate implements ITemplate<IGeneratorProjectContext> {
	
	List<String> requiredBundles = new ArrayList<String>();
	List<String> importPackages = new ArrayList<String>();
	
	def ManifestFileTemplate setBundles(List<String> requiredBundles) {
		this.requiredBundles = requiredBundles;
		return this;
	}
	
	def ManifestFileTemplate setPackages(List<String> importPackages) {
		this.importPackages = importPackages;
		return this;
	}
	
	public override String getContent(IGeneratorProjectContext metaData) {
		return '''
			Manifest-Version: 1.0
			Bundle-ManifestVersion: 2
			Bundle-Name: «metaData.projectName»
			Bundle-SymbolicName: «metaData.projectName»;singleton:=true
			Bundle-Version: 1.0.0.qualifier
			Bundle-Activator: «metaData.packageName».Activator
			«IF !requiredBundles.isEmpty»
			Require-Bundle:«FOR iterator : requiredBundles» «iterator.toString»«IF !requiredBundles.indexOf(iterator).equals(requiredBundles.size - 1)»,
			«ELSE»
			
			«ENDIF»«ENDFOR»«ENDIF»
			Bundle-RequiredExecutionEnvironment: JavaSE-1.7
			Bundle-ActivationPolicy: lazy
			«IF !importPackages.isEmpty»
			Import-Package:«FOR iterator : importPackages» «iterator.toString»«IF !importPackages.indexOf(iterator).equals(importPackages.size - 1)»,
			«ELSE»
			
			«ENDIF»«ENDFOR»«ENDIF»
		'''
	}
	
}