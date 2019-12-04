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
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.WordUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.EnumUtil;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.ModelVisibility;
import org.eclipse.vorto.repository.workflow.ModelState;

/**
 * Defines tagged search criteria made of {@literal [some supported tag]} +
 * {@literal [some text until next whitespace or end of input]}.<br/>
 * The first argument is a {@link String} used to construct a {@link Pattern} as above.<br/>
 * The second argument is a {@link Predicate <String>} that validates the value grouped.
 * See {@link SearchTags#normalizeParsedValue(String, String, Class)} for validation logic.<br/>
 * The third argument is a {@link BiFunction<String, SearchParameters, SearchParameters>} taking
 * the validated match value, an existing instance of {@link SearchParameters} and providing the
 * same instance of {@link SearchParameters}, mutated with the adequate {@code with...} invocation
 * (and with an appended multi-character wildcard {@literal *} for tagged and un-tagged names
 * containing no wildcard on their own).
 * @see SearchParameters
 * @author mena-bosch
 */
public enum SearchTags {

  /**
   * Tagged name query. Value matched initially by 1+ word character or ? or * wildcard.<br/>
   * A multi-character wildcard {@literal *} will be appended to it if it contains no wildcards,
   * before adding to the {@link SearchParameters}.
   */
  NAME(
      "name", SearchTags::normalizeParsedValue,
      (s, sp) -> sp.withTaggedName(appendPostfixWildcard(s))
  ),
  AUTHOR(
      "author", SearchTags::normalizeParsedValue,
      (s, sp) -> sp.withAuthor(s)
  ),
  USER_REFERENCE(
      "userReference", SearchTags::normalizeParsedValue,
      (s, sp) -> sp.withUserReference(s)
  ),

  /**
   * Visibility, type and state values are validated against specific enum types, case-insensitive.
   */
  VISIBILITY(
      "visibility",
      (s) -> normalizeParsedValue(s, ModelVisibility.class),
      (s, sp) -> sp.withVisibility(s)
  ),
  TYPE(
      "type",
      (s) -> normalizeParsedValue(s, ModelType.class),
      (s, sp) -> sp.withType(s)
  ),
  STATE(
      "state",
      (s) -> normalizeParsedValue(s, ModelState.class),
      (s, sp) -> sp.withState(s)
  ),

  /**
   * Tagged namespace query. Value matched initially by 1+ word character and/or dots, or ? or *
   * wildcard - namespace values can contain dots too.
   */
  NAMESPACE(
      "namespace",
      SearchTags::normalizeParsedValue,
      (s, sp) -> sp.withNamespace(s)
  ),

  /**
   * Tagged version query. Value matched initially by 1+ word character and/or dots, or ? or *
   * wildcard - version values can contain dots and colons too.
   */
  VERSION(
      "version",
      SearchTags::normalizeParsedValue,
      (s, sp) -> sp.withVersion(s)
  );

  private static final Logger LOGGER = Logger.getLogger(SearchTags.class);

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
   * Represents 1+ whitespaces. Used to split a given search string to parse for un-tagged values.
   */
  public static final String WHITESPACE_PATTERN = "\\s+";

  /**
   * Represents the default pattern used to parse tagged values (note: only the value, not the tag
   * itself, whose pattern is constructed with the name of the given enum element followed by colon).<br/>
   * This is likely sufficient for all types of tagged values, but the overloads of
   * {@link SearchTags#normalizeParsedValue(String, String, Class)} allow injecting a custom
   * pattern if necessary.
   */
  public static final String DEFAULT_VALUE_PATTERN = "\\S+";

