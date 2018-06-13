package org.eclipse.vorto.repository.importer;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.upload.StagingResult;
import org.eclipse.vorto.repository.core.impl.UserContext;

public interface IModelImporter {
	String getId();
	
	ModelId getModelId(byte[] file, String filename);
	
	boolean canHandle(byte[] file, String filename);
	StagingResult stageModel(byte[] file, String filename, UserContext user);
	
	boolean canHandle(String stagingId);
	CommittedModel commitModel(String stagingId, UserContext userContext);
}
