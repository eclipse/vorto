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
package org.eclipse.vorto.core.api.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.internal.xtend.type.baseimpl.types.PropertyTypeImpl;
import org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory;
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.PropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.PropertyType;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.functionblock.impl.ConfigurationImpl;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelFactory;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import com.ibm.icu.text.DateFormat.BooleanAttribute;
import com.ibm.icu.util.LocaleData.MeasurementSystem;

public class ModelConversionUtils {

  public static Model convertToFlatHierarchy(Model model) {
    if (model instanceof FunctionblockModel
        && ((FunctionblockModel) model).getSuperType() != null) {
      return convertToFlatHierarchy((FunctionblockModel) model);
    } else {
      return model;
    }
  }

  public static FunctionblockModel convertToFlatHierarchy(FunctionblockModel fbm) {
    FunctionBlock fb = fbm.getFunctionblock();

    // Consolidate all properties
    List<Property> configproperties = getFlatConfigProperties(fbm);
    List<Property> statusProperties = getFlatStatusProperties(fbm);
    List<Property> allProperties = new ArrayList();

    allProperties.addAll(configproperties);
    allProperties.addAll(statusProperties);



    // remove super type reference
    if (fbm.getSuperType() != null) {
      removeSuperTypeModelReference(fbm);
    }
    allProperties.stream().filter(p -> p.getType() instanceof ObjectPropertyType)
        .forEach(p -> createReference(fbm, (ObjectPropertyType) p.getType()));

    // set status properties
    Status status = FunctionblockFactory.eINSTANCE.createStatus();
    status.getProperties().addAll(statusProperties);
    fb.setStatus(status);

    // set configuration properties
    Configuration configuration = FunctionblockFactory.eINSTANCE.createConfiguration();
    configuration.getProperties().addAll(configproperties);
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


  private static List<Property> getFlatConfigProperties(FunctionblockModel fbm) {
    EList<Property> properties = new BasicEList<Property>();
    TreeIterator<EObject> iter = fbm.eAllContents();
    while (iter.hasNext()) {
      EObject obj = iter.next();
      if (obj instanceof Property) {
        Property property = (Property) obj;
        if (property.eContainer() instanceof Configuration) {
          properties.add(copyProperty(property));

          if (property.getType() instanceof ObjectPropertyType) {
            ObjectPropertyType objectType = (ObjectPropertyType) property.getType();
            if (objectType.getType() instanceof Entity) { // only flatten entities
              Entity entity = (Entity) ((ObjectPropertyType) property.getType()).getType();
              EList<Property> entityProperties = getFlatProperties(entity);
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
    }
    if (fbm.getSuperType() != null) {
      properties.addAll(getFlatConfigProperties(fbm.getSuperType()));
    }
    return properties;
  }

  private static List<Property> getFlatStatusProperties(FunctionblockModel fbm) {
    EList<Property> properties = new BasicEList<Property>();
    TreeIterator<EObject> iter = fbm.eAllContents();
    while (iter.hasNext()) {
      EObject obj = iter.next();
      if (obj instanceof Property) {
        Property property = (Property) obj;
        if (property.eContainer() instanceof Status) {
          properties.add(copyProperty(property));

          if (property.getType() instanceof ObjectPropertyType) {
            ObjectPropertyType objectType = (ObjectPropertyType) property.getType();
            if (objectType.getType() instanceof Entity) { // only flatten entities
              Entity entity = (Entity) ((ObjectPropertyType) property.getType()).getType();
              EList<Property> entityProperties = getFlatProperties(entity);
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
    }
    if (fbm.getSuperType() != null) {
      properties.addAll(getFlatStatusProperties(fbm.getSuperType()));
    }

    return properties;
  }

  private static Property copyProperty(Property property) {
    Property newProperty = DatatypeFactory.eINSTANCE.createProperty();
    newProperty.setName(property.getName());
    newProperty.setPresence(property.getPresence());
    newProperty.setDescription(property.getDescription());

    if (property.getType() instanceof PrimitivePropertyType) {
      PrimitivePropertyType newPrimitiveType =
          DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
      PrimitivePropertyType oldPrimitiveType = (PrimitivePropertyType) property.getType();
      newPrimitiveType.setType(oldPrimitiveType.getType());
      newProperty.setType(newPrimitiveType);
    } else if (property.getType() instanceof ObjectPropertyType) {
      ObjectPropertyType newObjectType = DatatypeFactory.eINSTANCE.createObjectPropertyType();
      ObjectPropertyType oldObjectType = (ObjectPropertyType) property.getType();
      newObjectType.setType(oldObjectType.getType());
      newProperty.setType(newObjectType);
    }

    EList<org.eclipse.vorto.core.api.model.datatype.PropertyAttribute> oldPropertyAtributeList =
        property.getPropertyAttributes();

    EList<PropertyAttribute> newPropertyAtributeList = new BasicEList<PropertyAttribute>();

    for (org.eclipse.vorto.core.api.model.datatype.PropertyAttribute oldProp : oldPropertyAtributeList) {
      if (oldProp instanceof BooleanPropertyAttribute) {
        BooleanPropertyAttribute newPropertyAtribute =
            DatatypeFactory.eINSTANCE.createBooleanPropertyAttribute();
        newPropertyAtribute.setType(((BooleanPropertyAttribute) oldProp).getType());
        newPropertyAtribute.setValue((((BooleanPropertyAttribute) oldProp).isValue()));
        newPropertyAtributeList.add(newPropertyAtribute);
      } else {
        EnumLiteralPropertyAttribute newPropertyAtribute =
            DatatypeFactory.eINSTANCE.createEnumLiteralPropertyAttribute();
        newPropertyAtribute.setType(((EnumLiteralPropertyAttribute) oldProp).getType());
        newPropertyAtribute.setValue((((EnumLiteralPropertyAttribute) oldProp).getValue()));
        newPropertyAtributeList.add(newPropertyAtribute);

      }
    }
    newProperty.getPropertyAttributes().addAll(newPropertyAtributeList);

    return newProperty;
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

  private static EList<Property> getFlatProperties(Entity entity) {
    EList<Property> properties = new BasicEList<Property>();
    TreeIterator<EObject> iter = entity.eAllContents();
    while (iter.hasNext()) {
      EObject obj = iter.next();
      if (obj instanceof Property) {
        Property property = (Property) obj;
        properties.add(copyProperty(property));
      }
    }
    if (entity.getSuperType() != null) {
      properties.addAll(getFlatProperties(entity.getSuperType()));
    }
    return properties;
  }

}