  /**
   * Normalizes a given tagged value against a {@link String} representing a whole match
   * {@link Pattern}, optionally with an enum type to match the value to a restricted set of known
   * types.<br/>
   * The matching is performed case-insensitive in the latter case, however the normalized value
   * will have its first letter automatically capitalized to match model enum types, if the string
   * contains wildcards. This really pertains to ElasticSearch rather than a general search feauture,
   * as the enum types are indexed as keywords and therefore searched case-sensitive (wildcards do work).
   * It does not hurt the JCR query search, so it can be generalized here. <br/>
   * The given value is intended as the search term stripped off its tag. <br/>
   * If the parsed value is {@literal null}, empty or does not match the given pattern, an empty
   * {@link Optional<String>} is returned. <br/>
   * If the value is validated and no enum is provided, the {@link Optional<String>} is returned as
   * is.<br/>
   * If an enum is provided and the parsed value is valid but contains wildcards, no further checks
   * can be performed and the {@link Optional<String>} will contain the value with its first letter
   * capitalized to match model enum types' format. <br/>
   * If the enum is provided and the value contains no wildcards, an attempt to normalize the value
   * by first matching it against the enum's elements case-insensitive is performed. <br/>
   * Either that succeeds and the {@link Optional<String>} contains the normalized value, or that
   * fails and the {@link Optional<String>} contains the parsed value as-is (which likely means the
   * search will ultimately yield no result for that condition).
   * @param parsedValue
   * @param matchPattern
   * @param enumType
   * @return
   */
  public static <E extends Enum<E>> Optional<String> normalizeParsedValue(String parsedValue, String matchPattern, Class<E> enumType) {
    Optional<String> result = Optional.empty();
    // null or empty value (after trimming): empty Optional returned
    if (isBlank(parsedValue)) {
      return result;
    }
    // parsed value is invalid according to pattern for tag: empty Optional returned
    if (!parsedValue.matches(matchPattern)) {
      LOGGER.debug(String.format("Parsing of value '%s' failed against pattern '%s'", parsedValue, matchPattern));
      return result;
    }
    // parsed value matches pattern for tag, and no enum type specified: result will contain parsed
    // value as-is
    if (Objects.isNull(enumType)) {
      result = Optional.of(parsedValue);
    }
    // enum type given
    else {
      // the value contains wildcards - cannot resolve exact enum element, so capitalizing first
      // letter only
      if (containsWildcard(parsedValue)) {
        result = Optional.of(WordUtils.capitalize(parsedValue));
      }
      // the value does not contain wildcards: attempts to normalize against known enum types or
      // uses the value as-is if none found
      else {
        result = Optional.of(EnumUtil.forNameIgnoreCase(enumType, parsedValue));
      }
    }
    return result;
  }

  /**
   * @see SearchTags#normalizeParsedValue(String, String, Class)
   * @param parsedValue
   * @param matchPattern
   * @return
   */
  public static Optional<String> normalizeParsedValue(String parsedValue, String matchPattern) {
    return SearchTags.normalizeParsedValue(parsedValue, matchPattern, null);
  }

  /**
   * @see SearchTags#normalizeParsedValue(String, String, Class)
   * @param parsedValue
   * @param enumType
   * @return
   */
  public static <E extends Enum<E>>Optional<String> normalizeParsedValue(String parsedValue, Class<E> enumType) {
    return SearchTags.normalizeParsedValue(parsedValue, DEFAULT_VALUE_PATTERN, enumType);
  }

  /**
   * @see SearchTags#normalizeParsedValue(String, String, Class)
   * @param parsedValue
   * @return
   */
  public static <E extends Enum<E>>Optional<String> normalizeParsedValue(String parsedValue) {
    return SearchTags.normalizeParsedValue(parsedValue, DEFAULT_VALUE_PATTERN, null);
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
  private Function<String, Optional<String>> taggedValueNormalizer;
  private BiFunction<String, SearchParameters, SearchParameters> accumulator;

  SearchTags(String name, Function<String, Optional<String>> taggedValueNormalizer, BiFunction<String, SearchParameters, SearchParameters> accumulator) {
    this.name = name;
    this.pattern = Pattern.compile(
        String.format(SEARCH_TAG_PATTERN_FORMAT, this.name, GROUP_ANYTHING_BEFORE_NEXT_WHITESPACE_OR_END),
        Pattern.CASE_INSENSITIVE
    );
    this.taggedValueNormalizer = taggedValueNormalizer;
    this.accumulator = accumulator;
  }

  /**
   * @return the tag's {@code name} property, not to be confused with {@link Enum#name()}.
   */
  public String getTagName() {
    return name;
  }

  /**
   * Evaluates the given token against the case-insensitive pattern and returns whether it matches.
   * @param token
   * @return
   */
  public boolean tokenMatches(String token) {
    return pattern.matcher(token).matches();
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
   *     validates and normalizes <b>group 1</b> (expected to be the only back-referencing group
   *     in the pattern), by checking for {@lilteral null} and empty values, then matching against
   *     the specific value pattern, optionally normalizing enum values (see
   *     {@link SearchTags#normalizeParsedValue(String, String, Class)}).
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
      String test = matcher.group(1);
      /*
      Validates / normalizes the value against group 1 of the pattern, knowing that
      the returned Optional<String> will only be present if the value is valid or we cannot
      validate in all certainty.
      */
      this.taggedValueNormalizer.apply(
        matcher.group(1)).ifPresent(s -> accumulator.apply(s, parameters)
      );
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
   *     {@link Set<String>}, after appending a multi-character wildcard {@literal *} if it contains
   *     no wildcards already.
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
        .stream(text.split(WHITESPACE_PATTERN))
        .filter(s -> Arrays.stream(SearchTags.values()).noneMatch(st -> st.tokenMatches(s)))
        .forEach(s -> result.add(appendPostfixWildcard(s)));
    return result;
  }

  /**
   * If the input contains no wildcard, appends a multi-character wildcard {@code *}.
   * @param input
   * @return
   */
  public static String appendPostfixWildcard(String input) {
    if (isBlank(input)) {
      return input;
    }
    return containsWildcard(input) ? input : input.concat("*");
  }
}
