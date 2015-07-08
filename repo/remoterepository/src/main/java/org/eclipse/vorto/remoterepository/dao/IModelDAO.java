/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.remoterepository.dao;

import java.util.Collection;

import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;

/**
 * 
 * The interface for the component that actually knows how the models are stored
 * internally.
 *
 */
public interface IModelDAO {
	/**
	 * Given a modelId, returns the actual byte[] of the underlying
	 * representation
	 * 
	 * @param id
	 * @return
	 */
	ModelContent getModelById(ModelId id);

	/**
	 * Given a modelId, returns whether the model already exists in the
	 * repository
	 * 
	 * @param id
	 * @return
	 */
	boolean exists(ModelId id);

	/**
	 * Returns all the models as byte[] in the underlying representation
	 */
	Collection<ModelContent> getAllModels(ModelType modelType);

	ModelView saveModel(ModelContent modelContent);

}
