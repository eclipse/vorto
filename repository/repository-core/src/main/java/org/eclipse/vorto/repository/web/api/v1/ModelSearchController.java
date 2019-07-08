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
package org.eclipse.vorto.repository.web.api.v1;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */

@RestController("modelSearchController")
@RequestMapping(value = "/api/v1/search")
public class ModelSearchController extends AbstractRepositoryController {
  
  @Autowired
  private ISearchService searchService;
  
  @RequestMapping(value = "/models", method = RequestMethod.GET,
	      produces = "application/json")
  @CrossOrigin(origins = "https://www.eclipse.org/vorto")
  public List<ModelInfo> searchByExpression(
      @ApiParam(value = "a free-text search expression",
          required = true) @RequestParam("expression") String expression)
      throws UnsupportedEncodingException {
        
    List<ModelInfo> result = searchService.search(URLDecoder.decode(expression, "utf-8"));

    return result.stream().sorted(new Comparator<ModelInfo>() {
      public int compare(ModelInfo o1, ModelInfo o2) {
        return o1.getCreationDate().after(o2.getCreationDate()) ? -1 : +1;
      }
    }).collect(Collectors.toList());
  }
   
  public ISearchService getSearchService() {
    return searchService;
  }

  public void setSearchService(ISearchService searchService) {
    this.searchService = searchService;
  }
  
  
}
