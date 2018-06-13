package org.eclipse.vorto.repository.importer.impl.vorto;

import java.util.List;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;

public class VortoStagingDetails {
	private ModelInfo modelResource;
	private List<ModelId> unresolvedReferences;

	public VortoStagingDetails(ModelInfo model) {
		this.modelResource = model;
	}

	public VortoStagingDetails(ModelInfo model, List<ModelId> missingReferences) {
		this.modelResource = model;
		this.unresolvedReferences = missingReferences;
	}

	public ModelInfo getModelResource() {
		return modelResource;
	}

	public void setModelResource(ModelInfo modelResource) {
		this.modelResource = modelResource;
	}

	public List<ModelId> getUnresolvedReferences() {
		return unresolvedReferences;
	}

	public void setUnresolvedReferences(List<ModelId> unresolvedReferences) {
		this.unresolvedReferences = unresolvedReferences;
	}

}
