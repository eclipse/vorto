package org.eclipse.vorto.repository.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.vorto.repository.api.content.IMappedElement;
import org.eclipse.vorto.repository.api.mapping.IMappingQuery;

public abstract class MappingQueryJxPath<Result extends IMappedElement> implements IMappingQuery<Result> {

	private List<TextFilter> filters = new ArrayList<TextFilter>();
	
	protected void addFilter(TextFilter filter) {
		this.filters.add(filter);
	}
	
	@SuppressWarnings("unchecked")
	public List<Result> list() {
        List<Object> params = new ArrayList<Object>();
        List<String> queries = getFilterQueries(filters, params);
        List<Result> result = new ArrayList<Result>(getAll());

        for (String query : queries) {        	
        	XPathEvaluator evaluator = new XPathEvaluator();
            result = (List<Result>)evaluator.eval(query,result);
        }

        return result;
    } 

    protected abstract Collection<Result> getAll(); 
    
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
	
	@Override
	public IMappingQuery<Result> stereotype(String name) {
		TextFilter tf = new TextFilter();
		tf.setKey("stereotype");
		tf.setText(name);
		tf.setWhereCondition("/.[stereotypes[name = '?']]");
		addFilter(tf);

		return this;
	}

	@Override
	public IMappingQuery<Result> attribute(String key, String value) {
		TextFilter tf = new TextFilter();
		tf.setKey("attribute");
		tf.setText(value);
		tf.setWhereCondition(".[stereotypes/attributes[@name = '" + key + "'] = '?']");
		addFilter(tf);
		return this;
	}

}
