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
package org.eclipse.vorto.codegen.examples.coap.server.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.coap.server.templates.CoAPServerIMRequestHandlerTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class CoAPServerIMRequestHandlerGeneratorTask extends
AbstractTemplateGeneratorTask<InformationModel> {
	
	private String className;
	private String interfacePrefix;
	private String javaFileExtension;
	private String path;
	private String classPackage;
	private String primitiveTypeWrapper_suffix;
	private String[] imports;
	
	public CoAPServerIMRequestHandlerGeneratorTask(
			String className,
			String javaFileExtension,
			String path,
			String classPackage,
			String interfacePrefix, 
			String primitiveTypeWrapper_suffix,
			String... imports) {
		this.className = className;
		this.javaFileExtension = javaFileExtension;
		this.path = path;
		this.classPackage = classPackage;
		this.interfacePrefix = interfacePrefix;
		this.primitiveTypeWrapper_suffix = primitiveTypeWrapper_suffix;
		this.imports = imports;
	}
	
	@Override
	public String getFileName(InformationModel im) {
		return className + javaFileExtension;
	}

	@Override
	public String getPath(InformationModel im) {
		return path;
	}

	@Override
	public ITemplate<InformationModel> getTemplate() {
		return new CoAPServerIMRequestHandlerTemplate(classPackage, className, interfacePrefix + className,interfacePrefix, primitiveTypeWrapper_suffix,imports);
	}

}
