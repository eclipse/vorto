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

package org.eclipse.vorto.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.vorto.perspective.view.DTTreeViewPart;
import org.eclipse.vorto.perspective.view.FBTreeViewPart;
import org.eclipse.vorto.perspective.view.InfoModelTreeViewPart;
import org.eclipse.vorto.perspective.view.ProjectSelectionViewPart;

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

		layout.addView(ProjectSelectionViewPart.PROJECT_SELECT_VIEW_ID, IPageLayout.LEFT, 0.10f,
				layout.getEditorArea());
		/*layout.addView(DTTreeViewPart.DT_TREE_VIEW_ID, IPageLayout.LEFT, 0.20f,
				ProjectSelectionViewPart.PROJECT_SELECT_VIEW_ID);
		layout.addView(FBTreeViewPart.FB_TREE_VIEW_ID, IPageLayout.BOTTOM,
				0.25f, DTTreeViewPart.DT_TREE_VIEW_ID);
		layout.addView(InfoModelTreeViewPart.IM_TREE_VIEW_ID,
				IPageLayout.BOTTOM, 0.50f, FBTreeViewPart.FB_TREE_VIEW_ID);*/
	}
}
