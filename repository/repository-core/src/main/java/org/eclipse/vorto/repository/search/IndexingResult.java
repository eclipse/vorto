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
package org.eclipse.vorto.repository.search;

import java.util.HashMap;
import java.util.Map;

public class IndexingResult {
  private Map<String, Integer> indexedNamespaces = new HashMap<>();

  public Map<String, Integer> getIndexedNamespaces() {
    return indexedNamespaces;
  }
  
  public int getNumberOfNamespaces() {
    return indexedNamespaces.size();
  }
  
  public int getTotalNumberOfIndexedModels() {
    return indexedNamespaces.entrySet().stream().mapToInt(entry -> entry.getValue()).sum();
  }

  public void setIndexedNamespaces(Map<String, Integer> indexedNamespaces) {
    this.indexedNamespaces = indexedNamespaces;
  }
  
  public void addIndexedNamespace(String namespace, int numModels) {
    indexedNamespaces.put(namespace, numModels);
  }
}
