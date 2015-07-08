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
package org.eclipse.vorto.remoterepository.internal.builder;

import java.util.Collection;

/**
 * A generic interface for creating criterias from expressions
 * 
 *
 * @param <ExprObject>
 */
interface ExpressionFactory<ExprObject> {

	/**
	 * Return an expression given expression type and a collection of expression
	 * objects
	 * 
	 * @param type
	 * @param operands
	 * @return
	 */
	ExprObject getExpression(String type, Collection<ExprObject> operands);

	/**
	 * Return an expression given an expression type and a value
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	ExprObject getExpression(String type, String value);

	/**
	 * Return an expression given an expression type and an expression object
	 * 
	 * @param type
	 * @param obj
	 * @return
	 */
	ExprObject getExpression(String type, ExprObject obj);
}