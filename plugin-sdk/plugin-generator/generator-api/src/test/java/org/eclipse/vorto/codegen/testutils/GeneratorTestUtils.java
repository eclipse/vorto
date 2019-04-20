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
package org.eclipse.vorto.codegen.testutils;

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
import org.eclipse.vorto.core.api.model.model.ModelId;

public abstract class GeneratorTestUtils {
	
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
		return new FunctionblockBuilder().withId(modelId);
	}
	
	public static InformationModelBuilder newInformationModel(ModelId modelId) {
		return new InformationModelBuilder().withId(modelId);
	}
	
	public static EntityBuilder newEntity(ModelId modelId) {
		return new EntityBuilder().withId(modelId);
	}
	
	public static class EntityBuilder {
		
		private Entity entity;
		
		public EntityBuilder() {
			this.entity = DatatypeFactory.eINSTANCE.createEntity();
		}
		
		public EntityBuilder withId(ModelId id) {
			entity.setName(id.getName());
			entity.setDisplayname(id.getName());
			entity.setNamespace(id.getNamespace());
			entity.setVersion(id.getVersion());
			return this;
		}
		
		public EntityBuilder withProperty(String name, PrimitiveType type) {
			this.entity.getProperties().add(GeneratorTestUtils.createProperty(name, type));
			return this;
		}
		
		public EntityBuilder withProperty(String name, Type objectType) {
			this.entity.getProperties().add(GeneratorTestUtils.createProperty(name, objectType));
			return this;
		}
		
		public Entity build() {
			return this.entity;
		}
		
	}
	
	public static EnumBuilder newEnum(ModelId modelId) {
		return new EnumBuilder().withId(modelId);
	}
	
	public static class EnumBuilder {
		private Enum enumeration;
		
		public EnumBuilder() {
			this.enumeration = DatatypeFactory.eINSTANCE.createEnum();
		}
		
		public EnumBuilder withId(ModelId id) {
			enumeration.setName(id.getName());
			enumeration.setDisplayname(id.getName());
			enumeration.setNamespace(id.getNamespace());
			enumeration.setVersion(id.getVersion());
			return this;
		}
		
		public EnumBuilder withLiterals(String... values) {
			for (String value : values) {
				EnumLiteral literal = DatatypeFactory.eINSTANCE.createEnumLiteral();
				literal.setName(value);
				this.enumeration.getEnums().add(literal);
			}
			return this;
		}
		
		public Enum build() {
			return this.enumeration;
		}
	}
	
	public static class InformationModelBuilder {
		
		private InformationModel infomodel;
		
		public InformationModelBuilder() {
			this.infomodel = InformationModelFactory.eINSTANCE.createInformationModel();
		}
		
		public InformationModelBuilder withId(ModelId id) {
			infomodel.setName(id.getName());
			infomodel.setDisplayname(id.getName());
			infomodel.setNamespace(id.getNamespace());
			infomodel.setVersion(id.getVersion());
			return this;
		}
		
		public InformationModelBuilder withFunctionBlock(FunctionblockModel fbm, String propertyName) {
			FunctionblockProperty property = InformationModelFactory.eINSTANCE.createFunctionblockProperty();
			property.setName(propertyName);
			property.setType(fbm);
			this.infomodel.getProperties().add(property);
			return this;
		}
		
		public InformationModel build() {
			return this.infomodel;
		}
	}
	
	public static class FunctionblockBuilder {
		
		private FunctionblockModel fbm;
		private FunctionBlock  fb;
		
		public FunctionblockBuilder() {
			this.fbm = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
			this.fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();	
			this.fbm.setFunctionblock(fb);
		}
		
		public FunctionblockBuilder withId(ModelId id) {
			fbm.setName(id.getName());
			fbm.setDisplayname(id.getName());
			fbm.setNamespace(id.getNamespace());
			fbm.setVersion(id.getVersion());
			return this;
		}
		
		public FunctionblockBuilder withStatusProperty(String name, PrimitiveType type) {
			if (this.fb.getStatus() == null) {
				Status status = FunctionblockFactory.eINSTANCE.createStatus();
				this.fb.setStatus(status);
			}
		
			this.fb.getStatus().getProperties().add(GeneratorTestUtils.createProperty(name, type));
			return this;
		}
		
		public FunctionblockBuilder withStatusProperty(String name, Type type) {
			if (this.fb.getStatus() == null) {
				Status status = FunctionblockFactory.eINSTANCE.createStatus();
				this.fb.setStatus(status);
			}
		
			this.fb.getStatus().getProperties().add(GeneratorTestUtils.createProperty(name, type));
			return this;
		}
		
		public FunctionblockBuilder withConfiguration(String name, PrimitiveType type) {
			if (this.fb.getConfiguration() == null) {
				Configuration configuration = FunctionblockFactory.eINSTANCE.createConfiguration();				
				this.fb.setConfiguration(configuration);
			}
			this.fb.getConfiguration().getProperties().add(GeneratorTestUtils.createProperty(name, type));
			return this;
		}
		
		public FunctionblockBuilder withConfiguration(String name, Type type) {
			if (this.fb.getConfiguration() == null) {
				Configuration configuration = FunctionblockFactory.eINSTANCE.createConfiguration();				
				this.fb.setConfiguration(configuration);
			}
			this.fb.getConfiguration().getProperties().add(GeneratorTestUtils.createProperty(name, type));
			return this;
		}
		
		
		public FunctionblockModel build() {
			return fbm;
		}
	}
	
}
