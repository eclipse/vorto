package org.eclipse.vorto.repository.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.ModelId;

public class ModelContent {

	private ModelId root = null;

	private Map<ModelId, AbstractModel> models = new HashMap<ModelId, AbstractModel>();

	public ModelId getRoot() {
		return root;
	}

	public void setRoot(ModelId root) {
		this.root = root;
	}

	public Map<ModelId, AbstractModel> getModels() {
		return models;
	}

	public void setModels(Map<ModelId, AbstractModel> models) {
		this.models = models;
	}

	@Override
	public String toString() {
		return "ModelContent [root=" + root + ", models=" + models + "]";
	}
	
	
}
