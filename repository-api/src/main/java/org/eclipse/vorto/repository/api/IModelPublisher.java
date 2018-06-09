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
package org.eclipse.vorto.repository.api;

import org.eclipse.vorto.repository.api.upload.ModelPublishException;

public interface IModelPublisher {

	/**
	 * Publishes a single Vorto model to the Repository
	 * @param type model type 
	 * @param content, actual model content
	 * @return modelId of the published model
	 * @throws ModelPublishException if the model cannot be uploaded due to integrity issues etc.
	 */
	ModelId publish(ModelType type, String content) throws ModelPublishException;
	
	/**
	 * Uploads an information Model image
	 * @param modelId
	 * @param imageBas64
	 * @throws ModelPublishException
	 */
	void uploadModelImage(ModelId modelId, String imageBas64) throws ModelPublishException;
	
}
