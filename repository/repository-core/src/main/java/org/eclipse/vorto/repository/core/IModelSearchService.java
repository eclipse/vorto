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
package org.eclipse.vorto.repository.core;

import java.util.List;
import java.util.Map;

/**
 * Provides model retrieval across tenants
 * 
 * @author ERM1SGP
 *
 */
public interface IModelSearchService {
  
  /**
   * 
   * @param expression
   * @return a map whose string is the tenantId where the model list comes from
   */
  Map<String, List<ModelInfo>> search(String expression); 
  
}
