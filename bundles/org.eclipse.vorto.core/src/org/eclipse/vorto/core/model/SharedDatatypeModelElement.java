package org.eclipse.vorto.core.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.vorto.core.api.model.model.Model;

import com.sun.org.apache.bcel.internal.generic.Type;

public class SharedDatatypeModelElement extends AbstractSharedModelElement {

	public SharedDatatypeModelElement(IModelProject ownerProject, IFile modelFile, Model model) {
		super(ownerProject, modelFile, model);
	}

	@Override
	protected ModelType getPossibleReferenceType() {
		return ModelType.DATATYPE;
	}

	@Override
	protected String getImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core/icons/shared_dt.png";
	}
	
	public static boolean appliesTo(Model model) {
		return Type.class.isInstance(model);
	}

}
