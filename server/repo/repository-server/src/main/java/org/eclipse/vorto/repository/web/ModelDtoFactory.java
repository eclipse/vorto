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
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
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
import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.EntityModel;
import org.eclipse.vorto.repository.api.content.EnumLiteral;
import org.eclipse.vorto.repository.api.content.EnumModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelEvent;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Operation;
import org.eclipse.vorto.repository.api.content.Param;
import org.eclipse.vorto.repository.api.content.ReturnType;

public class ModelDtoFactory {

	public static ModelInfo createDto(ModelInfo resource) {
		ModelInfo dto = new ModelInfo(createDto(resource.getId()), ModelType.valueOf(resource.getType().name()));
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
	
	public static ModelId createDto(ModelId modelId) {
		return new ModelId(modelId.getName(), modelId.getNamespace(), modelId.getVersion());
	}
	
	public static AbstractModel createResource(Model model) {
		if (model instanceof InformationModel) {
			return createResource((InformationModel)model);
		} else if (model instanceof FunctionblockModel) {
			return createResource((FunctionblockModel)model);
		} else if (model instanceof Entity) {
			return createResource((Entity)model);
		} else if (model instanceof Enum) {
			return createResource((Enum)model);
		} else if (model instanceof MappingModel) {
			return new ModelInfo(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),ModelType.Mapping);
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	public static Infomodel createResource(InformationModel model) {
		Infomodel infoResource = new Infomodel(new ModelId(model.getName(),model.getNamespace(),model.getVersion()), ModelType.InformationModel);
		
		for (FunctionblockProperty property : model.getProperties()) {
			infoResource.getFunctionblocks().add(createProperty(property));
		}
		
		infoResource.setDescription(model.getDescription());
		infoResource.setDisplayName(model.getDisplayname());
		infoResource.setReferences(model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));
				
		return infoResource;
	}
	
	private static ModelId createModelId(Model model) {
		return new ModelId(model.getName(), model.getNamespace(), model.getVersion());
	}
	
	private static ModelId createModelId(ModelReference reference) {
		ModelId modelId = ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion());
		return new ModelId(modelId.getName(),modelId.getNamespace(),modelId.getVersion());
	}

	public static org.eclipse.vorto.repository.api.content.FunctionblockModel createResource(FunctionblockModel model) {
		org.eclipse.vorto.repository.api.content.FunctionblockModel resource = new org.eclipse.vorto.repository.api.content.FunctionblockModel(new ModelId(model.getName(),model.getNamespace(),model.getVersion()), ModelType.Functionblock);
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
	
	private static Operation createOperation(org.eclipse.vorto.core.api.model.functionblock.Operation o) {
		Operation operation = new Operation();
		operation.setBreakable(o.isBreakable());
		operation.setDescription(o.getDescription());
		operation.setName(o.getName());
		operation.setParams(o.getParams().stream().map(p -> createParam(p)).collect(Collectors.toList()));
		
		if (o.getReturnType() != null) {
			ReturnType returnType = new ReturnType();
			returnType.setMultiple(o.getReturnType().isMultiplicity());
			if (o.getReturnType() instanceof ReturnPrimitiveType) {
				returnType.setPrimitive(true);
				PrimitiveType pt = ((ReturnPrimitiveType)o.getReturnType()).getReturnType();
				returnType.setType(org.eclipse.vorto.repository.api.content.PrimitiveType.valueOf(pt.name()));
			} else {
				returnType.setPrimitive(false);
				returnType.setType(createModelId(((ReturnObjectType)o.getReturnType()).getReturnType()));
			}
			operation.setResult(returnType);
		}
		return operation;
	}
	
	private static Param createParam(org.eclipse.vorto.core.api.model.functionblock.Param p) {
		Param param = new Param();
		param.setDescription(p.getDescription());
		param.setMultiple(p.isMultiplicity());
		param.setName(p.getName());
		if (p instanceof PrimitiveParam) {			
			param.setPrimitive(true);
			PrimitiveType pt = ((PrimitiveParam)p).getType();
			param.setType(org.eclipse.vorto.repository.api.content.PrimitiveType.valueOf(pt.name()));
		} else {
			param.setPrimitive(false);
			param.setType(createModelId(((RefParam)p).getType()));
		}
		return param;
	}
	
	
	private static ModelProperty createProperty(FunctionblockProperty property) {
		ModelProperty p = new ModelProperty();
		p.setDescription(property.getDescription());
		p.setName(property.getName());
		p.setType(createModelId(property.getType()));
		return p;
	}
	
	private static ModelProperty createProperty(Property property) {
		ModelProperty p = new ModelProperty();
		p.setDescription(property.getDescription());
		p.setMandatory(property.getPresence() != null ? property.getPresence().isMandatory() : true);
		p.setMultiple(property.isMultiplicity());
		p.setName(property.getName());
		if (property.getType() instanceof PrimitivePropertyType) {
			PrimitiveType pt = ((PrimitivePropertyType)property.getType()).getType();
			p.setType(org.eclipse.vorto.repository.api.content.PrimitiveType.valueOf(pt.name()));
		} else {
			p.setType(createModelId(((ObjectPropertyType)property.getType()).getType()));
		}
		return p;
	}
	
	private static ModelEvent createEvent(Event event) {
		ModelEvent modelEvent = new ModelEvent();
		modelEvent.setName(event.getName());
		modelEvent.setProperties(event.getProperties().stream().map(p -> createProperty(p)).collect(Collectors.toList()));
		return modelEvent;
	}
		
	public static EntityModel createResource(Entity model) {
		EntityModel resource = new EntityModel(new ModelId(model.getName(),model.getNamespace(),model.getVersion()), ModelType.Datatype);
		resource.setDescription(model.getDescription());
		resource.setDisplayName(model.getDisplayname());
		resource.setReferences(model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));
		resource.setProperties(model.getProperties().stream().map(p -> createProperty(p)).collect(Collectors.toList()));
		return resource;
	}
	
	public static EnumModel createResource(Enum model) {
		EnumModel resource = new EnumModel(new ModelId(model.getName(),model.getNamespace(),model.getVersion()), ModelType.Datatype);
		resource.setDescription(model.getDescription());
		resource.setDisplayName(model.getDisplayname());
		resource.setReferences(model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));
		resource.setLiterals(model.getEnums().stream().map(p -> createLiteral(p)).collect(Collectors.toList()));
		return resource;
	}
	
	private static EnumLiteral createLiteral(org.eclipse.vorto.core.api.model.datatype.EnumLiteral literal) {
		return new EnumLiteral(literal.getName(), literal.getDescription());
	}
}
