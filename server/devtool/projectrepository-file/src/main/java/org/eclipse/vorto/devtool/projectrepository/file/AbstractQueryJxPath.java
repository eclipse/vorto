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
package org.eclipse.vorto.devtool.projectrepository.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.devtool.projectrepository.query.IResourceQuery;
import org.eclipse.vorto.devtool.projectrepository.query.TextFilter;

/**
 * Query to search for beans by an XPath expression
 * 
 */
public abstract class AbstractQueryJxPath implements IResourceQuery {
	
	private List<TextFilter> filters = new ArrayList<TextFilter>();
	
	protected void addFilter(TextFilter filter) {
		this.filters.add(filter);
	}
	
	@SuppressWarnings("unchecked")
	public List<Resource> list() {
        List<Object> params = new ArrayList<Object>();
        List<String> queries = getFilterQueries(filters, params);
        List<Resource> result = new ArrayList<Resource>(getAll());

        for (String query : queries) {        	
        	XPathEvaluator evaluator = new XPathEvaluator();
            result = (List<Resource>)evaluator.eval(query,result);
        }

        return result;
    }
	
	public Resource singleResult() {
        List<Resource> result = list();
        if (result.size() == 1) {
            return result.get(0);
        } else if (result.size() > 1) {
            throw new IllegalStateException("More than 1 result was found with this query");
        } else {
            return null;
        }
	}
    

    public abstract Collection<Resource> getAll();

    protected List<String> getFilterQueries(List<TextFilter> filters, List<Object> params) {
        List<String> queries = new ArrayList<String>();

        if ((filters != null) && !filters.isEmpty()) {

            for (TextFilter filter : filters) {

            	String query = filter.getWhereCondition();

                for (Object parameter : filter.getParameters()) {
                    query = query.replaceFirst("\\?", parameter.toString());
                }

                queries.add(query);
                params.addAll(filter.getParameters());
            }
        } else {
            queries.add("/.");
        }

        return queries;
    }

    public int count() {
        return list().size();
    }
}

/* EOF */
