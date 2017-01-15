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
package org.eclipse.vorto.codegen.examples.latex.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.latex.tasks.template.LatexComplexPropertyTemplate;
import org.eclipse.vorto.codegen.examples.latex.tasks.template.LatexEntityTemplate;
import org.eclipse.vorto.codegen.examples.latex.tasks.template.LatexEnumTemplate;
import org.eclipse.vorto.codegen.examples.latex.tasks.template.LatexFBPropertyTemplate;
import org.eclipse.vorto.codegen.examples.latex.tasks.template.LatexFunctionBlockTemplate;
import org.eclipse.vorto.codegen.examples.latex.tasks.template.LatexOperationTemplate;
import org.eclipse.vorto.codegen.examples.latex.tasks.template.LatexSimplePropertyConstraintTemplate;
import org.eclipse.vorto.codegen.examples.latex.tasks.template.LatexSimplePropertyTemplate;
import org.eclipse.vorto.codegen.examples.latex.tasks.template.ReviewInformationModelTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class LatexInformationModelGeneratorTask extends
	AbstractTemplateGeneratorTask<InformationModel> {
	
	private String latexFileExtension;
	private String targetPath;
	
	public LatexInformationModelGeneratorTask (String latexFileExtension, 
			String targetPath) {
		this.latexFileExtension = latexFileExtension;
		this.targetPath = targetPath;
	}
	
	@Override
	public String getFileName(InformationModel im) {
		return im.getName() + latexFileExtension;
	}
	
	@Override
		public String getPath(InformationModel im) {
		return targetPath;
	}
	
	@Override
	public ITemplate<InformationModel> getTemplate() {
		LatexOperationTemplate operationTemplate = new LatexOperationTemplate();
		LatexFBPropertyTemplate fbPropertyTemplate = new LatexFBPropertyTemplate();
		LatexComplexPropertyTemplate complexTemplate = new LatexComplexPropertyTemplate();
		LatexSimplePropertyTemplate simpleTemplate = new LatexSimplePropertyTemplate();
		LatexSimplePropertyConstraintTemplate constraintTemplate = new LatexSimplePropertyConstraintTemplate();
		LatexFunctionBlockTemplate fbTemplate = new LatexFunctionBlockTemplate(constraintTemplate, simpleTemplate, complexTemplate, operationTemplate);
		LatexEntityTemplate entityTemplate = new LatexEntityTemplate(constraintTemplate, simpleTemplate, complexTemplate);
		LatexEnumTemplate enumTemplate = new LatexEnumTemplate();
		
		return new ReviewInformationModelTemplate(fbPropertyTemplate, fbTemplate, entityTemplate, enumTemplate);
	}
}
