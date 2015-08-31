package org.eclipse.vorto.perspective.dnd.dropvalidator;

import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.perspective.dnd.IDropValidator;

public class ModelTypeValidator implements IDropValidator {

	private ModelType modelType;
	
	public ModelTypeValidator(ModelType modelType) {
		this.modelType = modelType;
	}
	
	@Override
	public boolean allow(IModelProject receivingProject, Object droppedObject) {
		if (receivingProject == null && droppedObject instanceof ModelResource) {
			ModelResource model = (ModelResource) droppedObject;
			return model.getId().getModelType() == modelType;
		}
		return false;
	}

}
