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
package org.eclipse.vorto.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.vorto.perspective.vorto.view.VortoProjectSelectionViewPart;

public class VortoPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
				
		layout.createFolder("left", IPageLayout.LEFT, 0.2f,
				IPageLayout.ID_EDITOR_AREA);
		layout.createFolder("right", IPageLayout.RIGHT, 0.6f,
				IPageLayout.ID_EDITOR_AREA);
		layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f,
				IPageLayout.ID_EDITOR_AREA);
		layout.createFolder("top", IPageLayout.TOP, 0.6f,
				IPageLayout.ID_EDITOR_AREA);

		layout.addView(VortoProjectSelectionViewPart.PROJECT_SELECT_VIEW_ID, IPageLayout.LEFT, 0.20f,
				layout.getEditorArea());
	}
}
