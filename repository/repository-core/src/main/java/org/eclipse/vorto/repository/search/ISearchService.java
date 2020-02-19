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

import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;

public interface ISearchService {

  /**
   * Searches all public models or models for which the calling user is member/collaborator of.
   * 
   * @param expression The search expression
   * @return models matching the search expression
   */
  List<ModelInfo> search(String expression);
  
  /**
   * Searches all public models or models for which the calling user is member/collaborator of, but uses the
   * user context of @userContext
   * @param searchExpression The search expression
   * @param userContext The user context with which to execute this query
   * @return
   */
  List<ModelInfo> search(String searchExpression, IUserContext userContext);
}
