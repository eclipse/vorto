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
package org.eclipse.vorto.remoterepository.builder.mockmodelquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.vorto.remoterepository.dao.IModelDAO;
import org.eclipse.vorto.remoterepository.model.ModelFactory;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;

/**
 * 
 * @deprecated only for testing purpose
 */
public class PredicateModelQuery implements IModelQuery {

	private Predicate<ModelView> predicate = null;

	private ModelType modelType;

	private IModelDAO modelDao;

	public PredicateModelQuery(IModelDAO modelDao, ModelType type) {
		this.modelDao = modelDao;
		this.modelType = type;
	}

	private PredicateModelQuery(Predicate<ModelView> predicate,
			IModelDAO modelDao, ModelType modelType) {
		this(modelDao, modelType);
		this.predicate = predicate;
	}

	public IModelQuery name(final String param) {
		return setPredicateAndReturnQuery(Predicates.name(param));
	}

	public IModelQuery nameLike(String param) {
		return setPredicateAndReturnQuery(Predicates.nameLike(param));
	}

	public IModelQuery namespace(String param) {
		return setPredicateAndReturnQuery(Predicates.namespace(param));
	}

	public IModelQuery namespaceLike(String param) {
		return setPredicateAndReturnQuery(Predicates.namespaceLike(param));
	}

	public IModelQuery version(String version) {
		return setPredicateAndReturnQuery(Predicates.version(version));
	}

	public IModelQuery versionLike(String version) {
		return setPredicateAndReturnQuery(Predicates.versionLike(version));
	}

	@Override
	public IModelQuery modelType(ModelType modelType) {
		return null;
	}

	public Collection<ModelView> list() {
		// Collection<ModelView> modelViews = new ArrayList<ModelView>();
		// for(ModelView modelView : modelDao.getAllModelViews(modelType)) {
		// if (getPredicate() == null || getPredicate().apply(modelView)) {
		// modelViews.add(modelView);
		// }
		// }

		Collection<ModelView> modelView = new ArrayList<ModelView>();

		modelView.add(ModelFactory.newFunctionBlockView("com.erle", "1.0.0",
				"light", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.erle", "1.0.1",
				"light", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.erle", "1.0.2",
				"light", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.erle", "1.0.1",
				"spi", "SPI for communication"));
		modelView.add(ModelFactory.newFunctionBlockView("org.erle", "1.0.1",
				"rf", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("org.czar", "1.0.1",
				"i2c", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.czar", "1.0.1",
				"switch", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.mantos", "1.0.1",
				"radio", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.mantos", "1.0.1",
				"spi", "SPI"));

		return modelView;

	}

	public IModelQuery and(IModelQuery... queries) {
		return setPredicateAndReturnQuery(Predicates
				.and(getPredicates(queries)));
	}

	public IModelQuery or(IModelQuery... queries) {
		return setPredicateAndReturnQuery(Predicates.or(getPredicates(queries)));
	}

	public IModelQuery not(IModelQuery query) {
		return setPredicateAndReturnQuery(Predicates
				.not(((PredicateModelQuery) query).getPredicate()));
	}

	public ModelType getModelType() {
		return modelType;
	}

	public Predicate<ModelView> getPredicate() {
		return predicate;
	}

	private Collection<Predicate<ModelView>> getPredicates(
			IModelQuery... queries) {
		Collection<Predicate<ModelView>> modelViews = new ArrayList<>();
		for (IModelQuery query : queries) {
			modelViews.add(((PredicateModelQuery) query).getPredicate());
		}

		return modelViews;
	}

	private IModelQuery setPredicateAndReturnQuery(
			Predicate<ModelView> newPredicate) {
		if (predicate == null) {
			return new PredicateModelQuery(newPredicate, modelDao, modelType);
		} else {
			return new PredicateModelQuery(Predicates.and(Arrays.asList(
					predicate, newPredicate)), modelDao, modelType);
		}
	}
}
