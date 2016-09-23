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
package org.eclipse.vorto.repository.internal.resolver;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.vorto.repository.resolver.IModelIdResolver;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelIdResolverFactory {
	
	@Autowired
	private IModelRepository repository;
	
	private Map<String, IModelIdResolver> resolvers = new HashMap<>();

	public IModelIdResolver getResolver(String serviceKey) {
		if (!resolvers.containsKey(serviceKey)) {
			throw new UnknownModelIdResolverException(serviceKey);
		}
		return resolvers.get(serviceKey);
	}
	
	@PostConstruct
	public void initResolvers() {
		resolvers.put("lwm2m", new Lwm2mObjectIdResolver(repository));
	}
}
