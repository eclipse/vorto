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

import java.util.stream.Collectors;

import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.http.model.AbstractModelDto;
import org.eclipse.vorto.http.model.EntityModelDto;
import org.eclipse.vorto.http.model.EnumLiteralDto;
import org.eclipse.vorto.http.model.EnumModelDto;
import org.eclipse.vorto.http.model.FunctionblockModelDto;
import org.eclipse.vorto.http.model.InfomodelDto;
import org.eclipse.vorto.http.model.ModelEventDto;
import org.eclipse.vorto.http.model.ModelIdDto;
import org.eclipse.vorto.http.model.ModelPropertyDto;
import org.eclipse.vorto.http.model.ModelResourceDto;
import org.eclipse.vorto.http.model.ModelTypeDto;
import org.eclipse.vorto.http.model.OperationDto;
import org.eclipse.vorto.http.model.ParamDto;
import org.eclipse.vorto.http.model.ReturnTypeDto;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;

public class ModelDtoFactory {

	public static ModelResourceDto createDto(ModelResource resource) {
		ModelResourceDto dto = new ModelResourceDto(createDto(resource.getId()), ModelTypeDto.valueOf(resource.getModelType().name()));
		dto.setAuthor(resource.getAuthor());
		dto.setCreationDate(resource.getCreationDate());
		dto.setDescription(resource.getDescription());
		dto.setDisplayName(resource.getDisplayName());
		dto.setHasImage(resource.isHasImage());
		dto.setReferencedBy(resource.getReferencedBy().stream().map(r -> createDto(r)).collect(Collectors.toList()));
		dto.setReferences(resource.getReferences().stream().map(r -> createDto(r)).collect(Collectors.toList()));
		dto.setSupportedTargetPlatforms(resource.getSupportedTargetPlatforms());
		return dto;
	}
	
	public static ModelIdDto createDto(ModelId modelId) {
		return new ModelIdDto(modelId.getName(), modelId.getNamespace(), modelId.getVersion());
	}
	
	public static AbstractModelDto createResource(Model model) {
		if (model instanceof InformationModel) {
			return createResource((InformationModel)model);
		} else if (model instanceof FunctionblockModel) {
			return createResource((FunctionblockModel)model);
		} else if (model instanceof Entity) {
			return createResource((Entity)model);
		} else if (model instanceof Enum) {
			return createResource((Enum)model);
		} else if (model instanceof MappingModel) {
			return new ModelResourceDto(new ModelIdDto(model.getName(), model.getNamespace(), model.getVersion()),ModelTypeDto.Mapping);
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	public static InfomodelDto createResource(InformationModel model) {
		InfomodelDto infoResource = new InfomodelDto(new ModelIdDto(model.getName(),model.getNamespace(),model.getVersion()), ModelTypeDto.InformationModel);
		
		for (FunctionblockProperty property : model.getProperties()) {
			infoResource.getFunctionblocks().add(createProperty(property));
		}
		
		infoResource.setDescription(model.getDescription());
		infoResource.setDisplayName(model.getDisplayname());
		infoResource.setReferences(model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));
				
		return infoResource;
	}
	
	private static ModelIdDto createModelId(Model model) {
		return new ModelIdDto(model.getName(), model.getNamespace(), model.getVersion());
	}
	
	private static ModelIdDto createModelId(ModelReference reference) {
		org.eclipse.vorto.repository.model.ModelId modelId = org.eclipse.vorto.repository.model.ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion());
		return new ModelIdDto(modelId.getName(),modelId.getNamespace(),modelId.getVersion());
	}

	public static FunctionblockModelDto createResource(FunctionblockModel model) {
		FunctionblockModelDto resource = new FunctionblockModelDto(new ModelIdDto(model.getName(),model.getNamespace(),model.getVersion()), ModelTypeDto.Functionblock);
		resource.setDescription(model.getDescription());
		resource.setDisplayName(model.getDisplayname());
		resource.setReferences(model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));
		
		if (model.getFunctionblock().getConfiguration() != null) {
			resource.setConfigurationProperties(model.getFunctionblock().getConfiguration().getProperties().stream().map(p -> createProperty(p)).collect(Collectors.toList()));
		}
		if (model.getFunctionblock().getStatus() != null) {
			resource.setStatusProperties(model.getFunctionblock().getStatus().getProperties().stream().map(p -> createProperty(p)).collect(Collectors.toList()));
		}
		
		if (model.getFunctionblock().getFault() != null) {
			resource.setFaultProperties(model.getFunctionblock().getFault().getProperties().stream().map(p -> createProperty(p)).collect(Collectors.toList()));
		}
		
