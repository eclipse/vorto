/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.model.conversion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.BuilderUtils.EntityBuilder;
import org.eclipse.vorto.core.api.model.BuilderUtils.EnumBuilder;
import org.eclipse.vorto.core.api.model.BuilderUtils.EventBuilder;
import org.eclipse.vorto.core.api.model.BuilderUtils.FunctionblockBuilder;
import org.eclipse.vorto.core.api.model.BuilderUtils.InformationModelBuilder;
import org.eclipse.vorto.core.api.model.BuilderUtils.ModelBuilder;
import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType;
import org.eclipse.vorto.core.api.model.datatype.Presence;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl;
import org.eclipse.vorto.core.api.model.functionblock.DictonaryParam;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.Param;
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnDictonaryType;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType;
import org.eclipse.vorto.core.api.model.functionblock.ReturnType;
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl;
import org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl;
import org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.Constraint;
import org.eclipse.vorto.model.DictionaryType;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelEvent;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.Operation;
import org.eclipse.vorto.model.PrimitiveType;

/**
 * Converts the Model Content (pojo) to Vorto ecore model
 *
 */
public class ModelContentToEcoreConverter implements IModelConverter<ModelContent, Model> {

  static {
    DatatypePackageImpl.init();
    FunctionblockPackageImpl.init();
    InformationModelPackageImpl.init();
    MappingPackageImpl.init();
  }
  
  @Override
  public Model convert(ModelContent source, Optional<String> platformKey) {
    IModel model = source.getModels().get(source.getRoot());
    return convert(model, source);
  }

  private Model convert(IModel model, ModelContent context) {
    if (model instanceof EntityModel) {
      return convertEntity((EntityModel) model, context);
    } else if (model instanceof EnumModel) {
      return convertEnum((EnumModel) model);
    } else if (model instanceof FunctionblockModel) {
      return convertFunctionblock((FunctionblockModel) model, context);
    } else if (model instanceof Infomodel) {
      return convertInformationModel((Infomodel) model, context);
    } else {
      return null;
    }
  }

  private Model convertInformationModel(Infomodel model, ModelContent context) {
    InformationModelBuilder builder = BuilderUtils.newInformationModel(new ModelId(ModelType.InformationModel, model.getId().getName(),
        model.getId().getNamespace(), model.getId().getVersion()));
    
    builder.withCategory(model.getCategory());
    builder.withDescription(model.getDescription());
    builder.withDisplayName(model.getDisplayName());
    builder.withVortolang(model.getVortolang());
    
    for (ModelProperty property : model.getFunctionblocks()) {
      FunctionblockModel fbModel = (FunctionblockModel)context.getModels().get((org.eclipse.vorto.model.ModelId)property.getType());
      org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel convertedFb = (org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel)convertFunctionblock(fbModel,context);
      builder.withReference(ModelIdFactory.newInstance(convertedFb));
      builder.withFunctionBlock(convertedFb, property.getName(),property.getDescription(), property.isMandatory());
    }
    
    return builder.build();
  }
  
  private Property createProperty(ModelProperty sourceProperty, ModelBuilder<?> builder, ModelContent context) {
    Property property = null;
    if (sourceProperty.getType() instanceof PrimitiveType) {
      property = BuilderUtils.createProperty(sourceProperty.getName(), org.eclipse.vorto.core.api.model.datatype.PrimitiveType
          .valueOf(((PrimitiveType) sourceProperty.getType()).name()));
    } else if (sourceProperty.getType() instanceof org.eclipse.vorto.model.ModelId) {      
      IModel referencedModel =
          context.getModels().get((org.eclipse.vorto.model.ModelId) sourceProperty.getType());
      
      Type convertedReference = (Type)convert(referencedModel, context);
      builder.withReference(ModelIdFactory.newInstance(convertedReference));
      property = BuilderUtils.createProperty(sourceProperty.getName(), convertedReference);
    } else if (sourceProperty.getType() instanceof DictionaryType) {
      //FIXME
    } else {
      throw new UnsupportedOperationException("type must be either primitive, object or dictionary");
    }
    
    property.setDescription(sourceProperty.getDescription());
    Presence presence = org.eclipse.vorto.core.api.model.datatype.DatatypeFactory.eINSTANCE.createPresence();
    presence.setMandatory(sourceProperty.isMandatory());
    property.setPresence(presence);
    
    org.eclipse.vorto.core.api.model.datatype.ConstraintRule rule = org.eclipse.vorto.core.api.model.datatype.DatatypeFactory.eINSTANCE.createConstraintRule();
    for (Constraint constraint : sourceProperty.getConstraints()) {
      org.eclipse.vorto.core.api.model.datatype.Constraint eConstraint = org.eclipse.vorto.core.api.model.datatype.DatatypeFactory.eINSTANCE.createConstraint();
      eConstraint.setConstraintValues(constraint.getValue());
      eConstraint.setType(ConstraintIntervalType.valueOf(constraint.getType().name()));
      rule.getConstraints().add(eConstraint);
    }
    property.setConstraintRule(rule);
    
    return property;
  }

