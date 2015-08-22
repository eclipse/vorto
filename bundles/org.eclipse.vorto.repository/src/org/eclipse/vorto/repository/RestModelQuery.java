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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.fluent.Request;
import org.eclipse.vorto.core.api.repository.IModelQuery;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.repository.function.ModelViewToModelResource;
import org.eclipse.vorto.repository.function.StringToSearchResult;
import org.eclipse.vorto.repository.model.ModelView;
import org.eclipse.vorto.repository.model.SearchResult;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;


public class RestModelQuery extends AbstractModelQuery<String> {

	private static final String OP_SEPARATOR = ",";
	private static final String OP_END = ")";
	private static final String QUERY_SERVICE_URL = "/rest/model/query=";
	private static final String QUERY_ATTR_FORMAT = "%s(\"%s\")";
	private static final String QUERY_ATTRLIKE_FORMAT = "%sLike(\"%s\")";
	
	private final Map<Operator, String> operatorToQueryStringMap = initOperatorToQueryStringMap(); 

	private ConnectionInfoSupplier connectionInfo;
	private String queryString = null;
	private Function<String, SearchResult> resultConverter = new StringToSearchResult();
	private Function<ModelView, ModelResource> modelViewToModelResource = new ModelViewToModelResource();

	public RestModelQuery(ConnectionInfoSupplier connInfo) {
		this.connectionInfo = connInfo;
	}

	private Map<Operator, String> initOperatorToQueryStringMap() {
		Map<Operator, String> opToQueryStrMap = new HashMap<Operator, String>();
		opToQueryStrMap.put(Operator.AND, "and(");
		opToQueryStrMap.put(Operator.OR, "or(");
		opToQueryStrMap.put(Operator.NOT, "not(");
		return opToQueryStrMap;
	}

	public RestModelQuery(ConnectionInfoSupplier connInfo, String queryString) {
		this(connInfo);
		this.queryString = queryString;
	}

	public Collection<ModelResource> list() {
		try {
			String query = getQueryString(connectionInfo.connectionUrl(),
					queryString);
			
			String response = Request.Get(query).execute().returnContent()
					.asString();

			// Convert response to SearchResult
			SearchResult result = resultConverter.apply(response);

			// Convert the searchResult in result to return type
			return Collections2.transform(result.getSearchResult(),
					modelViewToModelResource);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error querying remote repository with queryString = "
							+ queryString, e);
		}
	}

	private String getQueryString(String connectionHost, String queryString) {
		StringBuilder connectionUrl = new StringBuilder();
		connectionUrl.append(connectionHost).append(QUERY_SERVICE_URL);
		
		if (queryString != null) {
			try {
				connectionUrl.append(URLEncoder.encode(queryString, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Error in encoding queryString = " + queryString, e);
			}
		}
		
		return connectionUrl.toString();
	}

	@Override
	public String getQuery() {
		return queryString;
	}
	
	// We need this for the joiner
	@Override
	public String toString() {
		return queryString;
	}

	@Override
	protected String createQuery(Operator operator, IModelQuery... queries) {
		if (!operatorToQueryStringMap.containsKey(operator)) {
			throw new IllegalArgumentException("Operator " + operator.name() + " isn't allowed in this expression.");
		}
		
		return new StringBuilder()
					.append(operatorToQueryStringMap.get(operator))
					.append(Joiner.on(OP_SEPARATOR).join(queries))
					.append(OP_END)
					.toString();
	}

	@Override
	protected String createQuery(String name, Operator operator, String value) {
		if (operator == Operator.EQUALS) {
			return String.format(QUERY_ATTR_FORMAT, name, value);
		} else if (operator == Operator.LIKE) {
			return String.format(QUERY_ATTRLIKE_FORMAT, name, value);
		}

		throw new RuntimeException("Query doesn't accept " + operator.name()
				+ " for this method.");
	}

	@Override
	protected IModelQuery newQuery(String query) {
		return new RestModelQuery(connectionInfo, query);
	}

}