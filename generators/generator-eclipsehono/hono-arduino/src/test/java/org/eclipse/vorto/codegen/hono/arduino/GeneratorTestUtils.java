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
package org.eclipse.vorto.codegen.hono.arduino;

import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.model.ModelId;

public abstract class GeneratorTestUtils {

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
		
		public Entity build() {
			return this.entity;
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
		
		public FunctionblockBuilder withConfiguration(String name, PrimitiveType type) {
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
