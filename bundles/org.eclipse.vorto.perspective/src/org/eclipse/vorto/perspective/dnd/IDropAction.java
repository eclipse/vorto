package org.eclipse.vorto.perspective.dnd;

import org.eclipse.vorto.core.model.IModelProject;

public interface IDropAction {
	boolean performDrop(IModelProject receivingProject, Object droppedObject);
}
