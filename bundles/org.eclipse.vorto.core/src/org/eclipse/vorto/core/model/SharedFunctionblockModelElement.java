package org.eclipse.vorto.core.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.model.Model;

public class SharedFunctionblockModelElement extends AbstractSharedModelElement {

	public SharedFunctionblockModelElement(IModelProject ownerProject, IFile modelFile, Model model) {
		super(ownerProject, modelFile, model);
	}

	@Override
	protected ModelType getPossibleReferenceType() {
		return ModelType.Datatype;
	}

	@Override
	protected String getImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core/icons/shared_fb.png";
	}

	public static boolean appliesTo(Model model) {
		return FunctionblockModel.class.isInstance(model);
	}
}
