/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.impl.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.springframework.stereotype.Component;

/**
 * 
 * Utility class used to parse the model search query, extract the relevant search criterion and
 * their parameters and create a statement that can be converted to a {@link Query} by the
 * {@link QueryManager} interface.
 * 
 * The search criterion currently supported are:
 * <ol>
 * <li>name:
 * <li>namespace:
 * <li>version:
 * </ol>
 * 
 * @author shiv
 *
 */
@Component
public class ModelSearchUtil {

  public static final String SEARCH_FILTER_TYPE_INFORMATION_MODEL = "InformationModel";

  public static final String SEARCH_FILTER_TYPE_FUNCTION_BLOCK = "Functionblock";

  public static final String SEARCH_FILTER_TYPE_DATA_TYPE = "Datatype";

  public static final String[] SEARCH_FILTER_TYPE_LIST = {SEARCH_FILTER_TYPE_DATA_TYPE,
      SEARCH_FILTER_TYPE_FUNCTION_BLOCK, SEARCH_FILTER_TYPE_INFORMATION_MODEL};

  public static final String SEARCH_FILTER_KEY_NAME = "name:";

  public static final String SEARCH_FILTER_KEY_NAMESPACE = "namespace:";

  public static final String SEARCH_FILTER_KEY_VERSION = "version:";

  public static final String SEARCH_FILTER_KEY_AUTHOR = "author:";

  public static final String SEARCH_FILTER_KEY_STATE = "state:";
  
  public static final String SEARCH_FILTER_KEY_VISIBILITY = "visibility:";

  public static final String[] SEARCH_FILTER_KEY_LIST =
      {SEARCH_FILTER_KEY_NAME, SEARCH_FILTER_KEY_NAMESPACE, SEARCH_FILTER_KEY_VERSION,
          SEARCH_FILTER_KEY_AUTHOR, SEARCH_FILTER_KEY_STATE,SEARCH_FILTER_KEY_VISIBILITY};

  public final String VORTO_DISPLAYNAME = "vorto:name";

  public final String VORTO_NAMESPACE = "vorto:namespace";

  public final String VORTO_VERSION = "vorto:version";

  public final String VORTO_AUTHOR = "vorto:author";
  
  public final String VORTO_VISIBILITY = "vorto:visibility";

  public final String VORTO_STATE = "vorto:state";

  public final String VORTO_TYPE = "vorto:type";

  public final String SOURCE = "[nt:file]";

  public final String SELECT_QUERY = "SELECT * FROM " + SOURCE + " WHERE ";

  public final String AND = "AND";

  public final String OR = "OR";

  /**
   * Map to store the search criterion present in the query expression with their parameters
   * mentioned in the expression before generating the JCR Query search statement.
   */
  private HashMap<String, ArrayList<String>> searchCriteriaParameterMap = new HashMap<>();

  /**
   * The column names displayed on the main UI and the names with which the corresponding columns
   * are stored at the JCR are different.<br>
   * The name filter displayed on the UI is actually stored as [vorto:name] on JCR.<br>
   * This map keeps a track of all such mappings.
   */
  private HashMap<String, String> map = new HashMap<>();

  public ModelSearchUtil() {
    map.put(SEARCH_FILTER_KEY_NAME, VORTO_DISPLAYNAME);
    map.put(SEARCH_FILTER_KEY_NAMESPACE, VORTO_NAMESPACE);
    map.put(SEARCH_FILTER_KEY_VERSION, VORTO_VERSION);
    map.put(SEARCH_FILTER_KEY_AUTHOR, VORTO_AUTHOR);
    map.put(SEARCH_FILTER_KEY_VISIBILITY, VORTO_VISIBILITY);
    map.put(SEARCH_FILTER_KEY_STATE, VORTO_STATE);
  }

  /**
   * Parses the search query and identifies the type of search strategy applicable on this query.
   * 
   * @param queryExpression
   * @return {@link SearchStrategy}
   */
  public SearchStrategy getSearchStrategy(String queryExpression) {
    searchCriteriaParameterMap.clear();
    for (String string : SEARCH_FILTER_KEY_LIST) {
      if (queryExpression.contains(string)) {
        return SearchStrategy.FILTERED;
      }
    }
    return SearchStrategy.FULL_TEXT;
  }

  /**
   * Parses the queryExpression to extract the relevant search criterion and their parameters and
   * creates a statement that can be converted to a {@link Query} by the {@link QueryManager}
   * interface.
   * <p>
   * Returns the original queryExpression unmodified if no search criterion are present. The search
   * criterion currently supported are:-
   * <ol>
   * <li>name:
   * <li>namespace:
   * <li>version:
   * </ol>
   * <p>
   * A search query of the form <i><b>name:ColorLight</b></i> is converted to <i><b>SELECT * FROM
   * SOURCE WHERE name = 'ColorLight'</i></b>
   * <p>
   * A search query of the form <i><b>name:ColorLight version:1.0.0</b></i> is converted to
   * <i><b>SELECT * FROM SOURCE WHERE name = 'ColorLight' AND version = '1.0.0'</i></b>
   * <p>
   * A search query of the form <i><b>name:ColorLight name:Color</b></i> is converted to
   * <i><b>SELECT * FROM SOURCE WHERE name IN ('ColorLight', 'Color')</i></b>
   * 
   * @param queryExpression
   * @return
   */
  public String getJCRStatementQuery(String queryExpression) {
    if (getSearchStrategy(queryExpression) == SearchStrategy.FULL_TEXT) {
      return SELECT_QUERY + "[vorto:type] = '" + queryExpression + "' OR CONTAINS([vorto:name],'"
          + queryExpression + "') or CONTAINS([vorto:description],'" + queryExpression + "')";
    } else {
      for (String string : SEARCH_FILTER_TYPE_LIST) {
        if (queryExpression.contains(string)) {
          ArrayList<String> arrayList = new ArrayList<>();
          arrayList.add(string);
          searchCriteriaParameterMap.put(VORTO_TYPE, arrayList);
          break;
        }
      }
      String[] array = queryExpression.split("\\s+");
      for (String string : array) {
        for (String filter : SEARCH_FILTER_KEY_LIST) {
          if (string.startsWith(filter)) {
            addToSearchCriteriaParameterMap(filter, string.replace(filter, ""));
          }
        }
      }
      return buildJCRSearchQuery();
    }
  }

