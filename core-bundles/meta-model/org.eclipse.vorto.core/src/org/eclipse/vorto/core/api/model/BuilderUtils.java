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

import java.util.List;
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
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.VortoLangVersion;

public abstract class BuilderUtils {

  private static Property createProperty(String name, Type type) {
    Property prop = DatatypeFactory.eINSTANCE.createProperty();
    prop.setName(name);
    prop.setType(createObjectType(type));
    return prop;
  }

  private static ObjectPropertyType createObjectType(Type type) {
    ObjectPropertyType typeObj = DatatypeFactory.eINSTANCE.createObjectPropertyType();
    typeObj.setType(type);
    return typeObj;
  }

  private static Property createProperty(String name, PrimitiveType type) {
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

    public T build() {
      return (T)model;
    }
  }

  public static class EntityBuilder extends ModelBuilder<Entity>{

    public EntityBuilder() {
      super(DatatypeFactory.eINSTANCE.createEntity());
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

    public InformationModelBuilder withFunctionBlock(FunctionblockModel fbm, String propertyName) {
      FunctionblockProperty property =
          InformationModelFactory.eINSTANCE.createFunctionblockProperty();
      property.setName(propertyName);
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
  }

}