  private Model convertFunctionblock(FunctionblockModel model, ModelContent context) {
    FunctionblockBuilder builder =
        BuilderUtils.newFunctionblock(new ModelId(ModelType.Functionblock, model.getId().getName(),
            model.getId().getNamespace(), model.getId().getVersion()));
    builder.withCategory(model.getCategory());
    builder.withDescription(model.getDescription());
    builder.withDisplayName(model.getDisplayName());
    builder.withVortolang(model.getVortolang());

    for (ModelProperty sourceProperty : model.getStatusProperties()) {
      Property property = createProperty(sourceProperty, builder, context);
      builder.withStatusProperty(property);
    }

    for (ModelProperty sourceProperty : model.getConfigurationProperties()) {
      Property property = createProperty(sourceProperty, builder, context);
      builder.withConfiguration(property);
    }

    for (Operation operation : model.getOperations()) {
      builder.withOperation(operation.getName(),
          createReturnTypeOrNull(operation.getResult(), builder, context),operation.getDescription(),
          operation.isBreakable(), createParams(operation.getParams(), context));
    }
    
    for (ModelEvent event : model.getEvents()) {
     EventBuilder eventBuilder = BuilderUtils.newEvent(event.getName());
      
     for (ModelProperty sourceProperty : event.getProperties()) {
       Property property = createProperty(sourceProperty, builder, context);
       eventBuilder.withProperty(property);
     }
     
     builder.withEvent(eventBuilder.build());
    }

    return builder.build();
  }
  

  private Param[] createParams(List<org.eclipse.vorto.model.Param> params, ModelContent context) {
    List<Param> ecoreParams = new ArrayList<>();
    for (org.eclipse.vorto.model.Param param : params) {
      if (param.getType() instanceof PrimitiveType) {
        PrimitiveParam type = FunctionblockFactory.eINSTANCE.createPrimitiveParam();
        type.setMultiplicity(param.isMultiple());
        
        type.setType((org.eclipse.vorto.core.api.model.datatype.PrimitiveType
            .valueOf(((PrimitiveType) param.getType()).name())));
        ecoreParams.add(type);
      } else if (param.getType() instanceof org.eclipse.vorto.model.ModelId) {
        RefParam type  = FunctionblockFactory.eINSTANCE.createRefParam();
        type.setMultiplicity(param.isMultiple());
        type.setType((Type) convert(
            context.getModels().get((org.eclipse.vorto.model.ModelId) param.getType()), context));
        ecoreParams.add(type);
      } else if (param.getType() instanceof DictionaryType) {
        DictonaryParam type = FunctionblockFactory.eINSTANCE.createDictonaryParam();
        type.setMultiplicity(param.isMultiple());
        ecoreParams.add(type);
      } else {
        return null;
      }
    }
    
    return ecoreParams.toArray(new Param[ecoreParams.size()]);
  }

  private ReturnType createReturnTypeOrNull(org.eclipse.vorto.model.ReturnType result, FunctionblockBuilder builder, 
      ModelContent context) {
    if (result == null) {
      return null;
    } else {
      if (result.getType() instanceof PrimitiveType) {
        ReturnPrimitiveType type = FunctionblockFactory.eINSTANCE.createReturnPrimitiveType();
        type.setMultiplicity(result.isMultiple());
        type.setReturnType(org.eclipse.vorto.core.api.model.datatype.PrimitiveType
            .valueOf(((PrimitiveType) result.getType()).name()));
        return type;
      } else if (result.getType() instanceof org.eclipse.vorto.model.ModelId) {
        ReturnObjectType type = FunctionblockFactory.eINSTANCE.createReturnObjectType();
        type.setMultiplicity(result.isMultiple());
        Type convertedType = ((Type) convert(
            context.getModels().get((org.eclipse.vorto.model.ModelId) result.getType()), context));
        builder.withReference(ModelIdFactory.newInstance(convertedType));
        type.setReturnType(convertedType);
        return type;
      } else if (result.getType() instanceof DictionaryType) {
        ReturnDictonaryType type = FunctionblockFactory.eINSTANCE.createReturnDictonaryType();
        type.setMultiplicity(result.isMultiple());
        return type;
      } else {
        return null;
      }
    }

  }

  private Model convertEnum(EnumModel model) {
    EnumBuilder builder = BuilderUtils.newEnum(new ModelId(ModelType.Datatype,
        model.getId().getName(), model.getId().getNamespace(), model.getId().getVersion()));
    builder.withCategory(model.getCategory());
    builder.withDescription(model.getDescription());
    builder.withDisplayName(model.getDisplayName());
    builder.withVortolang(model.getVortolang());

    for (org.eclipse.vorto.model.EnumLiteral literal : model.getLiterals()) {
      builder.withLiteral(literal.getName(), literal.getDescription());
    }

    return builder.build();
  }

  private Model convertEntity(EntityModel entity, ModelContent context) {
    EntityBuilder builder = BuilderUtils.newEntity(new ModelId(ModelType.Datatype,
        entity.getId().getName(), entity.getId().getNamespace(), entity.getId().getVersion()));
    builder.withCategory(entity.getCategory());
    builder.withDescription(entity.getDescription());
    builder.withDisplayName(entity.getDisplayName());
    builder.withVortolang(entity.getVortolang());
    builder.withReferences(entity.getReferences().stream()
        .map(r -> new ModelId(ModelType.Datatype, r.getName(), r.getNamespace(), r.getVersion()))
        .collect(Collectors.toList()));
    
    for (ModelProperty sourceProperty : entity.getProperties()) {
      Property property = createProperty(sourceProperty, builder, context);
      builder.withProperty(property);
    }
    return builder.build();

  }

}
