/*******************************************************************************
/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/


package org.eclipse.vorto.remoterepository.service.converter;

import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelView;

/**
 * This is the interface for any converters that can convert {@link ModelContent} 
 * to {@link ModelView}
 * 
 * @author 
 *
 */
public interface IModelConverterService {
	/**
	 * Converts {@code modelContent} to {@link ModelView}
	 * @param modelContent
	 * @return
	 */
	ModelView convert(ModelContent modelContent);
	
	/**
	 * Returns the ModelView given the byte array of the model file
	 * @param modelContent
	 * @return
	 */
	ModelView convertToModelView(byte[] modelFile);
	
	/**
	 * Returns the ModelContent given the byte array of the model file
	 * @param modelContent
	 * @return
	 */
	ModelContent convertToModelContent(byte[] modelFile);
}
