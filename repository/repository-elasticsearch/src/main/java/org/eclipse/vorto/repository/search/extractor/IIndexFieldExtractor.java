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

import java.util.Map;
import org.eclipse.vorto.repository.core.ModelInfo;

public interface IIndexFieldExtractor {
  
  /**
   * Extracts fields from a modelInfo to be indexed
   * @param modelInfo
   * @return a map where key is the field name and value is the value of the field
   */
  Map<String, String> extractFields(ModelInfo modelInfo);
  
  /**
   * Returns a map with keys being the field names and values being the field type
   * @return
   */
  Map<String, FieldType> getFields();
  
  public enum FieldType {
    /**
     * Cannot be searched with wildcards. Search expression must match the key
     */
    KEY, 
    /**
     * Can be searched with wildcards
     */
    TEXT
  }
}
