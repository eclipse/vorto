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

package org.eclipse.vorto.repository.api.impl;

import java.util.concurrent.CompletableFuture;

import org.apache.http.client.HttpClient;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.IModelResolver;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.resolver.ResolveQuery;

public class DefaultModelResolver extends ImplementationBase implements IModelResolver {
	
	private IModelRepository modelRepository;
	
	public DefaultModelResolver(HttpClient httpClient, RequestContext requestContext, IModelRepository modelRepo) {
		super(httpClient, requestContext);
		this.modelRepository = modelRepo;
	}
	
	@Override
	public CompletableFuture<ModelInfo> resolve(ResolveQuery query) {
		String resolveUrl = String.format("%s/rest/resolver/%s/%s/%s/%s", getRequestContext().getBaseUrl(), 
				query.getTargetPlatformKey(), query.getStereoType(), query.getAttributeId(), query.getAttributeValue());
		return requestAndTransform(resolveUrl, transformToClass(ModelId.class))
			.thenCompose(modelRepository::getById);
	}

}
