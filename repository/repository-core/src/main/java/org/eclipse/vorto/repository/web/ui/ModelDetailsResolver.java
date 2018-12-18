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
package org.eclipse.vorto.repository.web.ui;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelIdResolver;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.resolver.UnknownModelIdResolverException;
import org.eclipse.vorto.repository.web.core.dto.ResolveQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/resolve")
public class ModelDetailsResolver {

  @Autowired
  private IModelIdResolver resolver;

  @RequestMapping(value = "/{serviceKey}/{stereotype}/{attributeId}/{attributeValue}",
      method = RequestMethod.GET)
  public String resolve(@PathVariable("serviceKey") final String serviceKey,
      @PathVariable("stereotype") final String stereoType,
      @PathVariable("attributeId") final String attributeId,
      @PathVariable("attributeValue") final String attributeValue) throws Exception {
    ModelId resolvedId =
        resolver.resolve(new ResolveQuery(serviceKey, attributeId, attributeValue, stereoType));

    if (resolvedId != null) {
      return "redirect:/#/details/" + resolvedId.getNamespace() + "/" + resolvedId.getName() + "/"
          + resolvedId.getVersion();
    } else {
      throw new ModelNotFoundException("No Model found");
    }
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Model not found.") // 404
  @ExceptionHandler(ModelNotFoundException.class)
  public void NotFound(final ModelNotFoundException ex) {
    // do logging
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Unknown resolver.") // 404
  @ExceptionHandler(UnknownModelIdResolverException.class)
  public void UnknownModelIdResolver(final UnknownModelIdResolverException ex) {
    // do logging
  }
}
