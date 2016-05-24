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
package org.eclipse.vorto.wizard.mapping;

import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.wizard.ModelBaseWizardPage;

public class MappingModellWizardPage extends ModelBaseWizardPage {

	private static final String DEFAULT_VERSION = "1.0.0";
	private static final String DEFAULT_DESCRIPTION = "Mapping model for ";
	private static final String DEFAULT_MAPPINGMODEL_NAME = "NewMapping";

	protected MappingModellWizardPage(String pageName,IModelProject modelProject) {
		super(pageName,modelProject);
	}

	@Override
	protected String getDefaultVersion() {
		return DEFAULT_VERSION;
	}

	@Override
	protected String getDefaultDescription() {
		return DEFAULT_DESCRIPTION;
	}

	@Override
	protected String getDefaultModelName() {
		return DEFAULT_MAPPINGMODEL_NAME;
	}

	@Override
	protected String getGroupTitle() {
		return "Mapping Model Details";
	}

	@Override
	protected String getModelLabel() {
		return "Mapping Model Name:";
	}

}






