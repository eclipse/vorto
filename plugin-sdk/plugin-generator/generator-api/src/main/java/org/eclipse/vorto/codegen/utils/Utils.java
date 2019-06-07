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
package org.eclipse.vorto.codegen.utils;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Param;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelFactory;
import org.eclipse.vorto.core.api.model.model.ModelReference;

public class Utils {

  public static InformationModel toInformationModel(Model model) {
    if (model instanceof InformationModel) {
      return (InformationModel) model;
    } else if (model instanceof FunctionblockModel) {
      return Utils.wrapFunctionBlock((FunctionblockModel) model);
    } else {
      throw new UnsupportedOperationException("model must either be a information- or function block model");
    }
  }

  public static InformationModel wrapFunctionBlock(FunctionblockModel fbModel) {
    InformationModel infomodel = InformationModelFactory.eINSTANCE.createInformationModel();
    infomodel.setCategory(fbModel.getCategory());
    infomodel.setDescription(fbModel.getDescription());
    infomodel.setDisplayname(fbModel.getDisplayname());
    infomodel.setName(fbModel.getName() + "IM");
    infomodel.setNamespace(fbModel.getNamespace());
    infomodel.setVersion(fbModel.getVersion());

    FunctionblockProperty property =
        InformationModelFactory.eINSTANCE.createFunctionblockProperty();
    property.setType(fbModel);
    property.setName(fbModel.getName().toLowerCase());
    infomodel.getProperties().add(property);

    ModelReference reference = ModelFactory.eINSTANCE.createModelReference();
    reference.setImportedNamespace(fbModel.getNamespace() + "." + fbModel.getName());
    reference.setVersion(fbModel.getVersion());
    infomodel.getReferences().add(reference);
    return infomodel;
  }

  public static EList<Entity> getReferencedEntities(FunctionBlock fb) {
    BasicEList<Entity> entities = new BasicEList<Entity>();
    for (Type type : getReferencedTypes(fb)) {
      if ((type instanceof Entity) && (!entities.contains((Entity) type))) {
        entities.add((Entity) type);
      }
    }
    return entities;
  }

  public static EList<Enum> getReferencedEnums(FunctionBlock fb) {
    BasicEList<Enum> enums = new BasicEList<Enum>();
    for (Type type : getReferencedTypes(fb)) {
      if ((type instanceof Enum) && (!enums.contains((Enum) type))) {
        enums.add((Enum) type);
      }
    }
    return enums;
  }

  public static EList<Type> getReferencedTypes(Type type) {
    BasicEList<Type> types = new BasicEList<Type>();
    types.add(type);

    if (type instanceof Entity) {
      Entity entityType = (Entity) type;
      for (Property property : entityType.getProperties()) {
        types.addAll(getReferencedTypes(property));
      }
      types.add(entityType.getSuperType());
    }
    return types;
  }

  public static EList<Type> getReferencedTypes(Property property) {
    BasicEList<Type> types = new BasicEList<Type>();
    if (property.getType() instanceof ObjectPropertyType) {
      ObjectPropertyType objectType = (ObjectPropertyType) property.getType();
      types.add(objectType.getType());
      if (objectType.getType() instanceof Entity) {
        types.addAll(getReferencedTypes((Entity) objectType.getType()));
      }
    }
    return types;
  }

  public static EList<Type> getReferencedTypes(FunctionBlock fb) {
    BasicEList<Type> types = new BasicEList<Type>();
    if (fb != null) {
      // Analyze the status properties...
      if (fb.getStatus() != null) {
        for (Property property : fb.getStatus().getProperties()) {
          types.addAll(getReferencedTypes(property));
        }
      }
      // Analyze the configuration properties...
      if (fb.getConfiguration() != null) {
        for (Property property : fb.getConfiguration().getProperties()) {
          types.addAll(getReferencedTypes(property));
        }
      }
      // Analyze the fault properties...
      if (fb.getFault() != null) {
        for (Property property : fb.getFault().getProperties()) {
          types.addAll(getReferencedTypes(property));
        }
      }

      // Analyze the operation types
      for (Operation op : fb.getOperations()) {
        if (op.getReturnType() instanceof ReturnObjectType) {
          types.addAll(getReferencedTypes(((ReturnObjectType) op.getReturnType()).getReturnType()));
        }
        for (Param param : op.getParams()) {
          if (param instanceof RefParam) {
            types.addAll(getReferencedTypes(((RefParam) param).getType()));
          }
        }
      }
    }
    return types;
  }
}
