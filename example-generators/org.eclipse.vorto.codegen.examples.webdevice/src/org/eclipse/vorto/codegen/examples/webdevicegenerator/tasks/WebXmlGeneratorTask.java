/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks;

import org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.WebXmlTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;

public class WebXmlGeneratorTask extends
		AbstractTemplateGeneratorTask<FunctionblockProperty> {

	private final static String WEB_XML_PATH = "src/main/webapp/WEB-INF";

	@Override
	public String getFileName(final FunctionblockProperty fbProperty) {
		return "web.xml";
	}

	@Override
	public String getPath(final FunctionblockProperty fbProperty) {
		return WEB_XML_PATH;
	}

	@Override
	public ITemplate<FunctionblockProperty> getTemplate() {
		return new WebXmlTemplate();
	}
}
