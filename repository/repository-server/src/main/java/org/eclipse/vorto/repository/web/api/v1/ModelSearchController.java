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
package org.eclipse.vorto.repository.web.api.v1;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value = "/search")
@RestController("modelSearchController")
public class ModelSearchController extends AbstractRepositoryController {
	
	@Value("${server.config.authenticatedSearchMode:#{false}}")
	private boolean authenticatedSearchMode = false;
	
	@ApiOperation(value = "Finds models by free-text search expressions")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of search result"), @ApiResponse(code = 400, message = "Malformed search expression")})
	@RequestMapping(value = "/api/v1/{tenant}/search/models", method = RequestMethod.GET)
	@PreAuthorize("!@modelSearchController.isAuthenticatedSearchMode() || isAuthenticated()")
	public List<ModelInfo> searchByExpression(
			@ApiParam(value = "The owning tenant", required = true) final @PathVariable String tenant,
			@ApiParam(value = "a free-text search expression", required = true) @RequestParam("expression") String expression)
			throws UnsupportedEncodingException {
		IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName());
		List<ModelInfo> modelResources = modelRepository.search(URLDecoder.decode(expression, "utf-8"));
		return modelResources.stream()
				.map(resource -> ModelDtoFactory.createDto(resource, userContext)).sorted(new Comparator<ModelInfo>() {

					@Override
					public int compare(ModelInfo o1, ModelInfo o2) {
						return o1.getCreationDate().after(o2.getCreationDate()) ? -1 : +1;
					}
					
				}).collect(Collectors.toList());
	}
	
	public boolean isAuthenticatedSearchMode() {
		return authenticatedSearchMode;
	}

	public void setAuthenticatedSearchMode(boolean authenticatedSearchMode) {
		this.authenticatedSearchMode = authenticatedSearchMode;
	}
}
