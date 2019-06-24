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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Param;
import org.eclipse.vorto.core.api.model.functionblock.ReturnType;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.api.model.model.VortoLangVersion;

public abstract class BuilderUtils {

  public static Property createProperty(String name, Type type) {
    Property prop = DatatypeFactory.eINSTANCE.createProperty();
    prop.setName(name);
    prop.setType(createObjectType(type));
    return prop;
  }

  public static ObjectPropertyType createObjectType(Type type) {
    ObjectPropertyType typeObj = DatatypeFactory.eINSTANCE.createObjectPropertyType();
    typeObj.setType(type);
    return typeObj;
  }

  public static Property createProperty(String name, PrimitiveType type) {
    Property prop = DatatypeFactory.eINSTANCE.createProperty();
    prop.setName(name);
    prop.setType(createPrimitiveType(type));
    return prop;
  }
  
  private static PrimitivePropertyType createPrimitiveType(PrimitiveType type) {
    PrimitivePropertyType typeObj = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
    typeObj.setType(type);
    return typeObj;
  }
  
  public static FunctionblockBuilder newFunctionblock(ModelId modelId) {
    return (FunctionblockBuilder)new FunctionblockBuilder().withId(modelId);
  }

  public static InformationModelBuilder newInformationModel(ModelId modelId) {
    return (InformationModelBuilder)new InformationModelBuilder().withId(modelId);
  }

  public static EntityBuilder newEntity(ModelId modelId) {
    return (EntityBuilder)new EntityBuilder().withId(modelId);
  }
  
  public static MappingBuilder newMapping(ModelId modelId, String targetPlatformKey) {
    return (MappingBuilder)new MappingBuilder().withTargetPlatformKey(targetPlatformKey).withId(modelId);
  }
    
  public static EventBuilder newEvent(String name) {
    return new EventBuilder(name);
  }

  public abstract static class ModelBuilder<T extends Model> {
    protected T model;

    public ModelBuilder(T model) {
      this.model = model;
    }

    public ModelBuilder<T> withId(ModelId id) {
      model.setName(id.getName());
      model.setNamespace(id.getNamespace());
      model.setVersion(id.getVersion());
      model.setDisplayname(id.getName());
      return this;
    }

    public ModelBuilder<T> withDescription(String description) {
      model.setDescription(description);
      return this;
    }

    public ModelBuilder<T> withDisplayName(String displayName) {
      model.setDisplayname(displayName);
      return this;
    }

    public ModelBuilder<T> withCategory(String category) {
      model.setCategory(category);
      return this;
    }

    public ModelBuilder<T> withVortolang(String lang) {
      if ("1.0".equals(lang)) {
        model.setLang(VortoLangVersion.VERSION1);
      }
      return this;
    }
    
    public ModelBuilder<T> withReferences(List<ModelId> references) {
      for (ModelId id : references) {
        model.getReferences().add(id.asModelReference());
      }
      return this;
    }
    
    public ModelBuilder<T> withReference(ModelId references) {
      ModelReference referenceToBeAdded = references.asModelReference();
      if (!model.getReferences().stream().filter(ref -> (ref.getImportedNamespace().equals(referenceToBeAdded.getImportedNamespace()) && ref.getVersion().equals(referenceToBeAdded.getVersion()))).findAny().isPresent()) {
        model.getReferences().add(referenceToBeAdded);
      }     
      return this;
    }

    public T build() {
      return (T)model;
    }
  }
  
  public static class MappingBuilder extends ModelBuilder<MappingModel> {

    public MappingBuilder() {
      super(MappingFactory.eINSTANCE.createMappingModel());
    }
    
    public MappingBuilder withTargetPlatformKey(String targetPlatformKey) {
      this.model.setTargetPlatform(targetPlatformKey);
      return this;
    }
    
    public MappingBuilder addRule(MappingRule rule) {
      this.model.getRules().add(rule);
      return this;
    }
  
  }
      
  public static class MappingRuleBuilder {
    
    private MappingRule rule;
    
    public MappingRuleBuilder() {
      this.rule = MappingFactory.eINSTANCE.createMappingRule();
    }
    
    public MappingRuleBuilder withSource(Source source) {
      this.rule.getSources().add(source);
      return this;
    }
    
    public MappingRuleBuilder withStereotypeTarget(String name, Map<String,String> attributes) {
      StereoTypeTarget target = MappingFactory.eINSTANCE.createStereoTypeTarget();
      target.setName(name);
      for (String key : attributes.keySet()) {
        String value = attributes.get(key);
        Attribute attribute = MappingFactory.eINSTANCE.createAttribute();
        attribute.setName(key);
        attribute.setValue(value);
        target.getAttributes().add(attribute);
      }
      this.rule.setTarget(target);
      return this;
    }
    
    public MappingRule build() {
      return rule;
    }
  }
  
  public static class EventBuilder {
    private Event event;
    public EventBuilder(String name) {
      this.event = FunctionblockFactory.eINSTANCE.createEvent();
      this.event.setName(name);
    }
    
    public EventBuilder withProperty(Property property) {
      this.event.getProperties().add(property);
      return this;
    }
    
    public EventBuilder withProperty(String name, PrimitiveType type) {
      this.event.getProperties().add(BuilderUtils.createProperty(name, type));
      return this;
    }

