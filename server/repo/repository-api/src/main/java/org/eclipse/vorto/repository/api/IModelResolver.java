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
package org.eclipse.vorto.repository.api;

import java.util.concurrent.CompletableFuture;

import org.eclipse.vorto.repository.api.resolver.ResolveQuery;

public interface IModelResolver {

	/**
	 * Resolves a model for the given query
	 * @param query containing criteria for resolving the model
	 * @return model info or null, if no model can be found for the given query
	 */
	CompletableFuture<ModelId> resolve(ResolveQuery query);
}
