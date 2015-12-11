package org.eclipse.vorto.core.model;

import java.util.Objects;

import org.eclipse.core.resources.IFile;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.service.IModelElementResolver;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.core.service.SharedModelServiceFactory;

public abstract class AbstractSharedModelElement extends AbstractModelElement {

	protected IFile modelFile;
	protected IModelProject ownerProject;
	protected Model model;
	
	public AbstractSharedModelElement(IModelProject ownerProject, IFile modelFile, Model model) {
		this.ownerProject = Objects.requireNonNull(ownerProject);
		this.modelFile = Objects.requireNonNull(modelFile);
		this.model = Objects.requireNonNull(model);
	}

	@Override
	public IFile getModelFile() {
		return modelFile;
	}

	@Override
	public Model getModel() {
		return model;
	}
	
	protected IModelElementResolver[] getResolvers() {
		return new IModelElementResolver[] {
				ModelProjectServiceFactory.getDefault().getWorkspaceProjectResolver(),
				SharedModelServiceFactory.getDefault().getSharedModelResolver(ownerProject)
		};
	}
}
