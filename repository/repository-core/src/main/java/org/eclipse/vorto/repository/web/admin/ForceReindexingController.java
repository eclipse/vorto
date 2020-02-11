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
package org.eclipse.vorto.repository.web.admin;

import org.eclipse.vorto.repository.search.IIndexingService;
import org.eclipse.vorto.repository.search.IndexingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Forces re-indexing all model. Presently useful only for ElasticSearch indices. <br/>
 * As opposed to {@link IndexingController}, this controller will also force-recreate mappings. <br/>
 * This is rarely useful, i.e. only when a the mapping has been changed (e.g. a field with a different
 * type).<br/>
 * The Elastic Search service will create a temporary index with the up-to-date mapping, delete
 * and re-create the Vorto index with the correct mapping, and re-index all model from repository.<br/>
 * In case the simple search is in use instead, this will act exactly the same as {@link IndexingController}
 * - at the time, that does nothing in simple search either.
 * @author mena-bosch
 */
@RestController
public class ForceReindexingController {
  @Autowired
  private IIndexingService indexingServices;

  @RequestMapping(value = "/rest/forcereindex", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public ResponseEntity<IndexingResult> reindexModels() {
    IndexingResult result = indexingServices.forceReindexAllModels();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
