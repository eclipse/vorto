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
package org.eclipse.vorto.remoterepository.internal.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.eclipse.vorto.remoterepository.Constants;
import org.eclipse.vorto.remoterepository.internal.search.AbstractModelQuery;
import org.eclipse.vorto.remoterepository.internal.search.Operator;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;

public class LuceneModelQuery extends AbstractModelQuery<Query> {

	private static final int MAX_RESULT_COUNT = 30;

	private Log log = LogFactory.getLog(LuceneModelQuery.class);

	private Query luceneQuery;

	private IndexSearcher searcher;

	public LuceneModelQuery(IndexSearcher searcher) {
		this.searcher = searcher;
	}

	public LuceneModelQuery(Query luceneQuery, IndexSearcher searcher) {
		this(searcher);
		this.luceneQuery = luceneQuery;
	}

	@Override
	public Collection<ModelView> list() {
		try {
			Query newQuery = luceneQuery;
			if (newQuery == null) {
				newQuery = new MatchAllDocsQuery();
			}
			
			if (searcher != null) {
				TopDocs hits = searcher.search(newQuery, MAX_RESULT_COUNT);
				return convertToSearchResults(hits);
			} else {
				log.info("Searcher is null. The indexes might not have been created yet.");
			}
		} catch (IOException e) {
			log.error("Error occured during search", e);
		}

		return Collections.emptyList();
	}

	private List<ModelView> convertToSearchResults(TopDocs hits) {

		List<ModelView> searchResultsList = new ArrayList<ModelView>();
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document document;
			try {
				document = searcher.doc(scoreDoc.doc);
				searchResultsList.add(convertToModelView(document));
			} catch (Exception e) {
				log.error(e);
				throw new RuntimeException(
						"Error while converting TopDocs to ModelView", e);
			}
		}

		return searchResultsList;
	}

	private ModelView convertToModelView(Document document) {

		String modelName = document.get(Constants.INDEX_FIELD_MODEL_NAME);
		String namespace = document.get(Constants.INDEX_FIELD_MODEL_NAMESPACE);
		String version = document.get(Constants.INDEX_FIELD_MODEL_VERSION);
		String type = document.get(Constants.INDEX_FIELD_MODEL_TYPE);
		ModelType modelType = ModelType.valueOf(type);
		String description = document.get(Constants.INDEX_FIELD_DESCRIPTION);
		ModelId modelId = new ModelId(modelType, namespace, version, modelName);
		ModelView modelView = new ModelView(modelId, description);
		return modelView;
	}

	@Override
	protected Query createCriteria(Operator operator, IModelQuery... queries) {
		BooleanQuery combinatorQuery = new BooleanQuery();

		// Lucene doesn't support the UNARY NOT operator, so this is the
		// workaround
		if (operator == Operator.NOT) {
			combinatorQuery.add(new MatchAllDocsQuery(),
					BooleanClause.Occur.MUST);
		}

		for (IModelQuery query : queries) {
			combinatorQuery.add(((LuceneModelQuery) query).getCriteria(),
					mapToBooleanClause(operator));
		}

		return combinatorQuery;
	}

	private Occur mapToBooleanClause(Operator operator) {
		if (operator == Operator.OR) {
			return BooleanClause.Occur.SHOULD;
		} else if (operator == Operator.NOT) {
			return BooleanClause.Occur.MUST_NOT;
		} else if (operator == Operator.AND) {
			return BooleanClause.Occur.MUST;
		}

		throw new RuntimeException("Query doesn't accept " + operator.name()
				+ " for this method.");
	}

	@Override
	protected Query createTextCriteria(String name, Operator operator,
			String value) {
		if (operator == Operator.EQUALS) {
			return new TermQuery(new Term(name, value));
		} else if (operator == Operator.LIKE) {
			return new WildcardQuery(new Term(name, "*" + value + "*"));
		}

		throw new RuntimeException("Query doesn't accept " + operator.name()
				+ " for this method.");
	}

	@Override
	protected IModelQuery newQuery(Query query) {
		return new LuceneModelQuery(query, searcher);
	}

	protected Query getCriteria() {
		return luceneQuery;
	}
}
