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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.coap.common.templates.JavaFBOperationParamSetTemplate;
import org.eclipse.vorto.core.api.model.functionblock.Operation;

public class JavaFBOperationParamSetGeneratorTask extends
AbstractTemplateGeneratorTask<Operation> {

	private String className;
	private String javaFileExtension;
	private String path;
	private String classPackage;
	private String[] imports;
	
	public JavaFBOperationParamSetGeneratorTask(
			String className,
			String javaFileExtension,
			String path,
			String classPackage,
			String... imports) {
		this.className = StringUtils.capitalize(className);
		this.javaFileExtension = javaFileExtension;
		this.path = path;
		this.classPackage = classPackage;
		this.imports = imports;
	}
	
	@Override
	public String getFileName(Operation im) {
		return StringUtils.capitalize(className) + javaFileExtension;
	}

	@Override
	public String getPath(Operation im) {
		return path;
	}

	@Override
	public ITemplate<Operation> getTemplate() {		
		return new JavaFBOperationParamSetTemplate(className, classPackage, imports);
	}
}
