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

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.repository.model.IModelContent;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.ModelType;
import org.eclipse.vorto.repository.resolver.IModelIdResolver;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.service.IModelRepository.ContentType;

/**
 * The LWM2M Resolver resolves a function block model id by a given LWM2M object identifier defined in the function block mappings.
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class Lwm2mObjectIdResolver implements IModelIdResolver{

	private IModelRepository repository;
	
	public Lwm2mObjectIdResolver(IModelRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public ModelId resolve(String id) {
		List<ModelResource> mappings = this.repository.search(ModelType.Mapping.name());
		Optional<ModelId> foundId = mappings.stream()
													.filter(resource -> isLwM2MFunctionBlockMapping(resource))
													.map(r -> getModelId(r,id))
													.filter(modelId -> Objects.nonNull(modelId)).findFirst();
		return foundId.isPresent() ? foundId.get() : null;
	}
	
	private boolean isLwM2MFunctionBlockMapping(ModelResource resource) {
		IModelContent content = this.repository.getModelContent(resource.getId(), ContentType.DSL);
		return ((MappingModel)content.getModel()).getTargetPlatform().equals("lwm2m");
	}
	
	private ModelId getModelId(ModelResource mappingModelResource, String id) {
		IModelContent content = this.repository.getModelContent(mappingModelResource.getId(), ContentType.DSL);
		MappingModel mappingModel = (MappingModel)content.getModel();
		Optional<MappingRule> objectRule = mappingModel.getRules().stream().filter(rule -> rule.getTarget() instanceof StereoTypeTarget && ((StereoTypeTarget)rule.getTarget()).getName().equals("Object")).findFirst();							
		
		if (objectRule.isPresent()) {
			Optional<Attribute> objectIdAttribute = ((StereoTypeTarget)objectRule.get().getTarget()).getAttributes().stream().filter(attribute -> attribute.getName().equals("ObjectID")).findFirst();
			if (objectIdAttribute.isPresent() && objectIdAttribute.get().getValue().equals(id)) {
				return ModelId.fromReference(mappingModel.getReferences().get(0));
			}
		}
		return null;
	}

}
