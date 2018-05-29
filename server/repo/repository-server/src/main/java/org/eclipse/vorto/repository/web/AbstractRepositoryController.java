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

import org.eclipse.vorto.repository.api.exception.GenerationException;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class AbstractRepositoryController {

	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "Model not found.")  // 404
    @ExceptionHandler(ModelNotFoundException.class)
    public void NotFound(final ModelNotFoundException ex){
		// do logging
    }
	
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason = "Not authorized to view the model")  // 403
    @ExceptionHandler(NotAuthorizedException.class)
    public void unAuthorized(final NotAuthorizedException ex){
		// do logging
    }
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "Error during generation.")
    @ExceptionHandler(GenerationException.class)
    public void GeneratorProblem(final GenerationException ex){
		// do logging
    }
}
