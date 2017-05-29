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
package org.eclipse.vorto.codegen.coap.client.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.coap.client.templates.CoAPClientFunctionblockInterfaceTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaClassMethodParameterTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class CoAPClientFunctionblockInterfaceGeneratorTask extends
AbstractTemplateGeneratorTask<FunctionblockModel> {
	
	private String javaFileExtension;
	private String targetPath;
	private String classPackage;
	private String interfacePrefix;
	private String[] imports;
	
	public CoAPClientFunctionblockInterfaceGeneratorTask(String javaFileExtension, 
			String targetPath, 
			String classPackage,
			String interfacePrefix,
			String... imports) {
		super();
		this.javaFileExtension = javaFileExtension;
		this.targetPath = targetPath;
		this.classPackage = classPackage;
		this.interfacePrefix = interfacePrefix;
		this.imports = imports;
	}
	
	@Override
	public String getFileName(FunctionblockModel fbm) {
		return interfacePrefix + fbm.getName() + javaFileExtension;
	}

	@Override
	public String getPath(FunctionblockModel fbm) {
		return targetPath;
	}

	@Override
	public ITemplate<FunctionblockModel> getTemplate() {
		// Configure a field template
		JavaClassFieldTemplate propertyTemplate = new JavaClassFieldTemplate();
		
		// Configure an operation parameter template
		JavaClassMethodParameterTemplate methodParameterTemplate = new JavaClassMethodParameterTemplate();
		
		// Configure and return the functionblock template
		return new CoAPClientFunctionblockInterfaceTemplate(classPackage, interfacePrefix,imports ,propertyTemplate, methodParameterTemplate);
	}

}
