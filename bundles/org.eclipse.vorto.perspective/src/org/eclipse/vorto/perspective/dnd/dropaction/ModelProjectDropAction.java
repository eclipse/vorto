package org.eclipse.vorto.perspective.dnd.dropaction;

import org.eclipse.vorto.core.model.DatatypeModelProject;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.perspective.dnd.IDropAction;

public class ModelProjectDropAction implements IDropAction {

	@Override
	public boolean performDrop(IModelProject receivingProject,
			Object droppedObject) {
		IModelProject projectToBeDropped = (IModelProject) droppedObject;

		if (projectToBeDropped instanceof DatatypeModelProject
				&& !receivingProject.equals(projectToBeDropped)) {
			receivingProject.addReference(projectToBeDropped);
			ModelProjectServiceFactory.getDefault().save(receivingProject);
			return true;
		}
		
		return false;
	}

}
