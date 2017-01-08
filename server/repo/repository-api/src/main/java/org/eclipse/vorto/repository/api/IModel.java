package org.eclipse.vorto.repository.api;

import java.util.Collection;

public interface IModel {

	ModelId getId();
	
	ModelType getType();
	
	String getDisplayName();
	
	String getDescription();
	
	Collection<ModelId> getReferences();
}
