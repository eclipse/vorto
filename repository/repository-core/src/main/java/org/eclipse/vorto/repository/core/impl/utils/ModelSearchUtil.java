/**
 * Copyright (c) 2018, 2019 Contributors to the Eclipse Foundation
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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.search.SearchParameters;
import org.eclipse.vorto.repository.search.SearchTags;
import org.springframework.stereotype.Component;

/**
 * 
 * Utility class used to  extract the relevant search criteria from a text query, and convert it
 * into a JCR-SQL2 compliant {@link Query}.
 * @author shiv
 * @author mena-bosch (refactored by)
 *
 */
@Component
public class ModelSearchUtil {

  public static final String SOURCE = "[nt:file]";

  public static final String SELECT_QUERY = "SELECT * FROM " + SOURCE;

  public static final String SELECT_WHERE_QUERY =  SELECT_QUERY + " WHERE ";

  public static final String OR = " OR ";

  public static final String VORTO_NAME_FIELD = "[vorto:name]";

  public static final String VORTO_NAMESPACE_FIELD = "[vorto:namespace]";

  public static final String VORTO_VERSION_FIELD = "[vorto:version]";

  public static final String VORTO_AUTHOR_FIELD = "[vorto:author]";

  public static final String LAST_MODIFIED_BY_FIELD = "[jcr:lastModifiedBy]";

  public static final String VORTO_VISIBILITY_FIELD = "[vorto:visibility]";

  public static final String VORTO_STATE_FIELD = "[vorto:state]";

  public static final String VORTO_TYPE_FIELD = "[vorto:type]";

  /**
   * Used to convert search query wildcards to SQL-compatible wildcards with {@code LIKE} operator.
   */
  public static final Map<String, String> WILDCARD_TO_SQL_CONVERSION = new HashMap<>();
  static {
    WILDCARD_TO_SQL_CONVERSION.put("*", "%");
    WILDCARD_TO_SQL_CONVERSION.put("?", "_");
  }

  public static final String CONTAINS_STATEMENT_FORMAT = "CONTAINS (%s, '%s')";
  public static final String PARENTHESIS_WRAPPER_FORMAT = "(%s)";
  public static final String LOWER_LIKE_FORMAT = "LOWER(%s) LIKE '%s'";
  public static final String LOWER_EQUALS_FORMAT = "LOWER(%s) = '%s'";
  public static final String AND_FORMAT = " AND %s";

  public static Query createQueryFromExpression(Session session, String queryExpression) {
    try {

      QueryManager queryManager = session.getWorkspace().getQueryManager();
      String query = toJCRQuery(SearchParameters.build(queryExpression));

      return queryManager.createQuery(
        query, org.modeshape.jcr.api.query.Query.JCR_SQL2
      );

    } catch (RepositoryException repoException) {
      throw new FatalModelRepositoryException("Could not create query from expression",
          repoException);
    }
  }

  /**
   * Converts common wildcard characters in set {@literal [*,?]} to their SQL equivalents:
   * {@literal [%,_]} which work with JCL-SQL2 language.<br/>
   * The original wildcards come from the Vorto search syntax, and are used as is when the search
   * service is ElasticSearch, but not when the search service builds a JCR query. <br/>
   * Note that the conversion {@literal %} is presently only useful when within a word, as
   * wildcard searches will be used within a {@code CONTAINS} statement. <br/>
   * Therefore, while e.g. {@code CONTAINS([vorto:name] , 'some_hing')"} or
   * {@code CONTAINS([vorto:name] , 'so*ing')"} is useful, {@code CONTAINS([vorto:name] , '*something')"}
   * or {@code CONTAINS([vorto:name] , 'something*')"} etc. <br/>
   * At the time of writing, there are no clear insights on whether any performance overhead of
   * leaving leading or trailing wildcards in a {@code CONTAINS} search would justify trimming them.
   * @param term
   * @return
   */
  public static String convertWildcardsToSQL(String term) {
    if (SearchTags.isBlank(term)) {
      return term;
    }
    String result = term;
    for (Map.Entry<String, String> e: WILDCARD_TO_SQL_CONVERSION.entrySet()) {
      result = result.replace(e.getKey(), e.getValue());
    }
    return result;
  }

