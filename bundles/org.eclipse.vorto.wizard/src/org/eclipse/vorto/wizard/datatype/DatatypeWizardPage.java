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
package org.eclipse.vorto.wizard.datatype;

import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.wizard.ModelBaseWizardPage;

public class DatatypeWizardPage extends ModelBaseWizardPage {

	private static final String DEFAULT_VERSION = "1.0.0";
	private Datatype datatype;

	protected DatatypeWizardPage(Datatype type, String pageName, IModelProject modelProject) {
		super(pageName,modelProject);
		this.datatype = type;
	}

	@Override
	protected String getGroupTitle() {
		return this.datatype + " Details";
	}

	@Override
	protected String getModelLabel() {
		return this.datatype + " Name:";
	}

	@Override
	protected String getDefaultVersion() {
		return DEFAULT_VERSION;
	}

	@Override
	protected String getDefaultDescription() {
		return "Type for ";
	}

	@Override
	protected String getDefaultModelName() {
		return "New" + this.datatype;
	}

	@Override
	public ModelId getModelId() {
		return getModelId(ModelType.Datatype);
	}

}