  /**
   * Function to add the search criteria parameters to the map as the query is being parsed.
   * 
   * @param key
   * @param value
   */
  private void addToSearchCriteriaParameterMap(String key, String value) {
    if (searchCriteriaParameterMap.containsKey(key)) {
      ArrayList<String> arrayList = searchCriteriaParameterMap.get(key);
      arrayList.add(value);
      searchCriteriaParameterMap.put(key, arrayList);
    } else {
      ArrayList<String> arrayList = new ArrayList<>();
      arrayList.add(value);
      searchCriteriaParameterMap.put(key, arrayList);
    }
  }

  /**
   * Builds a search query that can be converted to {@link Query} by the {@link QueryManager}
   * interface. <br>
   * This search query is built using the filters and their parameters specified in the original
   * query expression.
   * 
   * @return
   */
  private String buildJCRSearchQuery() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(SELECT_QUERY);
    Iterator<Entry<String, ArrayList<String>>> iterator =
        searchCriteriaParameterMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<String, ArrayList<String>> entry = iterator.next();
      if (entry.getValue().isEmpty()) {
        continue;
      } else {
        if (map.containsKey(entry.getKey())) {
        	if (entry.getValue().get(0).endsWith("*")) {
              stringBuilder.append("CONTAINS([").append(map.get(entry.getKey())).append("],");
        	} else {
            	stringBuilder.append("[").append(map.get(entry.getKey())).append("]");
        	}
        } else {
        	if (entry.getValue().get(0).endsWith("*")) {
                stringBuilder.append("CONTAINS([").append(entry.getKey()).append("],");
        	} else {
        		stringBuilder.append("[").append(entry.getKey()).append("]");
        	}
        }
        if (entry.getValue().size() == 1) {
        	if (entry.getValue().get(0).endsWith("*")) {
               stringBuilder.append(getSearchCriteriaParametersAsString(entry.getValue())).append(")");
        	} else {
                stringBuilder.append(" = ").append(getSearchCriteriaParametersAsString(entry.getValue()));
        	}
        } else {
          stringBuilder.append(" IN ")
              .append(getSearchCriteriaParametersAsString(entry.getValue()));
        }
        if (iterator.hasNext()) {
        	stringBuilder.append(" ").append(AND).append(" ");
        }
        
      }
    }
    return stringBuilder.toString();
  }

  /**
   * Converts a list of search criteria parameters into their suitable string representation so that
   * they can be used in the query statement.
   * <p>
   * A list of size 1 is converted to 'listitem'
   * <p>
   * A list of size n is converted to ('listitem1', 'listitem2', .... 'listitemn')
   * 
   * @param arrayList
   * @return
   */
  private String getSearchCriteriaParametersAsString(ArrayList<String> criteria) {
    StringBuilder stringBuilder = new StringBuilder();
    if (criteria.isEmpty()) {
      return "''";
    } else if (criteria.size() == 1) {
      stringBuilder.append("'").append(clean(criteria.get(0))).append("'");
    } else {
      stringBuilder.append("(");
      for (String string : criteria) {
        stringBuilder.append("'").append(string).append("', ");
      }
      stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.length() - 1, "");
      stringBuilder.append(")");
    }
    return stringBuilder.toString();
  }

  private String clean(String value) {
	if (value.endsWith("*")) {
		return value.substring(0,value.indexOf("*"));
	} else {
		return value;
	}
}

/**
   * Specifies the search strategies that can be applied to a model search query
   * 
   * @author shiv
   *
   */
  enum SearchStrategy {
    FULL_TEXT("Full Text"), FILTERED("Filtered");

    private final String strategy;

    public String getStrategy() {
      return this.strategy;
    }

    SearchStrategy(String strategy) {
      this.strategy = strategy;
    }
  }

  public Query createQueryFromExpression(Session session, String queryExpression) {
    try {
      QueryManager queryManager = session.getWorkspace().getQueryManager();
      String jcrStatementQuery = this.getJCRStatementQuery(queryExpression);
      if (jcrStatementQuery.equals(queryExpression)) {
        return queryManager.createQuery(jcrStatementQuery,
            org.modeshape.jcr.api.query.Query.JCR_SQL2);
      } else {
        return queryManager.createQuery(jcrStatementQuery,
            org.modeshape.jcr.api.query.Query.JCR_SQL2);
      }
    } catch (RepositoryException repoException) {
      throw new FatalModelRepositoryException("Could not create query from expression",
          repoException);
    }
  }
}
