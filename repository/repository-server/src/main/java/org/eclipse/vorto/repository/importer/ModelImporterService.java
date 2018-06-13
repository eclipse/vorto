/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
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
