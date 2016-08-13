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
package org.eclipse.vorto.codegen.examples.coap.client.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.coap.client.templates.CoAPClientFunctionblockTemplate;
import org.eclipse.vorto.codegen.examples.coap.client.templates.CoAPClientOperationTemplate;
import org.eclipse.vorto.codegen.examples.coap.client.templates.CoAPClientPropertyTemplate;
import org.eclipse.vorto.codegen.examples.templates.java.JavaClassMethodParameterTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class CoAPClientFunctionblockImplGeneratorTask extends
AbstractTemplateGeneratorTask<FunctionblockModel> {
	
	private String javaFileExtension;
	private String targetPath;
	private String classPackage;
	private String interfacePrefix;
	private String implSuffix;
	private String paramSetSuffix;
	private String primitiveTypeSuffix;
	private String[] imports;
	
	public CoAPClientFunctionblockImplGeneratorTask(String javaFileExtension, 
			String targetPath, 
			String classPackage,
			String interfacePrefix,
			String implSuffix,
			String paramSetSuffix,
			String primitiveTypeSuffix,
			String... imports) {
		super();
		this.javaFileExtension = javaFileExtension;
		this.targetPath = targetPath;
		this.classPackage = classPackage;
		this.interfacePrefix = interfacePrefix;
		this.implSuffix = implSuffix;
		this.paramSetSuffix = paramSetSuffix;
		this.primitiveTypeSuffix = primitiveTypeSuffix;
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
		CoAPClientPropertyTemplate propertyTemplate = new CoAPClientPropertyTemplate(primitiveTypeSuffix, paramSetSuffix);
		
		// Configure an operation parameter template
		JavaClassMethodParameterTemplate methodParameterTemplate = new JavaClassMethodParameterTemplate();
		
		// Configure an operation template
		CoAPClientOperationTemplate methodTemplate= new CoAPClientOperationTemplate(primitiveTypeSuffix, paramSetSuffix, methodParameterTemplate);
		
		// Configure and return the functionblock template
		return new CoAPClientFunctionblockTemplate(classPackage, interfacePrefix, implSuffix,imports ,propertyTemplate, methodTemplate);
	}

}