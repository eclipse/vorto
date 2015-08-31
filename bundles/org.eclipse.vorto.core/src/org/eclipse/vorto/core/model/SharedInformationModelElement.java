package org.eclipse.vorto.core.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;

public class SharedInformationModelElement extends AbstractSharedModelElement {

	public SharedInformationModelElement(IModelProject ownerProject, IFile modelFile, Model model) {
		super(ownerProject, modelFile, model);
	}

	@Override
	protected ModelType getPossibleReferenceType() {
		return ModelType.Functionblock;
	}

	@Override
	protected String getImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core/icons/shared_im.png";
	}

	public static boolean appliesTo(Model model) {
		return model instanceof InformationModel;
	}
}
