/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.web;

import org.eclipse.vorto.http.model.ModelIdDto;
import org.eclipse.vorto.repository.internal.resolver.ModelIdResolverFactory;
import org.eclipse.vorto.repository.internal.resolver.UnknownModelIdResolverException;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.resolver.IModelIdResolver;
import org.eclipse.vorto.repository.service.ModelNotFoundException;
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
@RequestMapping(value = "/rest/resolver")
@Api(value="/resolve", description="Resolve information models by mapped platform attributes")
public class ModelResolveController {

	@Autowired
	private ModelIdResolverFactory resolverFactory;
	
	@ApiOperation(value = "Resolves a vorto model by a platform specific object identifier, defined in model mappings")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Resolver not found for specified generator service key")})
	@RequestMapping(value = "/{serviceKey}/{id}", method = RequestMethod.GET)
	public ModelIdDto resolve(@ApiParam(value = "Generator service key, e.g. LWM2M", required = true) @PathVariable("serviceKey") final String serviceKey, @ApiParam(value = "Platform specific id defined in mappings", required = true) @PathVariable("id") final String id) throws Exception{
		IModelIdResolver resolver = resolverFactory.getResolver(serviceKey);
		
		ModelId resolvedId = resolver.resolve(id);
		
		if (resolvedId != null) {
			return ModelDtoFactory.createDto(resolvedId);
		} else {
			throw new ModelNotFoundException("No Model found");
		}
	}
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "Model not found.")  // 404
    @ExceptionHandler(ModelNotFoundException.class)
    public void NotFound(final ModelNotFoundException ex){
		// do logging
    }
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "Unknown resolver.")  // 404
    @ExceptionHandler(UnknownModelIdResolverException.class)
    public void UnknownModelIdResolver(final UnknownModelIdResolverException ex){
		// do logging
    }
}
