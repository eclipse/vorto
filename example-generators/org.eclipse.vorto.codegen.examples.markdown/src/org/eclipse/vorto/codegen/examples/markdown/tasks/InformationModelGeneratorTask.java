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
package org.eclipse.vorto.codegen.examples.markdown.tasks;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.examples.markdown.templates.EntityTemplate;
import org.eclipse.vorto.codegen.examples.markdown.templates.EnumTemplate;
import org.eclipse.vorto.codegen.examples.markdown.templates.FunctionBlockTemplate;
import org.eclipse.vorto.codegen.examples.markdown.templates.InformationModelTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class InformationModelGeneratorTask extends
	AbstractTemplateGeneratorTask<InformationModel> {
	
	private String markdownFileExtension;
	private String targetPath;
	private FunctionBlockTemplate fbTemplate;
	private EntityTemplate entityTemplate;
	private EnumTemplate enumTemplate;
	
	public InformationModelGeneratorTask (String markdownFileExtension, 
			String targetPath, 
			FunctionBlockTemplate fbTemplate,
			EntityTemplate entityTemplate,
			EnumTemplate enumTemplate) {
		this.markdownFileExtension = markdownFileExtension;
		this.targetPath = targetPath;
		this.fbTemplate = fbTemplate;
		this.entityTemplate = entityTemplate;
		this.enumTemplate = enumTemplate;
	}
	
	@Override
	public String getFileName(InformationModel im) {
		return StringUtils.capitalize(im.getName()) + markdownFileExtension;
	}
	
	@Override
		public String getPath(InformationModel im) {
		return targetPath;
	}
	
	@Override
	public ITemplate<InformationModel> getTemplate() {
		return new InformationModelTemplate(this.fbTemplate, this.entityTemplate, this.enumTemplate);
	}
}
