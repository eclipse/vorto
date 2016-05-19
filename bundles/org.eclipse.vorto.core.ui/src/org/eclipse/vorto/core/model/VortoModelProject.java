package org.eclipse.vorto.core.model;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.internal.model.ModelFileLookupHelper;

public class VortoModelProject implements IModelProject {

	protected IProject project;
	
	protected ModelFileLookupHelper modelLookupHelper;
	
	public VortoModelProject(IProject project) {
		this.project = project;
		this.modelLookupHelper = new ModelFileLookupHelper(project);
	}
	@Override
	public IProject getProject() {
		return null;
	}

	@Override
	public void refresh(IProgressMonitor monitor) {
	}

	@Override
	public void addReference(IModelElement reference) {
	}

	@Override
	public IModelElement getSharedModelReference(ModelId modelId) {
		return null;
	}

	@Override
	public Collection<MappingModel> getMapping(String targetPlatform) {
		return null;
	}

	@Override
	public void save() {
		
	}

	@Override
	public void addMapping(ModelId id, byte[] mappingContent) {
		
	}

	@Override
	public List<IModelElement> getModelElements() {
		return null;
	}

	@Override
	public List<IModelElement> getModelElementsByType(ModelType modelType) {
		return null;
	}

}