  /**
   * Batch processing for {@link ModelSearchUtil#convertWildcardsToSQL(String)}.
   * @see ModelSearchUtil#convertWildcardsToSQL(String) 
   * @param terms
   * @return
   */
  public static Stream<String> convertWildcardsToSQL(Stream<String> terms) {
    // usual boilerplate
    if (Objects.isNull(terms)) {
      return Stream.empty();
    }
    return terms.map(ModelSearchUtil::convertWildcardsToSQL);
  }

  /**
   * Batch lowercasing of given terms.
   * @param terms
   * @return
   */
  public static Stream<String> lowerCase(Stream<String> terms) {
    // usual boilerplate
    if (Objects.isNull(terms)) {
      return Stream.empty();
    }
    return terms.map(String::toLowerCase);
  }

  /**
   * Builds an {@code OR}-separated series of free-text search constraints using the
   * {@code CONTAINS} operator for a given collection of fields, with a given collection of values. <br/>
   * In theory, this would used collate search logic for two fields into {@literal OR}-related
   * child constraints for each:
   * <ul>
   *   <li>
   *     The {@literal name}, which relates to:
   *     <ul>
   *       <li>
   *         {@literal displayName}
   *       </li>
   *       <li>
   *         {@literal description}
   *       </li>
   *       <li>
   *         {@literal searchableName}
   *       </li>
   *     </ul>
   *   </li>
   *   <li>
   *     The {@literal userReference}, which relates to:
   *     <ul>
   *       <li>
   *         {@literal author}
   *       </li>
   *       <li>
   *         {@literal lastModifiedBy}
   *       </li>
   *     </ul>
   *   </li>
   * </ul>
   * In practice, this method will only be invoked with a multi-element field collection when the
   * {@literal userReference} tag is in use. Contrary to the ElasticSearch service, not all the
   * aggregated fields above are not queryable with a JRC-SQL string, due to the fact that
   * {@literal displayName}, {@literal searchableName} are not supported at the time of writing.<br/>
   * Therefore {@literal name} queries will use one field: {@literal [vorto:name]}, whereas
   * {@literal userReference} queries will indeed use two fields: {@literal [vorto:author]} and
   * {@literal [jcr:lastModifiedBy]}.<br/>
   * Regardless of the fine points, the mechanism  works as follows:
   * <ol>
   *   <li>
   *     First, builds an {@literal OR}-separated list containing all values, once.
   *   </li>
   *   <li>
   *     Then, for each field, adds an {@literal OR}-separated constraint in the following format:
   *     {@code CONTAINS([field0], 'the OR-separated list of values') OR CONTAINS([field1], 'the same values') OR ...}
   *   </li>
   *   <li>
   *     The whole resulting constraint is enclosed within parenthesis.
   *   </li>
   * </ol>
   *
   * @param fields
   * @param values
   * @return
   */
  public static String buildContainsOrConstraint(Collection<String> fields, String... values) {
    // boilerplate validation
    if (Objects.isNull(fields) || fields.isEmpty() || Objects.isNull(values) || values.length == 0) {
      return "";
    }
    // building OR-separated values string
    String valuesFlattened = String.join(OR, values);

    // building OR-separated formats of CONTAINS(field, 'OR-separated list of values')
    return String.format(
        PARENTHESIS_WRAPPER_FORMAT,
        String.join(
          OR,
          fields.stream().map(
            f -> String.format(CONTAINS_STATEMENT_FORMAT, f, valuesFlattened)
          )
          .collect(Collectors.toSet())
        )
    );
  }

