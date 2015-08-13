package org.eclipse.vorto.perspective.dnd;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

public class ModelRepositoryDragListener implements DragSourceListener {

	private TableViewer viewer = null;
	
	public ModelRepositoryDragListener(TableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		LocalSelectionTransfer.getTransfer().setSelection(selection);
		event.data = selection.getFirstElement();
		event.doit = !selection.isEmpty();
	}

	@Override
	public void dragFinished(DragSourceEvent event) {

	}
}
