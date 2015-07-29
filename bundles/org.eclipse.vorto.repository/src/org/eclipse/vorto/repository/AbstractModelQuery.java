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
package org.eclipse.vorto.repository;

import org.eclipse.vorto.core.api.repository.IModelQuery;
import org.eclipse.vorto.core.model.ModelType;

public abstract class AbstractModelQuery<GenericQueryPayload> implements IModelQuery {

	private static final String QUERY_VERSION = "version";
	private static final String QUERY_NAMESPACE = "namespace";
	private static final String QUERY_NAME = "name";
	private static final String QUERY_MODEL_TYPE = "modelType";

	@Override
	public IModelQuery modelType(ModelType modelType) {
		return returnNewReferenceWithQuery(createQuery(QUERY_MODEL_TYPE, Operator.EQUALS, modelType.name().toLowerCase()));
	}

	@Override
	public IModelQuery name(String modelName) {
		return returnNewReferenceWithQuery(createQuery(QUERY_NAME, Operator.EQUALS, modelName));
	}

	@Override
	public IModelQuery nameLike(String modelName) {
		return returnNewReferenceWithQuery(createQuery(QUERY_NAME, Operator.LIKE, modelName));
	}

	@Override
	public IModelQuery namespace(String namespace) {
		return returnNewReferenceWithQuery(createQuery(QUERY_NAMESPACE, Operator.EQUALS, namespace));
	}

	@Override
	public IModelQuery namespaceLike(String namespace) {
		return returnNewReferenceWithQuery(createQuery(QUERY_NAMESPACE, Operator.LIKE, namespace));
	}
	
	@Override
	public IModelQuery version(String version) {
		return returnNewReferenceWithQuery(createQuery(QUERY_VERSION, Operator.EQUALS, version));
	}

	@Override
	public IModelQuery versionLike(String version) {
		return returnNewReferenceWithQuery(createQuery(QUERY_VERSION, Operator.LIKE, version));
	}

	@Override
	public IModelQuery and(IModelQuery... queries) {
		return returnNewReferenceWithQuery(createQuery(Operator.AND, queries));
	}

	@Override
	public IModelQuery or(IModelQuery... queries) {
		return returnNewReferenceWithQuery(createQuery(Operator.OR, queries));
	}

	@Override
	public IModelQuery not(IModelQuery query) {
		return returnNewReferenceWithQuery(createQuery(Operator.NOT, query));
	}
	
	/*
	 * This should return the payload this IModelQuery is carrying.
	 */
	abstract protected GenericQueryPayload getQuery();
	
	/*
	 * This should create a payload query that handles combination queries (AND, OR, NOT)
	 */
	abstract protected GenericQueryPayload createQuery(Operator operator, IModelQuery ... queries);
	
	/*
	 * This should create a payload query that returns actual attribute searches like name, nameLike .. etc
	 */
	abstract protected GenericQueryPayload createQuery(String name, Operator like, String value);
	
	/*
	 * This should create a new instance of the implementing class with the new payload query
	 */
	abstract protected IModelQuery newQuery(GenericQueryPayload query);
	
	protected IModelQuery returnNewReferenceWithQuery(GenericQueryPayload query) {
		if (getQuery() == null) {
			return newQuery(query);
		} else {
			return newQuery(createQuery(Operator.AND, this, newQuery(query)));
		}
	}
}
