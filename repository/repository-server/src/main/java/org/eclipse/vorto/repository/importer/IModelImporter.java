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
