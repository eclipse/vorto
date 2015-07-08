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
package org.eclipse.vorto.remoterepository.internal.search;

import org.eclipse.vorto.remoterepository.Constants;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;

public abstract class AbstractModelQuery<Criteria> implements IModelQuery {

	@Override
	public IModelQuery modelType(ModelType modelType) {
		return mapCriteriatoModelQuery(createTextCriteria(Constants.INDEX_FIELD_MODEL_TYPE, Operator.EQUALS, modelType.name()));
	}

	@Override
	public IModelQuery name(String modelName) {
		return mapCriteriatoModelQuery(createTextCriteria(Constants.INDEX_FIELD_MODEL_NAME, Operator.EQUALS, modelName));
	}

	@Override
	public IModelQuery nameLike(String modelName) {
		return mapCriteriatoModelQuery(createTextCriteria(Constants.INDEX_FIELD_MODEL_NAME, Operator.LIKE, modelName));
	}

	@Override
	public IModelQuery namespace(String namespace) {
		return mapCriteriatoModelQuery(createTextCriteria(Constants.INDEX_FIELD_MODEL_NAMESPACE, Operator.EQUALS, namespace));
	}

	@Override
	public IModelQuery namespaceLike(String namespace) {
		return mapCriteriatoModelQuery(createTextCriteria(Constants.INDEX_FIELD_MODEL_NAMESPACE, Operator.LIKE, namespace));
	}
	
	@Override
	public IModelQuery version(String version) {
		return mapCriteriatoModelQuery(createTextCriteria(Constants.INDEX_FIELD_MODEL_VERSION, Operator.EQUALS, version));
	}

	@Override
	public IModelQuery versionLike(String version) {
		return mapCriteriatoModelQuery(createTextCriteria(Constants.INDEX_FIELD_MODEL_VERSION, Operator.LIKE, version));
	}

	@Override
	public IModelQuery and(IModelQuery... queries) {
		return mapCriteriatoModelQuery(createCriteria(Operator.AND, queries));
	}

	@Override
	public IModelQuery or(IModelQuery... queries) {
		return mapCriteriatoModelQuery(createCriteria(Operator.OR, queries));
	}

	@Override
	public IModelQuery not(IModelQuery query) {
		return mapCriteriatoModelQuery(createCriteria(Operator.NOT, query));
	}
	
	/*
	 * This should return the payload this ModelQuery is carrying.
	 */
	abstract protected Criteria getCriteria();
	
	/*
	 * This should create a payload query that handles combination queries (AND, OR, NOT)
	 */
	abstract protected Criteria createCriteria(Operator operator, IModelQuery ... queries);
	
	/*
	 * This should create a payload query that returns actual attribute searches like name, nameLike .. etc
	 */
	abstract protected Criteria createTextCriteria(String name, Operator like, String value);
	
	/*
	 * This should create a new instance of the implementing class with the new payload criteria
	 */
	abstract protected IModelQuery newQuery(Criteria criteria);
	
	protected IModelQuery mapCriteriatoModelQuery(Criteria criteria) {
		if (getCriteria() == null) {
			return newQuery(criteria);
		} else {
			return newQuery(createCriteria(Operator.AND, this, newQuery(criteria)));
		}
	}
}
