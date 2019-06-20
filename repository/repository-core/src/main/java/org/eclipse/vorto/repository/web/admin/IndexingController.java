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
package org.eclipse.vorto.repository.web.admin;

import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.indexing.IIndexingService;
import org.eclipse.vorto.repository.core.indexing.IndexingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexingController {

  @Autowired
  private IIndexingService indexingServices;
  
  @Autowired
  private IModelRepositoryFactory repositoryFactory;
  
  @RequestMapping(value = "/rest/reindex", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public ResponseEntity<IndexingResult> reindexModels() {
    IndexingResult result = indexingServices.reindexAllModels(repositoryFactory.getModelSearchService().search(""));
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
  
}
