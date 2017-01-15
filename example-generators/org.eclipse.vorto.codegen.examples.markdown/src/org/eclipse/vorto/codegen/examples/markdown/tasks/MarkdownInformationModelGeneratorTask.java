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
package org.eclipse.vorto.codegen.examples.markdown.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.markdown.templates.MarkdownEntityTemplate;
import org.eclipse.vorto.codegen.examples.markdown.templates.MarkdownEnumTemplate;
import org.eclipse.vorto.codegen.examples.markdown.templates.MarkdownFunctionBlockTemplate;
import org.eclipse.vorto.codegen.examples.markdown.templates.MarkdownInformationModelTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class MarkdownInformationModelGeneratorTask extends
	AbstractTemplateGeneratorTask<InformationModel> {
	
	private String markdownFileExtension;
	private String targetPath;
	
	public MarkdownInformationModelGeneratorTask (String markdownFileExtension, 
			String targetPath) {
		this.markdownFileExtension = markdownFileExtension;
		this.targetPath = targetPath;
	}
	
	@Override
	public String getFileName(InformationModel im) {
		return im.getName() + markdownFileExtension;
	}
	
	@Override
		public String getPath(InformationModel im) {
		return targetPath;
	}
	
	@Override
	public ITemplate<InformationModel> getTemplate() {
		MarkdownFunctionBlockTemplate fbTemplate = new MarkdownFunctionBlockTemplate();
		MarkdownEntityTemplate entityTemplate = new MarkdownEntityTemplate();
		MarkdownEnumTemplate enumTemplate = new MarkdownEnumTemplate();
		return new MarkdownInformationModelTemplate(fbTemplate, entityTemplate, enumTemplate);
	}

	public String getPlatform() {
		return "markdown";
	}
}
