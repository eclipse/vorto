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
package org.eclipse.vorto.remoterepository.builder;

import org.eclipse.vorto.remoterepository.service.search.IModelQuery;

/**
 * Interpret query expression with the invocation of ModelQuery methods
 * 
 * Please refer to {@link IModelQuery} for documentation on query language.
 * 
 *
 */
public interface IModelQueryBuilder {
	/**
	 * Given the expression {@code expression}, build an {@link IModelQuery}
	 * 
	 * @param query
	 * @param expression
	 * @return
	 */
	IModelQuery buildFromExpression(IModelQuery query, String expression);
}
