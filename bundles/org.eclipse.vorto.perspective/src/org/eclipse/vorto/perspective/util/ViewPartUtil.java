package org.eclipse.vorto.perspective.util;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class ViewPartUtil {

	public static Object getFirstSelectedObject(final Class<?> selectionClass, SelectionChangedEvent event) {
		if (event.getSelection().isEmpty()) {
			return null;
		}
		
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			if (selection.size() > 0) {
				if (selectionClass.isInstance(selection.getFirstElement())) {
					return selection.getFirstElement();
				}
			}
		}
		
		return null;
	}
}
