/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.search.extractor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.ModelInfo;

public class BasicIndexFieldExtractor implements IIndexFieldExtractor {

  private static final Logger LOGGER = Logger.getLogger(BasicIndexFieldExtractor.class);
  
  public static final String VISIBILITY = "visibility";

  public static final String STATE = "state";

  public static final String DESCRIPTION = "description";

  public static final String DISPLAY_NAME = "displayName";

  public static final String AUTHOR = "author";
  
  public static final String MODIFIED_BY = "lastModifiedBy";

  public static final String MODEL_TYPE = "modelType";

  public static final String MODEL_ID = "modelId";
  
  public static final String MODEL_NAME_SEARCHABLE = "searchableName";
  
  public static final String MODEL_HASIMAGE = "hasImage";
  
  public static final String MODEL_CREATIONDATE = "createdOn";

  public static final String NAMESPACE = "namespace";

  public static final String VERSION = "version";
  
  @Override
  public Map<String, String> extractFields(ModelInfo modelInfo) {
    Map<String, String> basicFields = new HashMap<>();
    
    basicFields.put(BasicIndexFieldExtractor.MODEL_ID, modelInfo.getId().getPrettyFormat());
    basicFields.put(BasicIndexFieldExtractor.MODEL_NAME_SEARCHABLE, tokenizeModelIDForSearch(modelInfo.getId().getPrettyFormat()));
    basicFields.put(BasicIndexFieldExtractor.MODEL_TYPE, modelInfo.getType().toString());
    basicFields.put(BasicIndexFieldExtractor.AUTHOR, modelInfo.getAuthor());
    basicFields.put(BasicIndexFieldExtractor.MODIFIED_BY, modelInfo.getLastModifiedBy());
    basicFields.put(BasicIndexFieldExtractor.DISPLAY_NAME, modelInfo.getDisplayName());
    basicFields.put(BasicIndexFieldExtractor.DESCRIPTION, modelInfo.getDescription());
    basicFields.put(BasicIndexFieldExtractor.STATE, modelInfo.getState());
    basicFields.put(BasicIndexFieldExtractor.MODEL_HASIMAGE, Boolean.toString(modelInfo.isHasImage()));
    basicFields.put(BasicIndexFieldExtractor.MODEL_CREATIONDATE, Long.toString(modelInfo.getCreationDate().getTime()));
    basicFields.put(BasicIndexFieldExtractor.VISIBILITY, modelInfo.getVisibility());
    basicFields.put(BasicIndexFieldExtractor.NAMESPACE, modelInfo.getId().getNamespace());
    basicFields.put(BasicIndexFieldExtractor.VERSION, modelInfo.getId().getVersion());

    return basicFields;
  }

  @Override
  public Map<String, FieldType> getFields() {
    Map<String, FieldType> basicFields = new HashMap<>();
    
    basicFields.put(BasicIndexFieldExtractor.MODEL_ID, FieldType.KEY);
    basicFields.put(BasicIndexFieldExtractor.MODEL_NAME_SEARCHABLE, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.MODEL_TYPE, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.AUTHOR, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.MODIFIED_BY, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.DISPLAY_NAME, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.DESCRIPTION, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.STATE, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.VISIBILITY, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.MODEL_HASIMAGE, FieldType.KEY);
    basicFields.put(BasicIndexFieldExtractor.MODEL_CREATIONDATE, FieldType.KEY);
    basicFields.put(BasicIndexFieldExtractor.NAMESPACE, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.VERSION, FieldType.TEXT);

    return basicFields;
  }

  /**
   * Tokenizes a model version string into a whitespace-separated string, with the following rules:
   * <ul>
   *   <li>
   *     Given the following example: {@literal vorto.private.test:MyModel:1.0.0}
   *   </li>
   *   <li>
   *     The resulting string will be: {@literal vorto private test vorto.private.test MyModel 1.0.0}
   *   </li>
   *   <li>
   *     The id is first split by colon ({@literal :}, and every token added, separated by a whitespace
   *   </li>
   *   <li>
   *     The dot-separated namespace is added both as a split by dot ({@literal .}) between its
   *     tokens, then also as-is
   *   </li>
   * </ul>
   * @param modelId
   * @return
   */
  private static String tokenizeModelIDForSearch(String modelId) {
    if (Objects.isNull(modelId)) {
      LOGGER.warn("Attempting to process an null model id.");
      return "";
    }
    String trimmed = modelId.trim();
    if (trimmed.isEmpty()) {
      LOGGER.warn("Attempting to process an empty model id.");
      return "";
    }
    String[] colonSplit = trimmed.split(":");
    if (colonSplit.length > 3) {
      LOGGER.warn("Attempting to process invalid model id (more than 3 colon-separated tokens).");
      return modelId;
    }
    StringBuilder result = new StringBuilder();
    // not handling edge case with invalid namespace per previous implementation
    for (String childToken: colonSplit[0].split("\\.")) {
      result.append(childToken).append(" ");
    }
    for (String parentToken: colonSplit) {
      result.append(parentToken).append(" ");
    }
    // deleting last whitespace
    result.deleteCharAt(result.length() - 1);
    return result.toString();
  }

}
