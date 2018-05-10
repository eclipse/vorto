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
package org.eclipse.vorto.repository.web.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.PropertyAttribute;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationSource;
import org.eclipse.vorto.core.api.model.mapping.EntitySource;
import org.eclipse.vorto.core.api.model.mapping.FaultSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockPropertySource;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.OperationSource;
import org.eclipse.vorto.core.api.model.mapping.ReferenceTarget;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.mapping.StatusSource;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.BooleanAttributeProperty;
import org.eclipse.vorto.repository.api.content.BooleanAttributePropertyType;
import org.eclipse.vorto.repository.api.content.Constraint;
import org.eclipse.vorto.repository.api.content.ConstraintType;
import org.eclipse.vorto.repository.api.content.EntityModel;
import org.eclipse.vorto.repository.api.content.EnumAttributeProperty;
import org.eclipse.vorto.repository.api.content.EnumAttributePropertyType;
import org.eclipse.vorto.repository.api.content.EnumLiteral;
import org.eclipse.vorto.repository.api.content.EnumModel;
import org.eclipse.vorto.repository.api.content.IPropertyAttribute;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelEvent;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Operation;
import org.eclipse.vorto.repository.api.content.Param;
import org.eclipse.vorto.repository.api.content.ReturnType;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.repository.core.IUserContext;

/**
 * Converts the EMF Model to POJO's
 *
 */
public class ModelDtoFactory {

	public static ModelInfo createDto(ModelInfo resource, IUserContext userContext) {
		ModelInfo dto = new ModelInfo(createDto(resource.getId()), ModelType.valueOf(resource.getType().name()));
		
		if (userContext.getHashedUsername().equals(resource.getAuthor())) {
			dto.setAuthor(userContext.getUsername());
		}
		
		dto.setCreationDate(resource.getCreationDate());
		dto.setDescription(resource.getDescription());
		dto.setDisplayName(resource.getDisplayName());
		dto.setHasImage(resource.isHasImage());
		dto.setReferencedBy(resource.getReferencedBy().stream().map(r -> createDto(r)).collect(Collectors.toList()));
		dto.setReferences(resource.getReferences().stream().map(r -> createDto(r)).collect(Collectors.toList()));
		dto.setPlatformMappings(resource.getPlatformMappings());
		return dto;
	}

	public static ModelId createDto(ModelId modelId) {
		return new ModelId(modelId.getName(), modelId.getNamespace(), modelId.getVersion());
	}

	public static AbstractModel createResource(Model model,Optional<MappingModel> mappingModel) {
		if (model instanceof InformationModel) {
			return createResource((InformationModel) model, mappingModel);
		} else if (model instanceof FunctionblockModel) {
			return createResource((FunctionblockModel) model,mappingModel);
		} else if (model instanceof Entity) {
			return createResource((Entity) model,mappingModel);
		} else if (model instanceof Enum) {
			return createResource((Enum) model,mappingModel);
		} else if (model instanceof MappingModel) {
			return new ModelInfo(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
					ModelType.Mapping);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public static Infomodel createResource(InformationModel model, Optional<MappingModel> mappingModel) {
		Infomodel infoResource = new Infomodel(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
				ModelType.InformationModel);

		for (FunctionblockProperty property : model.getProperties()) {
			infoResource.getFunctionblocks().add(createProperty(property,mappingModel));
		}

		infoResource.setDescription(model.getDescription());
		infoResource.setDisplayName(model.getDisplayname());
		infoResource.setReferences(
				model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));

		if (mappingModel.isPresent()) {
			MappingModel _mappingModel = mappingModel.get();
			infoResource.setTargetPlatformKey(_mappingModel.getTargetPlatform());
			for (MappingRule rule : getInfoModelRule(_mappingModel.getRules())) {
				if (rule.getTarget() instanceof StereoTypeTarget) {
					StereoTypeTarget target = (StereoTypeTarget)rule.getTarget();
					infoResource.addStereotype(Stereotype.create(target.getName(), convertAttributesToMap(target.getAttributes())));
				} else if (rule.getTarget() instanceof ReferenceTarget) {
					ReferenceTarget target = (ReferenceTarget)rule.getTarget();
					infoResource.setMappingReference(createModelId(target.getMappingModel()));
				}
			}
		}
		return infoResource;
	}
	
	private static List<MappingRule> getInfoModelRule(List<MappingRule> rules) {
		return rules.stream().filter(r -> r.getSources().get(0) instanceof InfoModelAttributeSource).collect(Collectors.toList());
	}

	private static ModelId createModelId(Model model) {
		return new ModelId(model.getName(), model.getNamespace(), model.getVersion());
	}

	private static ModelId createModelId(ModelReference reference) {
		ModelId modelId = ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion());
		return new ModelId(modelId.getName(), modelId.getNamespace(), modelId.getVersion());
	}