    public EventBuilder withProperty(String name, Type objectType) {
      this.event.getProperties().add(BuilderUtils.createProperty(name, objectType));
      return this;
    }
    
    public Event build() {
      return event;
    }
  }

  public static class EntityBuilder extends ModelBuilder<Entity>{

    public EntityBuilder() {
      super(DatatypeFactory.eINSTANCE.createEntity());
    }
    
    public EntityBuilder withProperty(Property property) {
      this.model.getProperties().add(property);
      return this;
    }

    public EntityBuilder withProperty(String name, PrimitiveType type) {
      this.model.getProperties().add(BuilderUtils.createProperty(name, type));
      return this;
    }

    public EntityBuilder withProperty(String name, Type objectType) {
      this.model.getProperties().add(BuilderUtils.createProperty(name, objectType));
      return this;
    }

  }

  public static EnumBuilder newEnum(ModelId modelId) {
    return (EnumBuilder)new EnumBuilder().withId(modelId);
  }

  public static class EnumBuilder extends ModelBuilder<Enum> {
  
    public EnumBuilder() {
      super(DatatypeFactory.eINSTANCE.createEnum());
    }

    public EnumBuilder withLiterals(String... values) {
      for (String value : values) {
        EnumLiteral literal = DatatypeFactory.eINSTANCE.createEnumLiteral();
        literal.setName(value);
        this.model.getEnums().add(literal);
      }
      return this;
    }
    
    public EnumBuilder withLiteral(String value, String description) {
      EnumLiteral literal = DatatypeFactory.eINSTANCE.createEnumLiteral();
      literal.setName(value);
      literal.setDescription(description);
      this.model.getEnums().add(literal);
      return this;
    }
  }

  public static class InformationModelBuilder extends ModelBuilder<InformationModel>{


    public InformationModelBuilder() {
      super(InformationModelFactory.eINSTANCE.createInformationModel());
    }

    public InformationModelBuilder withFunctionBlock(FunctionblockModel fbm, String propertyName, String description, boolean isMultiple) {
      FunctionblockProperty property =
          InformationModelFactory.eINSTANCE.createFunctionblockProperty();
      property.setName(propertyName);
      property.setDescription(description);
      property.setMultiplicity(isMultiple);
      property.setType(fbm);
      this.model.getProperties().add(property);
      return this;
    }
  }

  public static class FunctionblockBuilder extends ModelBuilder<FunctionblockModel> {

    private FunctionBlock fb;

    public FunctionblockBuilder() {
      super(FunctionblockFactory.eINSTANCE.createFunctionblockModel());
      this.fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
      this.model.setFunctionblock(fb);
    }

    public FunctionblockBuilder withStatusProperty(String name, PrimitiveType type) {
      if (this.fb.getStatus() == null) {
        Status status = FunctionblockFactory.eINSTANCE.createStatus();
        this.fb.setStatus(status);
      }

      this.fb.getStatus().getProperties().add(BuilderUtils.createProperty(name, type));
      return this;
    }

    public FunctionblockBuilder withStatusProperty(String name, Type type) {
      if (this.fb.getStatus() == null) {
        Status status = FunctionblockFactory.eINSTANCE.createStatus();
        this.fb.setStatus(status);
      }

      this.fb.getStatus().getProperties().add(BuilderUtils.createProperty(name, type));
      return this;
    }
    
    public FunctionblockBuilder withStatusProperty(Property property) {
      if (this.fb.getStatus() == null) {
        Status status = FunctionblockFactory.eINSTANCE.createStatus();
        this.fb.setStatus(status);
      }

      this.fb.getStatus().getProperties().add(property);
      return this;
    }

    public FunctionblockBuilder withConfiguration(Property property) {
      if (this.fb.getConfiguration() == null) {
        Configuration configuration = FunctionblockFactory.eINSTANCE.createConfiguration();
        this.fb.setConfiguration(configuration);
      }
      this.fb.getConfiguration().getProperties().add(property);
      return this;
    }

    public FunctionblockBuilder withConfiguration(String name, PrimitiveType type) {
      if (this.fb.getConfiguration() == null) {
        Configuration configuration = FunctionblockFactory.eINSTANCE.createConfiguration();
        this.fb.setConfiguration(configuration);
      }
      this.fb.getConfiguration().getProperties().add(BuilderUtils.createProperty(name, type));
      return this;
    }

    public FunctionblockBuilder withConfiguration(String name, Type type) {
      if (this.fb.getConfiguration() == null) {
        Configuration configuration = FunctionblockFactory.eINSTANCE.createConfiguration();
        this.fb.setConfiguration(configuration);
      }
      this.fb.getConfiguration().getProperties().add(BuilderUtils.createProperty(name, type));
      return this;
    }
    
    public FunctionblockBuilder withEvent(Event event) {
      this.fb.getEvents().add(event);
      return this;
    }
    
    public FunctionblockBuilder withOperation(String operationName, ReturnType returnType, String description, boolean breakable, Param... params) {
      Operation operation = FunctionblockFactory.eINSTANCE.createOperation();
      operation.setName(operationName);
      operation.setDescription(description);
      operation.setBreakable(breakable);
      operation.setReturnType(returnType);
      if (params != null && params.length > 0) {
        operation.getParams().addAll(Arrays.asList(params));
      }
      
      this.fb.getOperations().add(operation);
      return this;
    }    
  }

}
