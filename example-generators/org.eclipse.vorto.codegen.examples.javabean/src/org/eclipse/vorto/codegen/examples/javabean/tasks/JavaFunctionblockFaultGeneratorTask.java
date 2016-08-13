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
package org.eclipse.vorto.codegen.examples.javabean.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.javabean.tasks.template.FunctionblockFaultTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class JavaFunctionblockFaultGeneratorTask extends AbstractTemplateGeneratorTask<FunctionblockModel> {
	
	private String javaFileExtension;
	private String targetPath;
	private String implSuffix;
	private String[] imports;
	private String classPackage;
	
	public JavaFunctionblockFaultGeneratorTask(String javaFileExtension, 
			String targetPath, 
			String classPackage,
			String interfacePrefix,
			String implSuffix,
			String... imports) {
		super();
		this.classPackage = classPackage;
		this.javaFileExtension = javaFileExtension;
		this.targetPath = targetPath;
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
		return new FunctionblockFaultTemplate(imports,implSuffix, classPackage);
	}
}
