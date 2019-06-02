/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.model.conversion;

import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.BuilderUtils.EntityBuilder;
import org.eclipse.vorto.core.api.model.BuilderUtils.EnumBuilder;
import org.eclipse.vorto.core.api.model.BuilderUtils.FunctionblockBuilder;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;

/**
 * Converts the Model Content (pojo) to Vorto ecore model
 *
 */
public class ModelContentToEcoreConverter implements IModelConverter<ModelContent, Model> {
  
    public ModelContentToEcoreConverter() {
      DatatypePackageImpl.init();
    }

	@Override
	public Model convert(ModelContent source, Optional<String> platformKey) {
	  AbstractModel model = source.getModels().get(source.getRoot());
	  return convert(model,source);
	}
	
  private Model convert(AbstractModel model, ModelContent context) {
    if (model instanceof EntityModel) {
      return convertEntity((EntityModel)model,context);
    } else if (model instanceof EnumModel) {
      return convertEnum((EnumModel)model);
    } else if (model instanceof FunctionblockModel) {
      return convertFunctionblock((FunctionblockModel)model,context);
    } else {
      return null;
    }
  }

  private Model convertFunctionblock(FunctionblockModel model, ModelContent context) {
    FunctionblockBuilder builder = BuilderUtils.newFunctionblock(new ModelId(ModelType.Functionblock, model.getId().getName(), model.getId().getNamespace(), model.getId().getVersion()));
    builder.withCategory(model.getCategory());
    builder.withDescription(model.getDescription());
    builder.withDisplayName(model.getDisplayName());
    builder.withVortolang(model.getVortolang());
    
    for (ModelProperty property : model.getStatusProperties()) {
      if (property.getType() instanceof PrimitiveType) {
        builder.withStatusProperty(property.getName(), org.eclipse.vorto.core.api.model.datatype.PrimitiveType.valueOf(((PrimitiveType)property.getType()).name()));
      } else if (property.getType() instanceof org.eclipse.vorto.model.ModelId){
        AbstractModel referencedModel = context.getModels().get((org.eclipse.vorto.model.ModelId)property.getType());
        Type convertedReference = (Type)convert(referencedModel,context);
        builder.withStatusProperty(property.getName(), convertedReference);
      }
    }
    
    for (ModelProperty property : model.getConfigurationProperties()) {
      if (property.getType() instanceof PrimitiveType) {
        builder.withConfiguration(property.getName(), org.eclipse.vorto.core.api.model.datatype.PrimitiveType.valueOf(((PrimitiveType)property.getType()).name()));
      } else if (property.getType() instanceof org.eclipse.vorto.model.ModelId){
        AbstractModel referencedModel = context.getModels().get((org.eclipse.vorto.model.ModelId)property.getType());
        Type convertedReference = (Type)convert(referencedModel,context);
        builder.withConfiguration(property.getName(), convertedReference);
      }
    }

    return builder.build();
  }

  private Model convertEnum(EnumModel model) {
    EnumBuilder builder = BuilderUtils.newEnum(new ModelId(ModelType.Datatype, model.getId().getName(), model.getId().getNamespace(), model.getId().getVersion()));
    builder.withCategory(model.getCategory());
    builder.withDescription(model.getDescription());
    builder.withDisplayName(model.getDisplayName());
    builder.withVortolang(model.getVortolang());
    
    for (org.eclipse.vorto.model.EnumLiteral literal : model.getLiterals()) {
      builder.withLiteral(literal.getName(),literal.getDescription());
    }
    
    return builder.build();
  }

  private Model convertEntity(EntityModel entity, ModelContent context) {
    EntityBuilder builder =  BuilderUtils.newEntity(new ModelId(ModelType.Datatype, entity.getId().getName(), entity.getId().getNamespace(), entity.getId().getVersion()));
    builder.withCategory(entity.getCategory());
    builder.withDescription(entity.getDescription());
    builder.withDisplayName(entity.getDisplayName());
    builder.withVortolang(entity.getVortolang());
    builder.withReferences(entity.getReferences().stream().map(r -> new ModelId(ModelType.Datatype, r.getName(), r.getNamespace(), r.getVersion())).collect(Collectors.toList()));
    for (ModelProperty property : entity.getProperties()) {
      if (property.getType() instanceof PrimitiveType) {
        builder.withProperty(property.getName(), org.eclipse.vorto.core.api.model.datatype.PrimitiveType.valueOf(((PrimitiveType)property.getType()).name()));
      } else if (property.getType() instanceof org.eclipse.vorto.model.ModelId){
        AbstractModel referencedModel = context.getModels().get((org.eclipse.vorto.model.ModelId)property.getType());
        Type convertedReference = (Type)convert(referencedModel,context);
        builder.withProperty(property.getName(), convertedReference);
      }
    }
    return builder.build();
        
  }

}
