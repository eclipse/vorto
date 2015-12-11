package org.eclipse.vorto.core.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.model.Model;

public class SharedDatatypeModelElement extends AbstractSharedModelElement {

	public SharedDatatypeModelElement(IModelProject ownerProject, IFile modelFile, Model model) {
		super(ownerProject, modelFile, model);
	}

	@Override
	protected ModelType getPossibleReferenceType() {
		return ModelType.Datatype;
	}

	@Override
	protected String getImageURLAsString() {
		if(model instanceof Entity) 
			return "platform:/plugin/org.eclipse.vorto.core.ui/icons/shared_dt_entity.png";
		else if(model instanceof Enum)
			return "platform:/plugin/org.eclipse.vorto.core.ui/icons/shared_dt_enum.png";
		else 
			return "platform:/plugin/org.eclipse.vorto.core.ui/icons/shared_dt.png";
	}
	
	public static boolean appliesTo(Model model) {
		return model instanceof Type;
	}

}
