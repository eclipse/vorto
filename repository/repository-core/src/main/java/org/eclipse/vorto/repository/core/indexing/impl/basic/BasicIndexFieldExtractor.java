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
package org.eclipse.vorto.repository.core.indexing.impl.basic;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.indexing.impl.IIndexFieldExtractor;

public class BasicIndexFieldExtractor implements IIndexFieldExtractor {

  private static final String PRIVATE = "private";
  
  @Override
  public Map<String, String> extractFields(ModelInfo modelInfo) {
    Map<String, String> basicFields = new HashMap<>();
    
    basicFields.put(BasicIndexFieldSupplier.MODEL_ID, modelInfo.getId().getPrettyFormat());
    basicFields.put(BasicIndexFieldSupplier.MODEL_TYPE, modelInfo.getType().toString());
    basicFields.put(BasicIndexFieldSupplier.AUTHOR, modelInfo.getAuthor());
    basicFields.put(BasicIndexFieldSupplier.DISPLAY_NAME, modelInfo.getDisplayName());
    basicFields.put(BasicIndexFieldSupplier.DESCRIPTION, modelInfo.getDescription());
    basicFields.put(BasicIndexFieldSupplier.STATE, modelInfo.getState());
    basicFields.put(BasicIndexFieldSupplier.VISIBILITY, PRIVATE);
    
    return basicFields;
  }

}
