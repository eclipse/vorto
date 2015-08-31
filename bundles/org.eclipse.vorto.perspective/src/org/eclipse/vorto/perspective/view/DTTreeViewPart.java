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
package org.eclipse.vorto.perspective.view;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.perspective.dnd.ModelDragListener;
import org.eclipse.vorto.perspective.dnd.ModelDropListenerFactory;

public class DTTreeViewPart extends AbstractTreeViewPart {

	public static final String DT_TREE_VIEW_ID = "org.eclipse.vorto.ui.perspective.dttreeview";

	@Override
	protected void hookListeners() {
		super.hookListeners();

		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer
				.getTransfer() };
		treeViewer.addDragSupport(operations, transferTypes,
				new ModelDragListener(treeViewer));
		treeViewer.addDropSupport(operations, transferTypes,
				ModelDropListenerFactory
						.datatypeViewPartDropListener(treeViewer));
	}

	@Override
	public Set<IModelElement> getContent() {
		return new TreeSet<IModelElement>(ModelProjectServiceFactory
				.getDefault().getProjectsInWorkspace(ModelType.Datatype));
	}
}
