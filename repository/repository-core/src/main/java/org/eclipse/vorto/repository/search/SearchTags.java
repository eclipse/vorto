/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.search;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.vorto.model.EnumUtil;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.ModelVisibility;
import org.eclipse.vorto.repository.workflow.ModelState;

/**
 * Defines tagged search criteria made of {@literal [some supported tag]} +
 * {@literal [some text until next whitespace or end of input]}.<br/>
 * The first argument is a {@link String} used to construct a {@link Pattern} as above.<br/>
 * The second argument is a {@link Predicate <String>} that validates the value grouped.
 * See {@link SearchTags#validateParsedValue(String, String, Class)} for validation logic.<br/>
 * The third argument is a {@link BiFunction<String, SearchParameters, SearchParameters>} taking
 * the validated match value, an existing instance of {@link SearchParameters} and providing the
 * same instance of {@link SearchParameters}, mutated with the adequate {@code with...} invocation.
 * @see SearchParameters
 * @author mena-bosch
 */
public enum SearchTags {

  /**
   * Tagged name query. Value matched initially by 1+ word character or ? or * wildcard.
   */
  NAME(
      "name", (s) -> validateParsedValue(s, "[\\w?*]+"),
      (s, sp) -> sp.withName(s)
  ),
  AUTHOR(
      "author", (s) -> validateParsedValue(s, "[\\w?*]+"),
      (s, sp) -> sp.withAuthor(s)
  ),
  USER_REFERENCE(
      "userReference", (s) -> validateParsedValue(s, "[\\w?*]+"),
      (s, sp) -> sp.withUserReference(s)
  ),

  /**
   * Visibility, type and state values are validated against specific enum types, case-insensitive.
   */
  VISIBILITY(
      "visibility",
      (s) -> validateParsedValue(s, "[\\w?*]+",  ModelVisibility.class),
      (s, sp) -> sp.withVisibility(s)
  ),
  TYPE(
      "type",
      (s) -> validateParsedValue(s, "[\\w?*]+",  ModelType.class),
      (s, sp) -> sp.withType(s)
  ),
  STATE(
      "state",
      (s) -> validateParsedValue(s, "[\\w?*]+",  ModelState.class),
      (s, sp) -> sp.withState(s)
  ),

  /**
   * Tagged namespace query. Value matched initially by 1+ word character and/or dots, or ? or *
   * wildcard - namespace values can contain dots too.
   */
  NAMESPACE(
      "namespace",
      (s) -> validateParsedValue(s, "[\\w?*.]+"),
      (s, sp) -> sp.withNamespace(s)
  ),

  /**
   * Tagged version query. Value matched initially by 1+ word character and/or dots, or ? or *
   * wildcard - version values can contain dots and colons too.
   */
  VERSION(
      "version",
      (s) -> validateParsedValue(s, "[\\w?*.:]+"),
      (s, sp) -> sp.withVersion(s)
  );


  /**
   * This {@link Pattern} consumes any 1* character reluctantly until the next whitespace or end
   * of input, and groups the consumed character(s) in group 1.
   */
  public static final String GROUP_ANYTHING_BEFORE_NEXT_WHITESPACE_OR_END = "(.+?)(?=\\s|$)";
  /**
   * This simple string format represents the [tag]:[search value] format to be parsed in the
   * search expression.
   */
  public static final String SEARCH_TAG_PATTERN_FORMAT = "%s:%s";

  /**
   * Supported wildcards: {@literal *} and {@literal ?}.
   */
  public static final String[] WILDCARDS = {"*", "?"};

  /**
   * Validates a given tagged value against a {@link String} representing a whole match
   * {@link Pattern}, optionally with an enum type to match the value to a restricted set of known
   * types (matching is performed case-insensitive in that case). <br/>
   * The given value is intended as the search term stripped off its tag. <br/>
   * If the term contains wildcards, the enum validation is omitted, and the matching delegated
   * the the backing search engine (e.g. ElasticSearch).
   * @param parsedValue
   * @param enumType
   * @return
   */
  public static <E extends Enum<E>> boolean validateParsedValue(String parsedValue, String matchPattern, Class<E> enumType) {
    if (isBlank(parsedValue)) {
      return false;
    }
    if (!parsedValue.matches(matchPattern)) {
      return false;
    }
    if (Objects.isNull(enumType) || containsWildcard(parsedValue)) {
      return true;
    }
    return EnumUtil.isAnyValueOfCaseInsensitive(enumType, parsedValue);
  }

  /**
   * @see SearchTags#validateParsedValue(String, String, Class)
   * @param parsedValue
   * @param matchPattern
   * @return
   */
  public static boolean validateParsedValue(String parsedValue, String matchPattern) {
    return SearchTags.validateParsedValue(parsedValue, matchPattern, null);
  }