  /**
   * Builds a constraint whose terms are separated by {@code OR}.<br/>
   * The {@code operatorFormat} argument must be a 2-variable format {@link String} such as
   * {@link ModelSearchUtil#LOWER_EQUALS_FORMAT} or {@link ModelSearchUtil#LOWER_LIKE_FORMAT}.<br/>
   * <b>Note:</b> for {@link ModelSearchUtil#LOWER_LIKE_FORMAT} usage, it is assumed that the
   * values will be lower-cased by the caller. This method assumes they are and does not alter
   * them.<br/>
   * For each of the given fields, all values are joined into an expression that fulfills the pattern:
   * {@code (field op value0 [OR field op value1 OR ...]*)} where:
   * <ul>
   *   <li>
   *     {@code op} is the operator coming from the {@code operatorFormat} format {@link String}.
   *   </li>
   *   <li>
   *     The statements between square brackets followed by an asterisk are intended as occurring
   *     0+ times.
   *   </li>
   * </ul>
   * The final result will be a parentheses-enclosed {@link String} representing a constraint in
   * the following format:
   * {@code ((field0 op value0 [OR field0 op value1 OR ...]*) OR (field1 op value0 [OR field1 op value1 OR...]) OR...)}
   *
   * @param operatorFormat
   * @param fields
   * @param values
   * @return
   */
  public static String buildOrConstraint(String operatorFormat, Collection<String> fields, String... values) {
    String result = "";
    // boilerplate validation
    if (Objects.isNull(fields) || fields.isEmpty() || Objects.isNull(values) || values.length == 0) {
      return result;
    }

    // single append with lambda
    result =
      // formatting result within parenthesis
      conditionallyWrapInParenthesis(
        fields.size() > 1,
        // joining mapping of field -> (field0 op value0 OR field op value1 ...) OR (field1 op...)
        // where "op" is operatorFormat, i.e. a format String such as "%s = %s", or "%s LIKE %s"
        String.join(
          OR,
          fields.stream().map(
            f ->
                // this builds each child constraint such as (field0 op value0 OR field0 op value1 ...)
                conditionallyWrapInParenthesis(
                    values.length > 1,
                    String.join(
                        OR,
                        Arrays.stream(values)
                          .map(v -> String.format(operatorFormat, f, v))
                          .collect(Collectors.toList())
                    )
                )
          )
          .collect(Collectors.toList())
        )
      )
    ;
    return result.toString();
  }

  /**
   * Boilerplate utility to wrap a given input {@link String} in parenthesis using the
   * {@link ModelSearchUtil#PARENTHESIS_WRAPPER_FORMAT} format. <br/>
   * Takes a {@code boolean} condition to infer whether the input actually needs to be wrapped. <br/>
   * Useful to remove clutter when used within lambda expressions.<br/>
   * The input is <b>not</b> validated in any way.
   * @param condition
   * @param input
   * @return
   */
  public static String conditionallyWrapInParenthesis(boolean condition, String input) {
    return condition ? String.format(PARENTHESIS_WRAPPER_FORMAT, input) : input;
  }

