package org.eclipse.vorto.perspective.dnd.dropvalidator;

import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.model.IModelProject;

public class DatatypeValidator extends TargetClassSourceClassValidator {

	public DatatypeValidator(Class<?> targetClass, Class<?> sourceClass) {
		super(targetClass, sourceClass);
	}

	@Override
	public boolean allow(IModelProject receivingProject, Object droppedObject) {
		boolean entityDroppedToEnum = entityDroppedToEnum(receivingProject.getModel(), ((IModelProject) droppedObject).getModel());
		return super.allow(receivingProject, droppedObject) && !entityDroppedToEnum;
	}

	private boolean entityDroppedToEnum(Model targetModel, Model droppedModel) {
		return targetModel instanceof Enum && droppedModel instanceof Entity;
	}
}
