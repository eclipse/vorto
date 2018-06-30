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
package org.eclipse.vorto.perspective.vorto.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.vorto.perspective.view.AbstractProjectSelectionViewPart;
import org.eclipse.vorto.perspective.view.ModelTreeViewer;
import org.eclipse.vorto.wizard.vorto.VortoProjectWizard;

public class VortoProjectSelectionViewPart extends AbstractProjectSelectionViewPart {

	public static final String PROJECT_SELECT_VIEW_ID = "org.eclipse.vorto.perspective.view.VortoProjectSelectionViewPart";

	@Override
	protected ModelTreeViewer getDataTypeTreeViewer(Composite modelPanel) {
		return new DatatypeTreeViewer(modelPanel, this);
	}

	@Override
	protected ModelTreeViewer getFunctionblockTreeViewer(Composite modelPanel) {
		return new FunctionblockTreeViewer(modelPanel, this);
	}

	@Override
	protected ModelTreeViewer getInfomodelTreeViewer(Composite modelPanel) {
		return new InfomodelTreeViewer(modelPanel, this);
	}

	@Override
	protected String getViewerText() {
		return "Select Vorto Project";
	}

	@Override
	protected String getAddToolTipText() {
		return "Add Vorto Project";
	}

	@Override
	protected String getDeleteToolTipText() {
		return "Delete selected Vorto Project";
	}

	@Override
	protected INewWizard getProjectWizard() {
		return new VortoProjectWizard();
	}

}
