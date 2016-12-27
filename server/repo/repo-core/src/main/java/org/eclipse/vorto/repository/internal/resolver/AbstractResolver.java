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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.eclipse.vorto.http.model.ModelType;
import org.eclipse.vorto.repository.model.IModelContent;
import org.eclipse.vorto.repository.resolver.IModelIdResolver;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.service.IModelRepository.ContentType;

public abstract class AbstractResolver implements IModelIdResolver {

	protected IModelRepository repository;
	
	private String serviceKey;
	
	public AbstractResolver(IModelRepository repository, String serviceKey) {
		this.repository = repository;
		this.serviceKey = serviceKey;
	}
	
	@Override
	public ModelId resolve(String id) {
		List<ModelResource> mappings = this.repository.search(ModelType.Mapping.name());
		Optional<ModelId> foundId = mappings.stream()
													.filter(resource -> matchesServiceKey(resource))
													.map(r -> doResolve(r,id))
													.filter(modelId -> Objects.nonNull(modelId)).findFirst();
		return foundId.isPresent() ? foundId.get() : null;
	}
	
	private boolean matchesServiceKey(ModelResource resource) {
		IModelContent content = this.repository.getModelContent(resource.getId(), ContentType.DSL);
		return ((MappingModel)content.getModel()).getTargetPlatform().equals(this.serviceKey);
	}
	
	protected abstract ModelId doResolve(ModelResource mappingModelResource, String id);

}
