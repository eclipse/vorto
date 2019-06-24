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
import org.eclipse.vorto.repository.core.indexing.impl.IIndexFieldCreator;
import org.eclipse.vorto.repository.core.indexing.impl.IndexingService;

public class BasicIndexFieldCreator implements IIndexFieldCreator {

  @Override
  public Map<String, String> getFields() {
    Map<String, String> basicFields = new HashMap<>();
    
    basicFields.put(BasicIndexFieldSupplier.MODEL_ID, IndexingService.KEYWORD);
    basicFields.put(BasicIndexFieldSupplier.MODEL_TYPE, IndexingService.KEYWORD);
    basicFields.put(BasicIndexFieldSupplier.AUTHOR, IndexingService.KEYWORD);
    basicFields.put(BasicIndexFieldSupplier.DISPLAY_NAME, IndexingService.TEXT);
    basicFields.put(BasicIndexFieldSupplier.DESCRIPTION, IndexingService.TEXT);
    basicFields.put(BasicIndexFieldSupplier.STATE, IndexingService.KEYWORD);
    basicFields.put(BasicIndexFieldSupplier.VISIBILITY, IndexingService.KEYWORD);
    
    return basicFields;
  }

}