  /**
   * Boilerplate to infer whether a given value contains any supported wildcard: {@literal *} or
   * {@literal ?}.
   * @param value
   * @return whether the given value contains any of {@link SearchTags#WILDCARDS}
   */
  public static boolean containsWildcard(String value) {
    if (isBlank(value)) {
      return false;
    }
    else {
      for (String wildcard: WILDCARDS) {
        if (value.contains(wildcard)) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Boilerplate to check whether the given {@link String} is null or if trimmed, empty.
   * @param value
   * @return whether the given value is {@literal null} or if trimmed, empty.
   */
  public static boolean isBlank(String value) {
    return  Objects.isNull(value) || value.trim().isEmpty();
  }

  private String name;
  private Pattern pattern;
  private Predicate<String> taggedValueValidator;
  private BiFunction<String, SearchParameters, SearchParameters> accumulator;

  SearchTags(String name, Predicate<String> taggedValueValidator, BiFunction<String, SearchParameters, SearchParameters> accumulator) {
    this.name = name;
    this.pattern = Pattern.compile(
        String.format(SEARCH_TAG_PATTERN_FORMAT, this.name, GROUP_ANYTHING_BEFORE_NEXT_WHITESPACE_OR_END),
        Pattern.CASE_INSENSITIVE
    );
    this.taggedValueValidator = taggedValueValidator;
    this.accumulator = accumulator;
  }

  /**
   * @return the tag's {@code name} property, not to be confused with {@link Enum#name()}.
   */
  public String getTagName() {
    return name;
  }

  /**
   * Validates that the given value is not {@code null} nor empty if trimmed, and that it
   * matches the correct expression for this enum type.
   * @see SearchTags#taggedValueValidator
   * @param expression
   * @return
   */
  public boolean validate(String expression) {
    return !isBlank(expression) && this.taggedValueValidator.test(expression);
  }

  /**
   * Parses a tagged value from a text input. <br/>
   * This method can mutate the given {@link SearchParameters} by operating as follows:
   * <ol>
   *   <li>
   *     It checks the text for the tag pertaining to this enum element by matching
   *     against its {@link Pattern}
   *   </li>
   *   <li>
   *     For any match found (i.e. any tagged expression in the given {@literal text}, it
   *     validates <b>group 1</b> (expected to be the only back-referencing group in the pattern),
   *     by checking for {@lilteral null} and empty values, then matching against the specific
   *     value pattern, and if the value contains no wildcards but reflects an enumerated type,
   *     also matching case-insensitive against any known enum elements.
   *     See {@link SearchTags#validateParsedValue(String, String, Class)}.
   *   </li>
   *   <li>
   *     If the value is valid according to the above methodology, it is then added to the
   *     given {@link SearchParameters} by invoking the {@link BiFunction#apply(Object, Object)}
   *     method of the element's accumulator.
   *   </li>
   * </ol>
   * This method should be invoked iteratively by going through all {@link SearchTags#values()},
   * in order to progressively populate any given instance of {@link SearchParameters}.<br/>
   * As this will only look for tagged values, a utility method can be invoked on the same text
   * to find a {@link Set <String>} of spurious, untagged values (which will concretely default to
   * a name search - see {@link SearchTags#parseUntaggedValues(String)}.<br/>
   * While this methodology presents the inconvenience of parsing the text multiple times (once for
   * each tag value, then once again for all untagged values), it is much cleaner than mutating
   * the text itself (i.e. by using a mutable {@link CharSequence}).
   * @param parameters
   * @param text
   * @return
   */
  public SearchParameters parseValue(SearchParameters parameters, String text) {
    if (isBlank(text)) {
      return parameters;
    }
    Matcher matcher = pattern.matcher(text);
    while (matcher.find()) {
      String parsedValue = matcher.group(1);
      if (validate(parsedValue)) {
        accumulator.apply(parsedValue, parameters);
      }
    }
    return parameters;
  }

  /**
   * This utility method complements {@link SearchTags#parseValue(SearchParameters, String)}. <br/>
   * It parses the given text for all untagged values by:
   * <ol>
   *   <li>Splitting it by {@literal \s+}</li>
   *   <li>
   *     Checking every chunk (if any) whether it contains any enumerated tag name (see also:
   *     {@link SearchTags#getTagName()}).
   *   </li>
   *   <li>
   *     If the chunk does <b>not</b> contain any known tag name, then it is added to the
   *     {@link Set<String>}.
   *   </li>
   * </ol>
   * The order of appearance of untagged elements is not retained in the result.
   * @param text
   * @return
   */
  public static Set<String> parseUntaggedValues(String text) {
    Set<String> result = new HashSet<>();
    if (isBlank(text)) {
      return result;
    }
    Arrays
        .stream(text.split("\\s+"))
        .filter(s -> Arrays.stream(SearchTags.values()).noneMatch(st -> s.contains(st.getTagName())))
        .forEach(result::add);
    return result;
  }

  /**
   * Boilerplate utility to append a multi-character wildcard ({@literal *}) to any value of
   * the given {@link Set<String>} that does not already contain a supported wildcard (i.e.
   * {@literal *} or {@literal ?}).
   * @param names
   * @return
   */
  public static Set<String> appendPostfixWildcardForNames(Set<String> names) {
    Set<String> result = new HashSet<>();
    for (String name: names) {
      if (containsWildcard(name)) {
        result.add(name);
      }
      else {
        result.add(name.concat("*"));
      }
    }
    return result;
  }
}
