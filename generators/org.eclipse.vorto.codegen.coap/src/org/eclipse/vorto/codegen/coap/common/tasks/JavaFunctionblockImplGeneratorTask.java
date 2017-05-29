/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.coap.common.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.coap.common.templates.JavaFunctionblockImplTemplate;
import org.eclipse.vorto.codegen.coap.server.templates.CoAPServerPropertyTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaClassMethodParameterTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaClassMethodTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class JavaFunctionblockImplGeneratorTask extends
AbstractTemplateGeneratorTask<FunctionblockModel> {
	
	private String javaFileExtension;
	private String targetPath;
	private String classPackage;
	private String interfacePrefix;
	private String implSuffix;
	private String[] imports;
	
	public JavaFunctionblockImplGeneratorTask(String javaFileExtension, 
			String targetPath, 
			String classPackage,
			String interfacePrefix,
			String implSuffix,
			String... imports) {
		super();
		this.javaFileExtension = javaFileExtension;
		this.targetPath = targetPath;
		this.classPackage = classPackage;
		this.interfacePrefix = interfacePrefix;
		this.implSuffix = implSuffix;
		this.imports = imports;
	}
	
	@Override
	public String getFileName(FunctionblockModel fbm) {
		return fbm.getName()+ implSuffix + javaFileExtension;
	}

	@Override
	public String getPath(FunctionblockModel fbm) {
		return targetPath;
	}

	@Override
	public ITemplate<FunctionblockModel> getTemplate() {
		// Configure a field template
		CoAPServerPropertyTemplate propertyTemplate = new CoAPServerPropertyTemplate();
		
		// Configure an operation parameter template
		JavaClassMethodParameterTemplate methodParameterTemplate = new JavaClassMethodParameterTemplate();
		
		// Configure an operation template
		JavaClassMethodTemplate methodTemplate= new JavaClassMethodTemplate(methodParameterTemplate);
		
		// Configure and return the functionblock template
		return new JavaFunctionblockImplTemplate(classPackage, interfacePrefix, implSuffix,imports ,propertyTemplate, methodTemplate);
	}

}