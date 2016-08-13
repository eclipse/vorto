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
package org.eclipse.vorto.codegen.examples.coap.common.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.templates.java.JavaFunctionblockPropertyGetterDeclarationTemplate;
import org.eclipse.vorto.codegen.examples.templates.java.JavaFunctionblockPropertySetterDeclarationTemplate;
import org.eclipse.vorto.codegen.examples.templates.java.JavaInformationModelInterfaceTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class JavaInformationModelInterfaceGeneratorTask extends
AbstractTemplateGeneratorTask<InformationModel> {
	
	private String javaFileExtension;
	private String targetPath;
	private String classPackage;
	private String interfacePrefix;
	private String getterPrefix;
	private String setterPrefix;
	private String[] imports;
	
	public JavaInformationModelInterfaceGeneratorTask(String javaFileExtension, 
			String targetPath, 
			String classPackage,
			String interfacePrefix,
			String getterPrefix,
			String setterPrefix,
			String... imports) {
		super();
		this.javaFileExtension = javaFileExtension;
		this.targetPath = targetPath;
		this.classPackage = classPackage;
		this.interfacePrefix = interfacePrefix;
		this.getterPrefix = getterPrefix;
		this.setterPrefix = setterPrefix;
		this.imports = imports;
	}
	
	@Override
	public String getFileName(InformationModel im) {
		return interfacePrefix + im.getName() + javaFileExtension;
	}

	@Override
	public String getPath(InformationModel im) {
		return targetPath;
	}

	@Override
	public ITemplate<InformationModel> getTemplate() {
		// Configure a functionblock property getter template
		JavaFunctionblockPropertyGetterDeclarationTemplate getterTemplate= new JavaFunctionblockPropertyGetterDeclarationTemplate(getterPrefix, interfacePrefix);
		
		// Configure a functionblock property setter template
		JavaFunctionblockPropertySetterDeclarationTemplate setterTemplate= new JavaFunctionblockPropertySetterDeclarationTemplate(setterPrefix, interfacePrefix);
		
		// Configure and return the information model template
		return new JavaInformationModelInterfaceTemplate(classPackage, interfacePrefix,imports, getterTemplate, setterTemplate);
	}

}
