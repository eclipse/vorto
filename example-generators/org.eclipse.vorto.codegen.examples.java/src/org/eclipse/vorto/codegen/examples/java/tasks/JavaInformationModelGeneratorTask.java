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
package org.eclipse.vorto.codegen.examples.java.tasks;

import org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.templates.java.JavaFunctionblockPropertyGetterTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaFunctionblockPropertySetterTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaFunctionblockPropertyTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaInformationModelTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class JavaInformationModelGeneratorTask extends
AbstractTemplateGeneratorTask<InformationModel> {
	
	private String javaFileExtension;
	private String targetPath;
	private String classPackage;
	private String interfacePrefix;
	private String implSuffix;
	private String getterPrefix;
	private String setterPrefix;
	private String[] imports;
	
	public JavaInformationModelGeneratorTask(String javaFileExtension, 
			String targetPath, 
			String classPackage,
			String interfacePrefix,
			String implSuffix,
			String getterPrefix,
			String setterPrefix,
			String... imports) {
		super();
		this.javaFileExtension = javaFileExtension;
		this.targetPath = targetPath;
		this.classPackage = classPackage;
		this.interfacePrefix = interfacePrefix;
		this.implSuffix = implSuffix;
		this.getterPrefix = getterPrefix;
		this.setterPrefix = setterPrefix;
		this.imports = imports;
	}
	
	@Override
	public String getFileName(InformationModel im) {
		return im.getName() + javaFileExtension;
	}

	@Override
	public String getPath(InformationModel im) {
		return targetPath;
	}

	@Override
	public ITemplate<InformationModel> getTemplate() {
		// Configure a functionblock property template
		JavaFunctionblockPropertyTemplate propertyTemplate = new JavaFunctionblockPropertyTemplate(interfacePrefix);
		
		// Configure a functionblock property getter template
		JavaFunctionblockPropertyGetterTemplate getterTemplate= new JavaFunctionblockPropertyGetterTemplate(getterPrefix, interfacePrefix);
		
		// Configure a functionblock property setter template
		JavaFunctionblockPropertySetterTemplate setterTemplate= new JavaFunctionblockPropertySetterTemplate(setterPrefix, interfacePrefix);
		
		// Configure and return the information model template
		return new JavaInformationModelTemplate(classPackage, interfacePrefix,implSuffix,imports, propertyTemplate, getterTemplate, setterTemplate);
	}

}