	public static org.eclipse.vorto.repository.api.content.FunctionblockModel createResource(FunctionblockModel model,Optional<MappingModel> mappingModel) {
		org.eclipse.vorto.repository.api.content.FunctionblockModel resource = new org.eclipse.vorto.repository.api.content.FunctionblockModel(
				new ModelId(model.getName(), model.getNamespace(), model.getVersion()), ModelType.Functionblock);
		resource.setDescription(model.getDescription());
		resource.setDisplayName(model.getDisplayname());
		resource.setReferences(
				model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));

		if (model.getFunctionblock().getConfiguration() != null) {
			resource.setConfigurationProperties(model.getFunctionblock().getConfiguration().getProperties().stream()
					.map(p -> createProperty(p,mappingModel)).collect(Collectors.toList()));
		}
		if (model.getFunctionblock().getStatus() != null) {
			resource.setStatusProperties(model.getFunctionblock().getStatus().getProperties().stream()
					.map(p -> createProperty(p,mappingModel)).collect(Collectors.toList()));
		}

		if (model.getFunctionblock().getFault() != null) {
			resource.setFaultProperties(model.getFunctionblock().getFault().getProperties().stream()
					.map(p -> createProperty(p,mappingModel)).collect(Collectors.toList()));
		}

		if (model.getFunctionblock().getEvents() != null) {
			resource.setEvents(model.getFunctionblock().getEvents().stream().map(e -> createEvent(e,mappingModel))
					.collect(Collectors.toList()));
		}

		if (model.getFunctionblock().getOperations() != null) {
			resource.setOperations(model.getFunctionblock().getOperations().stream().map(o -> createOperation(o,mappingModel))
					.collect(Collectors.toList()));
		}
		
		if (mappingModel.isPresent()) {
			MappingModel _mappingModel = mappingModel.get();
			resource.setTargetPlatformKey(_mappingModel.getTargetPlatform());
			for (MappingRule rule : getFbRule(_mappingModel.getRules())) {
				StereoTypeTarget target = (StereoTypeTarget)rule.getTarget();
				resource.addStereotype(Stereotype.create(target.getName(), convertAttributesToMap(target.getAttributes())));
			}
		}

