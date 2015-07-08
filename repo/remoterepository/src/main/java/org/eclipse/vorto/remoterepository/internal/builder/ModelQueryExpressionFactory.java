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

import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;

/**
 * This is an expression factory that produces ModelQuery objects based on type.
 * 
 *
 */
public class ModelQueryExpressionFactory implements
		ExpressionFactory<IModelQuery> {

	private static final String QUERY_OPERATOR_NOT = "not";
	private static final String QUERY_OPERATOR_AND = "and";
	private static final String QUERY_OPERATOR_OR = "or";
	private static final String QUERY_OPERATOR_NAMESPACE_LIKE = "namespaceLike";
	private static final String QUERY_OPERATOR_NAMESPACE = "namespace";
	private static final String QUERY_OPERATOR_NAME_LIKE = "nameLike";
	private static final String QUERY_OPERATOR_NAME = "name";
	private static final String QUERY_OPERATOR_VERSION_LIKE = "versionLike";
	private static final String QUERY_OPERATOR_VERSION = "version";
	private static final String QUERY_OPERATOR_MODELTYPE = "modelType";

	private IModelQuery parentQuery;

	public ModelQueryExpressionFactory(IModelQuery parentQuery) {
		this.parentQuery = parentQuery;
	}

	/*
	 * Returns a ModelQuery of combination operators
	 */
	public IModelQuery getExpression(String type,
			Collection<IModelQuery> operands) {
		if (type.equals(QUERY_OPERATOR_OR)) {
			return parentQuery.or(operands.toArray(new IModelQuery[operands
					.size()]));
		} else if (type.equals(QUERY_OPERATOR_AND)) {
			return parentQuery.and(operands.toArray(new IModelQuery[operands
					.size()]));
		}

		throw new RuntimeException("Unrecognized operator name : " + type);
	}

	/*
	 * Returns a ModelQuery of attribute (name, namelike, namespace,
	 * namespaceLike, version, versionLike) operators
	 */
	public IModelQuery getExpression(String type, String value) {
		if (type.equals(QUERY_OPERATOR_NAME)) {
			return parentQuery.name(value);
		} else if (type.equals(QUERY_OPERATOR_NAME_LIKE)) {
			return parentQuery.nameLike(value);
		} else if (type.equals(QUERY_OPERATOR_NAMESPACE)) {
			return parentQuery.namespace(value);
		} else if (type.equals(QUERY_OPERATOR_NAMESPACE_LIKE)) {
			return parentQuery.namespaceLike(value);
		} else if (type.equals(QUERY_OPERATOR_VERSION)) {
			return parentQuery.version(value);
		} else if (type.equals(QUERY_OPERATOR_VERSION_LIKE)) {
			return parentQuery.versionLike(value);
		} else if (type.equals(QUERY_OPERATOR_MODELTYPE)) {
			return parentQuery
					.modelType(ModelType.valueOf(value.toUpperCase()));
		}

		throw new RuntimeException("Unrecognized operator name : " + type);
	}

	/*
	 * returns a Negation ModelQuery
	 */
	public IModelQuery getExpression(String type, IModelQuery obj) {
		if (type.equals(QUERY_OPERATOR_NOT)) {
			return parentQuery.not(obj);
		}

		throw new RuntimeException("Unrecognized operator name : " + type);
	}

}
