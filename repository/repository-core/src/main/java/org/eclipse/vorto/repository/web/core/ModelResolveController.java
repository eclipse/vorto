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

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelIdResolver;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.resolver.UnknownModelIdResolverException;
import org.eclipse.vorto.repository.web.core.dto.ResolveQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/rest/{tenant}/models/resolvers")
@Api(value = "/resolve", description = "Resolve information models by mapped platform attributes")
public class ModelResolveController {

  @Autowired
  private IModelIdResolver resolver;

  @ApiOperation(
      value = "Resolves a vorto model by a platform specific object identifier, defined in model mappings")
  @ApiResponses(value = {
      @ApiResponse(code = 404, message = "Resolver not found for specified generator service key")})
  @RequestMapping(value = "/{serviceKey}/{stereoType}/{attributeId}/{attributeValue}",
      method = RequestMethod.GET)
  public ModelId resolve(
      @ApiParam(value = "Generator service key, e.g. LWM2M",
          required = true) @PathVariable("serviceKey") final String serviceKey,
      @ApiParam(value = "Platform specific stereotype name defined in mappings",
          required = true) @PathVariable("stereoType") final String stereoType,
      @ApiParam(value = "Platform specific attributeId defined in mappings",
          required = true) @PathVariable("attributeId") final String attributeId,
      @ApiParam(value = "Platform specific attributeValue defined in mappings",
          required = true) @PathVariable("attributeValue") final String attributeValue)
      throws Exception {


    ModelId resolvedId =
        resolver.resolve(new ResolveQuery(serviceKey, attributeId, attributeValue, stereoType));

    if (resolvedId != null) {
      return ModelDtoFactory.createDto(resolvedId);
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