  /**
   *
   * @param item
   * @return whether the given string contains any SQL-style wildcard.
   * @see SearchTags#containsWildcard(String) for search-style wildcard checks instead.
   * @see ModelSearchUtil#WILDCARD_TO_SQL_CONVERSION
   */
  public static boolean containsSQLWildcard(String item) {
    // boilerplate validation
    if (SearchTags.isBlank(item)) {
      return false;
    }
    for (String value: WILDCARD_TO_SQL_CONVERSION.values()) {
      if (item.contains(value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Constructs a constraint with the given values and fields, by working as follows:
   * <ol>
   *   <li>
   *     Firstly, validates both arguments against null/empty.
   *   </li>
   *   <li>
   *     Secondly, converts any search-language wildcard into SQL wildcards.
   *     See{@link ModelSearchUtil#convertWildcardsToSQL(Stream)}.
   *   </li>
   *   <li>
   *     When dealing with a single value only, the constraint's operator will be inferred by
   *     whether the value contains any wildcards: {@code LIKE} if it does, {@code =} if it doesn't.
   *     In both cases, {@code LOWER} will be invoked on the field and the value will be lower-cased.
   *     The constraint is then returned as {@code (field0 op singleValue [OR field1 op singleValue]*)},
   *     where {@code op} is the chosen operator, the main constraint is enclosed in parenthesis,
   *     and the number of comparisons is determined by the number of fields given.
   *     Fields within square brackets followed by an asterisk in the example are to be interpreted
   *     as a 0+ occurrence.
   *   </li>
   *   <li>
   *     When dealing with multiple values, the impact is not just on the operator, but on the
   *     actual type of the constraint.
   *     <ul>
   *       <li>
   *         For multiple values either of which contains any wildcard, a FTS constraint is created
   *         in the form of:
   *         {@code (CONTAINS(field0, 'value0 OR value1 [OR...]*') [OR CONTAINS(field1, 'value0 OR value1 [OR...]*']*}
   *       </li>
   *       <li>
   *         For multiple values none of which contains any wildcard, a nested "equals" constraint
   *         is created in the form of:
   *         {@code (field0 = 'value0' OR field0 = 'value1' [OR...]*) [OR (field1 = 'value0' OR field1 = 'value1' [OR...]*)]*}
   *       </li>
   *     </ul>
   *   </li>
   *   <li>
   *     Lastly worth specifying, the invoked utility methods wrap what becomes the output here in
   *     parenthesis.
   *   </li>
   * </ol>
   *
   * @see ModelSearchUtil#convertWildcardsToSQL(Stream)
   * @see ModelSearchUtil#containsSQLWildcard(String) 
   * @see ModelSearchUtil#buildOrConstraint(String, Collection, String...) 
   * @see ModelSearchUtil#buildContainsOrConstraint(Collection, String...)
   * @param values
   * @param fields
   * @return
   */
  public static String buildJCRConstraint(Collection<String> values, String... fields) {
    // boilerplate validation
    if (Objects.isNull(values) || values.isEmpty() || Objects.isNull(fields) || fields.length == 0) {
      return "";
    }

    // wildcard conversion
    String[] convertedValues = lowerCase(convertWildcardsToSQL(values.stream())).toArray(String[]::new);

    // single value: using = for non-wildcard and LIKE for wildcard
    if (convertedValues.length == 1) {
      String value = convertedValues[0];
      String operatorFormat = containsSQLWildcard(value) ? LOWER_LIKE_FORMAT : LOWER_EQUALS_FORMAT;
      return buildOrConstraint(operatorFormat, Arrays.asList(fields), value);
    }
    // multiple values
    else {
      // deciding if any value has a wildcard (in which case we use CONTAINS FTS) or none
      // (in the latter case we build nested OR statements with equals operator)
      boolean anyWildcardInValues = Arrays.stream(convertedValues).anyMatch(ModelSearchUtil::containsSQLWildcard);
      if (anyWildcardInValues) {
        return buildContainsOrConstraint(Arrays.asList(fields), convertedValues);
      }
      else {
        return buildOrConstraint(LOWER_EQUALS_FORMAT, Arrays.asList(fields), convertedValues);
      }
    }
  }

  /**
   * Boilerplate with trivial logic wrapping the given constraint into a format {@link String} which 
   * basically:
   * <ol>
   *   <li>
   *     Checks whether the given query {@link StringBuilder} only equals
   *     {@link ModelSearchUtil#SELECT_WHERE_QUERY}.
   *   </li>
   *   <li>
   *     If so, appends a single whitespace and the constraint as-is.
   *   </li>
   *   <li>
   *     If not (i.e. other constraints have already been added), prepends {@literal AND} to the
   *     constraint through {@link String} format {@link ModelSearchUtil#AND_FORMAT}, and then
   *     appends the formatted constraint to the given query {@link StringBuilder}.
   *   </li>
   * </ol>
   * This is used when building the JCR full query while consuming the generated
   * {@link SearchParameters}, by appending all built constraints coming from that POJO.<br/>
   * Assumes the given constraint is syntactically valid, not empty or {@code null}.<br/>
   * @see ModelSearchUtil#toJCRQuery(SearchParameters) where this is invoked.
   * @see ModelSearchUtil#AND_FORMAT
   * @param query
   * @param constraint
   * @return
   */
  public static void appendConstraint(StringBuilder query, String constraint) {
    if (!query.toString().equals(SELECT_WHERE_QUERY)) {
      query.append(String.format(AND_FORMAT, constraint));
    }
    else {
      query.append(constraint);
    }
  }

  /**
   * Builds a JCR-SQL2 compatible search query based on the given {@link SearchParameters}.<br/>
   * Contrary to the ElasticSearch service, the query is not built using a programmatic QOM here. <br/>
   * Two main reasons for this:
   * <ol>
   *   <li>
   *     The QOM API does not provide (m)any usage examples, which is especially painful when building
   *     arbitrary queries based on user search input. See
   *     <a href src="https://docs.jboss.org/author/display/MODE50/Query+and+search#Queryandsearch-Creatingqueries">here</a>
   *     and
   *     <a href src="https://docs.jboss.org/author/display/MODE/JCR-JQOM">here</a> for literally
   *     the <i>only two</i>, minimal examples I could find.
   *   </li>
   *   <li>
   *     The <a href src="https://docs.jboss.org/author/display/MODE/JCR-JQOM">docs</a> advises to:
   *     <blockquote>[...]]not consider using the QOM API just to get a performance benefit.
   *     The JCR-SQL2 parser is very efficient, and your application code will be far easier to
   *     understand and maintain. Where possible, use JCR-SQL2 query expressions.</blockquote>
   *   </li>
   * </ol>
   * <b>Note:</b> some of the fields indexed and used by the ElasticSearch service are not usable
   * here, as follows:
   * <ul>
   *   <li>
   *     {@literal displayName}
   *   </li>
   *   <li>
   *     {@literal lastModifiedBy} - this is in fact replaced by {@literal [jcr:lastModifiedBy]}
   *   </li>
   *   <li>
   *     {@literal searchableName}
   *   </li>
   * </ul>
   * Finally, empty queries (resulting in empty {@link SearchParameters}) will build a static
   * catch-all query with no constraints.
   * @see ModelSearchUtil#buildContainsOrConstraint(Collection, String...)
   * @see ModelSearchUtil#buildJCRConstraint(Collection, String...)
   * @see ModelSearchUtil#buildOrConstraint(String, Collection, String...) 
   * @param params
   * @return
   */
  public static String toJCRQuery(SearchParameters params) {

    StringBuilder result = new StringBuilder();

    // empty search, returning query for all models
    if (params.isEmpty()) {
      return result.append(SELECT_QUERY).toString();
    }

    // appending WHERE
    result.append(SELECT_WHERE_QUERY);

    // handling authors
    if (params.hasAuthors()) {
      appendConstraint(result, buildJCRConstraint(params.getAuthors(), VORTO_AUTHOR_FIELD));
    }

    // handling names
    if (params.hasNames()) {
      appendConstraint(result, buildJCRConstraint(params.getNames(), VORTO_NAME_FIELD));
    }

    // handling namespaces
    if (params.hasNamespaces()) {
      appendConstraint(result, buildJCRConstraint(params.getNamespaces(), VORTO_NAMESPACE_FIELD));
    }

    // handling states
    if (params.hasStates()) {
      appendConstraint(result, buildJCRConstraint(params.getStates(), VORTO_STATE_FIELD));
    }

    // handling types
    if (params.hasTypes()) {
      appendConstraint(result, buildJCRConstraint(params.getTypes(), VORTO_TYPE_FIELD));
    }

    // handling user references (authors + lastModifiedBys)
    if (params.hasUserReferences()) {
      appendConstraint(result, buildJCRConstraint(params.getUserReferences(), VORTO_AUTHOR_FIELD, LAST_MODIFIED_BY_FIELD));
    }

    // handling versions
    if (params.hasVersions()) {
      appendConstraint(result, buildJCRConstraint(params.getVersions(), VORTO_VERSION_FIELD));
    }

    // handling visibilities
    if (params.hasVisibilities()) {
      appendConstraint(result, buildJCRConstraint(params.getVisibilities(), VORTO_VISIBILITY_FIELD));
    }

    return result.toString();
  }

}