		return resource;
	}
	private static Map<String,String> convertAttributesToMap(List<Attribute> attributes) {
		Map<String,String> result = attributes.stream().collect(
                Collectors.toMap(Attribute::getName, Attribute::getValue));
		return result;
	}
	private static List<MappingRule> getFbRule(List<MappingRule> rules) {
		return rules.stream().filter(r -> r.getSources().get(0) instanceof FunctionBlockAttributeSource).collect(Collectors.toList());
	}

	private static Operation createOperation(org.eclipse.vorto.core.api.model.functionblock.Operation o,Optional<MappingModel> mappingModel) {
		Operation operation = new Operation();
		operation.setBreakable(o.isBreakable());
		operation.setDescription(o.getDescription());
		operation.setName(o.getName());
		operation.setParams(o.getParams().stream().map(p -> createParam(p,mappingModel)).collect(Collectors.toList()));

		if (o.getReturnType() != null) {
			ReturnType returnType = new ReturnType();
			returnType.setMultiple(o.getReturnType().isMultiplicity());
			if (o.getReturnType() instanceof ReturnPrimitiveType) {
				returnType.setPrimitive(true);
				PrimitiveType pt = ((ReturnPrimitiveType) o.getReturnType()).getReturnType();
				returnType.setType(org.eclipse.vorto.repository.api.content.PrimitiveType.valueOf(pt.name()));
			} else {
				returnType.setPrimitive(false);
				returnType.setType(createModelId(((ReturnObjectType) o.getReturnType()).getReturnType()));
			}
			operation.setResult(returnType);
		}
		
		if (mappingModel.isPresent()) {
			operation.setTargetPlatformKey(mappingModel.get().getTargetPlatform());
			for (MappingRule rule : getOperationRule(operation.getName(),mappingModel.get().getRules())) {
				if (rule.getTarget() instanceof StereoTypeTarget) {
					StereoTypeTarget target = (StereoTypeTarget)rule.getTarget();
					operation.addStereotype(Stereotype.create(target.getName(), convertAttributesToMap(target.getAttributes())));
				} else if (rule.getTarget() instanceof ReferenceTarget) {
					ReferenceTarget target = (ReferenceTarget)rule.getTarget();
					operation.setMappingReference(createModelId(target.getMappingModel()));
				}
			}
		}
		return operation;
	}

	private static Param createParam(org.eclipse.vorto.core.api.model.functionblock.Param p,Optional<MappingModel> mappingModel) {
		Param param = new Param();
		param.setDescription(p.getDescription());
		param.setMultiple(p.isMultiplicity());
		param.setName(p.getName());
		if (p instanceof PrimitiveParam) {
			PrimitiveType pt = ((PrimitiveParam) p).getType();
			param.setType(org.eclipse.vorto.repository.api.content.PrimitiveType.valueOf(pt.name()));

			if (((PrimitiveParam) p).getConstraintRule() != null
					&& ((PrimitiveParam) p).getConstraintRule().getConstraints() != null) {
				List<Constraint> constraints = ((PrimitiveParam) p).getConstraintRule().getConstraints().stream()
						.map(c -> createConstraint(c)).collect(Collectors.toList());
				param.setConstraints(constraints);
			}
		} else {
			param.setType(createModelId(((RefParam) p).getType()));
		}
		
		if (mappingModel.isPresent()) {
			param.setTargetPlatformKey(mappingModel.get().getTargetPlatform());
			for (MappingRule rule : getParamRule(((org.eclipse.vorto.core.api.model.functionblock.Operation)p.eContainer()).getName(),param.getName(),mappingModel.get().getRules())) {
				if (rule.getTarget() instanceof StereoTypeTarget) {
					StereoTypeTarget target = (StereoTypeTarget)rule.getTarget();
					param.addStereotype(Stereotype.create(target.getName(), convertAttributesToMap(target.getAttributes())));
				} else if (rule.getTarget() instanceof ReferenceTarget) {
					ReferenceTarget target = (ReferenceTarget)rule.getTarget();
					param.setMappingReference(createModelId(target.getMappingModel()));
				}
			}
		}

		return param;
	}

	private static ModelProperty createProperty(FunctionblockProperty property,Optional<MappingModel> mappingModel) {
		ModelProperty p = new ModelProperty();
		p.setDescription(property.getDescription());
		p.setName(property.getName());
		p.setType(createModelId(property.getType()));
		
		if (mappingModel.isPresent()) {
			p.setTargetPlatformKey(mappingModel.get().getTargetPlatform());
			for (MappingRule rule : getPropertyRule(p.getName(),mappingModel.get().getRules())) {
				if (rule.getTarget() instanceof StereoTypeTarget) {
					StereoTypeTarget target = (StereoTypeTarget)rule.getTarget();
					p.addStereotype(Stereotype.create(target.getName(), convertAttributesToMap(target.getAttributes())));
				} else if (rule.getTarget() instanceof ReferenceTarget) {
					ReferenceTarget target = (ReferenceTarget)rule.getTarget();
					p.setMappingReference(createModelId(target.getMappingModel()));
				}
			}
		}
		
		return p;
	}

	private static ModelProperty createProperty(Property property,Optional<MappingModel> mappingModel) {
		ModelProperty p = new ModelProperty();
		p.setDescription(property.getDescription());
		p.setMandatory(property.getPresence() != null ? property.getPresence().isMandatory() : true);
		p.setMultiple(property.isMultiplicity());
		p.setName(property.getName());
		if (property.getType() instanceof PrimitivePropertyType) {
			PrimitiveType pt = ((PrimitivePropertyType) property.getType()).getType();
			p.setType(org.eclipse.vorto.repository.api.content.PrimitiveType.valueOf(pt.name()));
		} else {
			p.setType(createModelId(((ObjectPropertyType) property.getType()).getType()));
		}

		if (property.getConstraintRule() != null && property.getConstraintRule().getConstraints() != null) {
			List<Constraint> constraints = property.getConstraintRule().getConstraints().stream()
					.map(c -> createConstraint(c)).collect(Collectors.toList());
			p.setConstraints(constraints);
		}

		if (property.getPropertyAttributes() != null) {
			List<IPropertyAttribute> attributes = property.getPropertyAttributes().stream().map(a -> createAttribute(a))
					.collect(Collectors.toList());
			p.setAttributes(attributes);
		}
		
		if (mappingModel.isPresent()) {
			p.setTargetPlatformKey(mappingModel.get().getTargetPlatform());
			for (MappingRule rule : getPropertyRule(p.getName(),mappingModel.get().getRules())) {
				StereoTypeTarget target = (StereoTypeTarget)rule.getTarget();
				p.addStereotype(Stereotype.create(target.getName(), convertAttributesToMap(target.getAttributes())));
			}
		}
		
		return p;
	}
	
	private static List<MappingRule> getPropertyRule(String propertyName, List<MappingRule> rules) {
		return rules.stream().filter(r -> ( r.getSources().get(0) instanceof FunctionBlockPropertySource || r.getSources().get(0) instanceof InfoModelPropertySource ) && matchesProperty(r.getSources().get(0),propertyName)).collect(Collectors.toList());
	}
	
	private static List<MappingRule> getOperationRule(String operationName, List<MappingRule> rules) {
		return rules.stream().filter(r -> ( r.getSources().get(0) instanceof FunctionBlockPropertySource) && matchesOperation(r.getSources().get(0),operationName)).collect(Collectors.toList());
	}
	
	private static List<MappingRule> getParamRule(String operationName, String paramName, List<MappingRule> rules) {
		return rules.stream().filter(r -> ( r.getSources().get(0) instanceof OperationSource) && matchesOperation(r.getSources().get(0),operationName) && matchesParam(r.getSources().get(0), paramName)).collect(Collectors.toList());
	}

	
	private static boolean matchesProperty(Source source, String propertyName) {
		if (source instanceof ConfigurationSource && ((ConfigurationSource)source).getProperty().getName().equals(propertyName)) {
			return true;
		} else if (source instanceof StatusSource && ((StatusSource)source).getProperty().getName().equals(propertyName)) {
			return true;
		} else if (source instanceof FaultSource && ((FaultSource)source).getProperty().getName().equals(propertyName)) {
			return true;
		} else if (source instanceof InfoModelPropertySource && ((InfoModelPropertySource)source).getProperty().getName().equals(propertyName)) {
			return true;
		}
		return false;
	}
	
	private static boolean matchesOperation(Source source, String operationName) {
		if (source instanceof OperationSource && ((OperationSource)source).getOperation().getName().equals(operationName)) {
			return true;
		} 
		return false;
	}
	
	private static boolean matchesParam(Source source,String paramName) {
		if (source instanceof OperationSource && ((OperationSource)source).getParam() != null && ((OperationSource)source).getParam().getName().equals(paramName)) {
			return true;
		} 
		return false;
	}

	private static IPropertyAttribute createAttribute(PropertyAttribute attribute) {
		if (attribute instanceof BooleanPropertyAttribute) {
			BooleanPropertyAttribute booleanAttribute = (BooleanPropertyAttribute) attribute;
			return new BooleanAttributeProperty(
					BooleanAttributePropertyType.valueOf(booleanAttribute.getType().name()),
					booleanAttribute.isValue());
		} else if (attribute instanceof EnumLiteralPropertyAttribute) {
			EnumLiteralPropertyAttribute enumAttribute = (EnumLiteralPropertyAttribute) attribute;
			return new EnumAttributeProperty(EnumAttributePropertyType.valueOf(enumAttribute.getType().name()),
					createLiteral(enumAttribute.getValue()));
		} else {
			throw new UnsupportedOperationException("Property Attribute cannot be mapped");
		}
	}

	private static Constraint createConstraint(org.eclipse.vorto.core.api.model.datatype.Constraint c) {
		return new Constraint(ConstraintType.valueOf(c.getType().name()), c.getConstraintValues());
	}

	private static ModelEvent createEvent(Event event,Optional<MappingModel> mappingModel) {
		ModelEvent modelEvent = new ModelEvent();
		modelEvent.setName(event.getName());
		modelEvent
				.setProperties(event.getProperties().stream().map(p -> createProperty(p,mappingModel)).collect(Collectors.toList()));
		return modelEvent;
	}

	public static EntityModel createResource(Entity model,Optional<MappingModel> mappingModel) {
		EntityModel resource = new EntityModel(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
				ModelType.Datatype);
		resource.setDescription(model.getDescription());
		resource.setDisplayName(model.getDisplayname());
		resource.setReferences(
				model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));
		resource.setProperties(model.getProperties().stream().map(p -> createProperty(p,mappingModel)).collect(Collectors.toList()));
		
		if (mappingModel.isPresent()) {
			resource.setTargetPlatformKey(mappingModel.get().getTargetPlatform());
			for (MappingRule rule : getEntityRule(mappingModel.get().getRules())) {
				StereoTypeTarget target = (StereoTypeTarget)rule.getTarget();
				resource.addStereotype(Stereotype.create(target.getName(), convertAttributesToMap(target.getAttributes())));
			}
		}
		
		return resource;
	}
	
	private static List<MappingRule> getEntityRule(List<MappingRule> rules) {
		return rules.stream().filter(r -> r.getSources().get(0) instanceof EntitySource).collect(Collectors.toList());
	}

	public static EnumModel createResource(Enum model) {
		EnumModel resource = new EnumModel(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
				ModelType.Datatype);
		resource.setDescription(model.getDescription());
		resource.setDisplayName(model.getDisplayname());
		resource.setReferences(
				model.getReferences().stream().map(reference -> createModelId(reference)).collect(Collectors.toList()));
		resource.setLiterals(model.getEnums().stream().map(p -> createLiteral(p)).collect(Collectors.toList()));
		return resource;
	}

	private static EnumLiteral createLiteral(org.eclipse.vorto.core.api.model.datatype.EnumLiteral literal) {
		return new EnumLiteral(literal.getName(), literal.getDescription());
	}
}
