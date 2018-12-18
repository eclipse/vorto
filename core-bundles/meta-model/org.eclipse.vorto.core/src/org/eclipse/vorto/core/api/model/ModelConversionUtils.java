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
package org.eclipse.vorto.core.api.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelFactory;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.api.model.model.ModelReference;

public class ModelConversionUtils {

  public static FunctionblockModel convertToFlatHierarchy(FunctionblockModel fbm) {
    FunctionBlock fb = fbm.getFunctionblock();

    // Consolidate all properties
    List<Property> properties = getFlatProperties(fbm);

    // remove super type reference
    if (fbm.getSuperType() != null) {
      removeSuperTypeModelReference(fbm);
    }
    properties.stream().filter(p -> p.getType() instanceof ObjectPropertyType)
        .forEach(p -> createReference(fbm, (ObjectPropertyType) p.getType()));

    Status status = FunctionblockFactory.eINSTANCE.createStatus();
    status.getProperties().addAll(properties.stream().filter(p -> p.eContainer() instanceof Status)
        .collect(Collectors.toList()));

    fb.setStatus(status);

    Configuration configuration = FunctionblockFactory.eINSTANCE.createConfiguration();
    configuration.getProperties().addAll(properties.stream()
        .filter(p -> p.eContainer() instanceof Configuration).collect(Collectors.toList()));
    fb.setConfiguration(configuration);


    // Consolidate all operations
    List<Operation> operations = getFlatOperations(fbm);
    fb.getOperations().clear();
    fb.getOperations().addAll(operations);


    return fbm;
  }

  private static void removeSuperTypeModelReference(FunctionblockModel fbm) {
    Iterator<ModelReference> iter = fbm.getReferences().iterator();
    while (iter.hasNext()) {
      ModelReference reference = iter.next();
      ModelReference superTypeReference =
          ModelIdFactory.newInstance(fbm.getSuperType()).asModelReference();
      if (EcoreUtil.equals(superTypeReference, reference)) {
        iter.remove();
      }
    }

  }

  private static void createReference(FunctionblockModel fbm, ObjectPropertyType type) {
    ModelReference reference = ModelFactory.eINSTANCE.createModelReference();
    reference.setImportedNamespace(type.getType().getNamespace() + "." + type.getType().getName());
    reference.setVersion(type.getType().getVersion());
    fbm.getReferences().add(reference);
  }

  private static List<Operation> getFlatOperations(FunctionblockModel fbm) {
    List<Operation> operations = new ArrayList<Operation>();
    TreeIterator<EObject> iter = fbm.eAllContents();
    while (iter.hasNext()) {
      EObject obj = iter.next();
      if (obj instanceof Operation) {
        operations.add((Operation) obj);
      }
    }
    if (fbm.getSuperType() != null) {
      operations.addAll(getFlatOperations(fbm.getSuperType()));
    }
    return operations;
  }

  public static InformationModel convertToFlatHierarchy(InformationModel infomodel) {
    for (FunctionblockProperty fbProperty : infomodel.getProperties()) {
      FunctionblockModel fbm = fbProperty.getType();
      fbProperty.setType(convertToFlatHierarchy(fbm));

      // merge any extended properties from information model to the FB properties
      if (fbProperty.getExtendedFunctionBlock() != null
          && fbProperty.getExtendedFunctionBlock().getStatus() != null) {
        for (Property extendedProperty : fbProperty.getExtendedFunctionBlock().getStatus()
            .getProperties()) {
          Optional<Property> baseProperty = fbm.getFunctionblock().getStatus().getProperties()
              .stream().filter(p -> p.getName().equals(extendedProperty.getName())).findFirst();
          if (baseProperty.isPresent()) {
            baseProperty.get().setConstraintRule(extendedProperty.getConstraintRule());
          }
        }
      }
    }

    return infomodel;
  }

  private static List<Property> getFlatProperties(FunctionblockModel fbm) {
    List<Property> properties = new ArrayList<Property>();
    TreeIterator<EObject> iter = fbm.eAllContents();
    while (iter.hasNext()) {
      EObject obj = iter.next();
      if (obj instanceof Property) {
        Property property = (Property) obj;
        properties.add(property);

        if (property.getType() instanceof ObjectPropertyType) {
          ObjectPropertyType objectType = (ObjectPropertyType) property.getType();
          if (objectType.getType() instanceof Entity) { // only flatten entities
            Entity entity = (Entity) ((ObjectPropertyType) property.getType()).getType();
            List<Property> entityProperties = getFlatProperties(entity);
            entity.getProperties().addAll(entityProperties);
            if (entity.getSuperType() != null) {
              removeSuperTypeModelReference(entity);
            }
            entity.getProperties().stream().filter(p -> p.getType() instanceof ObjectPropertyType)
                .forEach(p -> createReference(entity, (ObjectPropertyType) p.getType()));

          }
        }
      }
    }
    if (fbm.getSuperType() != null) {
      properties.addAll(getFlatProperties(fbm.getSuperType()));
    }
    return properties;
  }

  private static void removeSuperTypeModelReference(Entity entity) {
    Iterator<ModelReference> iter = entity.getReferences().iterator();
    while (iter.hasNext()) {
      ModelReference reference = iter.next();
      ModelReference superTypeReference =
          ModelIdFactory.newInstance(entity.getSuperType()).asModelReference();
      if (EcoreUtil.equals(superTypeReference, reference)) {
        iter.remove();
      }
    }

  }

  private static void createReference(Entity entity, ObjectPropertyType type) {
    ModelReference reference = ModelFactory.eINSTANCE.createModelReference();
    reference.setImportedNamespace(type.getType().getNamespace() + "." + type.getType().getName());
    reference.setVersion(type.getType().getVersion());
    entity.getReferences().add(reference);
  }

  private static List<Property> getFlatProperties(Entity entity) {
    List<Property> properties = new ArrayList<Property>();
    TreeIterator<EObject> iter = entity.eAllContents();
    while (iter.hasNext()) {
      EObject obj = iter.next();
      if (obj instanceof Property) {
        Property property = (Property) obj;
        properties.add(property);
      }
    }
    if (entity.getSuperType() != null) {
      properties.addAll(getFlatProperties(entity.getSuperType()));
    }
    return properties;
  }
}
