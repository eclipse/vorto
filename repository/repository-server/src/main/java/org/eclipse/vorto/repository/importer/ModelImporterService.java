package org.eclipse.vorto.repository.importer;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.vorto.repository.api.upload.StagingResult;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelImporterService {
	
	@Autowired
	private List<IModelImporter> modelImporters;
	
	/*
	 * This needs to be improved to handle .zip files
	 */
	public List<StagingResult> stageFile(String filename, byte[] content, UserContext userContext) {
		return modelImporters.stream()
			.filter(importer -> importer.canHandle(content, filename))
			.map(importer -> importer.stageModel(content, filename, userContext))
			.collect(Collectors.toList());
	}
	
	public CommittedModel commitStagedFile(String stagingId, UserContext userContext) {
		return modelImporters.stream()
			.filter(importer -> importer.canHandle(stagingId))
			.findFirst()
			.map(importer -> importer.commitModel(stagingId, userContext))
			.orElseThrow(() -> new ModelImporterException("Can't find importer."));
	}
	
}
