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
package org.eclipse.vorto.repository.search.extractor;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.repository.core.ModelInfo;

public class BasicIndexFieldExtractor implements IIndexFieldExtractor {

  private static final String PRIVATE = "private";
  
  public static final String VISIBILITY = "visibility";

  public static final String STATE = "state";

  public static final String DESCRIPTION = "description";

  public static final String DISPLAY_NAME = "displayName";

  public static final String AUTHOR = "author";

  public static final String MODEL_TYPE = "modelType";

  public static final String MODEL_ID = "modelId";
  
  public static final String MODEL_HASIMAGE = "hasImage";
  
  public static final String MODEL_CREATIONDATE = "createdOn";
  
  @Override
  public Map<String, String> extractFields(ModelInfo modelInfo) {
    Map<String, String> basicFields = new HashMap<>();
    
    basicFields.put(BasicIndexFieldExtractor.MODEL_ID, modelInfo.getId().getPrettyFormat());
    basicFields.put(BasicIndexFieldExtractor.MODEL_TYPE, modelInfo.getType().toString());
    basicFields.put(BasicIndexFieldExtractor.AUTHOR, modelInfo.getAuthor());
    basicFields.put(BasicIndexFieldExtractor.DISPLAY_NAME, modelInfo.getDisplayName());
    basicFields.put(BasicIndexFieldExtractor.DESCRIPTION, modelInfo.getDescription());
    basicFields.put(BasicIndexFieldExtractor.STATE, modelInfo.getState());
    basicFields.put(BasicIndexFieldExtractor.MODEL_HASIMAGE, Boolean.toString(modelInfo.isHasImage()));
    basicFields.put(BasicIndexFieldExtractor.MODEL_CREATIONDATE, Long.toString(modelInfo.getCreationDate().getTime()));
    basicFields.put(BasicIndexFieldExtractor.VISIBILITY, modelInfo.getVisibility());
    
    return basicFields;
  }
  
  @Override
  public Map<String, FieldType> getFields() {
    Map<String, FieldType> basicFields = new HashMap<>();
    
    basicFields.put(BasicIndexFieldExtractor.MODEL_ID, FieldType.KEY);
    basicFields.put(BasicIndexFieldExtractor.MODEL_TYPE, FieldType.KEY);
    basicFields.put(BasicIndexFieldExtractor.AUTHOR, FieldType.KEY);
    basicFields.put(BasicIndexFieldExtractor.DISPLAY_NAME, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.DESCRIPTION, FieldType.TEXT);
    basicFields.put(BasicIndexFieldExtractor.STATE, FieldType.KEY);
    basicFields.put(BasicIndexFieldExtractor.VISIBILITY, FieldType.KEY);
    basicFields.put(BasicIndexFieldExtractor.MODEL_HASIMAGE, FieldType.KEY);
    basicFields.put(BasicIndexFieldExtractor.MODEL_CREATIONDATE, FieldType.KEY);
    return basicFields;
  }

}