		if (model.getFunctionblock().getEvents() != null) {
			resource.setEvents(model.getFunctionblock().getEvents().stream().map(e -> createEvent(e)).collect(Collectors.toList()));
		}
		
		if (model.getFunctionblock().getOperations() != null) {
			resource.setOperations(model.getFunctionblock().getOperations().stream().map(o -> createOperation(o)).collect(Collectors.toList()));
		}
		
		return resource;
	}
	
	private static OperationDto createOperation(org.eclipse.vorto.core.api.model.functionblock.Operation o) {
		OperationDto operation = new OperationDto();
		operation.setBreakable(o.isBreakable());
		operation.setDescription(o.getDescription());
		operation.setName(o.getName());
		operation.setParams(o.getParams().stream().map(p -> createParam(p)).collect(Collectors.toList()));
		
		if (o.getReturnType() != null) {
			ReturnTypeDto returnType = new ReturnTypeDto();
			returnType.setMultiple(o.getReturnType().isMultiplicity());
			if (o.getReturnType() instanceof ReturnPrimitiveType) {
				returnType.setPrimitive(true);
				returnType.setType(((ReturnPrimitiveType)o.getReturnType()).getReturnType());
			} else {
				returnType.setPrimitive(false);
				returnType.setType(createModelId(((ReturnObjectType)o.getReturnType()).getReturnType()));
			}
			operation.setResult(returnType);
		}
		return operation;
	}
	
	private static ParamDto createParam(org.eclipse.vorto.core.api.model.functionblock.Param p) {
		ParamDto param = new ParamDto();
		param.setDescription(p.getDescription());
		param.setMultiple(p.isMultiplicity());
		param.setName(p.getName());
		if (p instanceof PrimitiveParam) {			
			param.setPrimitive(true);
			param.setType(((PrimitiveParam)p).getType());
		} else {
			param.setPrimitive(false);
			param.setType(createModelId(((RefParam)p).getType()));
		}
		return param;
	}
	
	
	private static ModelPropertyDto createProperty(FunctionblockProperty property) {
		ModelPropertyDto p = new ModelPropertyDto();
		p.setDescription(property.getDescription());
		p.setName(property.getName());
		p.setPrimitive(false);
		p.setType(createModelId(property.getType()));
		return p;
	}
	
	private static ModelPropertyDto createProperty(Property property) {
		ModelPropertyDto p = new ModelPropertyDto();
		p.setDescription(property.getDescription());
		p.setMandatory(property.getPresence() != null ? property.getPresence().isMandatory() : true);
		p.setMultiple(property.isMultiplicity());
		p.setName(property.getName());
		if (property.getType() instanceof PrimitivePropertyType) {
			p.setPrimitive(true);
			p.setType(((PrimitivePropertyType)property.getType()).getType());
		} else {
			p.setPrimitive(false);
			p.setType(createModelId(((ObjectPropertyType)property.getType()).getType()));
		}
		return p;
	}
	
	private static ModelEventDto createEvent(Event event) {
		ModelEventDto modelEvent = new ModelEventDto();
		modelEvent.setName(event.getName());
		modelEvent.setProperties(event.getProperties().stream().map(p -> createProperty(p)).collect(Collectors.toList()));
		return modelEvent;
	}
		
	public static EntityModelDto createResource(Entity model) {
		EntityModelDto resource = new EntityModelDto(new ModelIdDto(model.getName(),model.getNamespace(),model.getVersion()), ModelTypeDto.Datatype);
		resource.setDescription(model.getDescription());
		resource.setDisplayName(model.getDisplayname());
		resource.setReferences(model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));
		resource.setProperties(model.getProperties().stream().map(p -> createProperty(p)).collect(Collectors.toList()));
		return resource;
	}
	
	public static EnumModelDto createResource(Enum model) {
		EnumModelDto resource = new EnumModelDto(new ModelIdDto(model.getName(),model.getNamespace(),model.getVersion()), ModelTypeDto.Datatype);
		resource.setDescription(model.getDescription());
		resource.setDisplayName(model.getDisplayname());
		resource.setReferences(model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));
		resource.setLiterals(model.getEnums().stream().map(p -> createLiteral(p)).collect(Collectors.toList()));
		return resource;
	}
	
	private static EnumLiteralDto createLiteral(org.eclipse.vorto.core.api.model.datatype.EnumLiteral literal) {
		return new EnumLiteralDto(literal.getName(), literal.getDescription());
	}
}
