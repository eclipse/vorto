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
package org.eclipse.vorto.repository.model;

import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.repository.service.IModelRepository.ContentType;

public interface IModelContent {

	/**
	 * EMF Model object
	 * 
	 * @return the model
	 */
	Model getModel();
	
	/**
	 * format of the model content
	 * @return the type of the model
	 */
	ContentType getType();
	
	/**
	 * actual model content value
	 * @return the context of the model
	 */
	byte[] getContent();
}
