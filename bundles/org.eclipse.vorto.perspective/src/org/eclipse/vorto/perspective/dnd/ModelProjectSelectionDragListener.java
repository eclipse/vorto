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
package org.eclipse.vorto.perspective.dnd;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

public class ModelProjectSelectionDragListener implements DragSourceListener {

	private final TreeViewer viewer;

	public ModelProjectSelectionDragListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		 ISelection selection=viewer.getSelection();
	     LocalSelectionTransfer.getTransfer().setSelection(selection);
	     event.doit=!selection.isEmpty();
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data=LocalSelectionTransfer.getTransfer().getSelection();
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
	}

}
