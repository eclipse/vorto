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
package org.eclipse.vorto.repository.web.core;

import io.swagger.annotations.Api;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Used by the model creation dialog to fetch all released models by a certain namespace.
 * TODO: Extend API Search Service to support better search criteria, which would make this implemention obsolete!
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value = "/search")
@RestController("InternalModelSearchController")
@RequestMapping(value = "/rest/search")
public class ModelSearchController extends AbstractRepositoryController {

  @Autowired
  private ISearchService searchService;
  
  @RequestMapping(value = "/public", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_USER')")
  public Collection<ModelInfo> getPublicModels(@RequestParam("namespace") String namespace) {
    List<ModelInfo> models = searchService.search("visibility:public");
    if (namespace != null) {
      models = models.stream().filter(model -> model.getId().getNamespace().startsWith(namespace)).collect(Collectors.toList());
    }
    return models;
  }
}
