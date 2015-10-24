package org.eclipse.vorto.repository.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vorto.repository.model.ModelId;

public class ModelReferentialIntegrityException extends
		ModelRepositoryException {
	
	private List<ModelId> referencedBy = new ArrayList<>();

	public ModelReferentialIntegrityException(String msg, List<ModelId> referencedBy) {
		super(msg);
		this.referencedBy = referencedBy;
	}
	
	public List<ModelId> getReferencedBy() {
		return referencedBy;
	}

}
