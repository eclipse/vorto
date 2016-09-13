package org.eclipse.vorto.repository.service;

import java.io.InputStream;

import org.eclipse.vorto.repository.model.ModelId;

public interface IRepositoryManager {

	byte[] backup() throws Exception;
	
	void restore(InputStream inputStream) throws Exception;
	
	void removeModel(ModelId modelId);
}
