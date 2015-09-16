/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty

class ConfigurationClassTemplate implements ITemplate<FunctionblockProperty> {
		
	override getContent(FunctionblockProperty fbProperty) {
		var FunctionblockModel model = fbProperty.type
		
		return '''
		package «ModuleUtil.getModelPackage(model)»;
		
		import java.beans.BeanInfo;
		import java.beans.Introspector;
		import java.beans.PropertyDescriptor;
		import java.util.HashMap;
		import java.util.Map;

		
		import org.codehaus.jackson.map.annotate.JsonSerialize;
		«JavaClassGeneratorUtils.getImports(model.functionblock.status.properties)»
		«JavaClassGeneratorUtils.getImports(model.functionblock.configuration.properties)»
		«JavaClassGeneratorUtils.getImports(model.functionblock.fault.properties)»
		
		@JsonSerialize
		public class «ModuleUtil.getCamelCase(fbProperty.name)»Configuration {
			public «ModuleUtil.getCamelCase(fbProperty.name)»Configuration() {
				try {
					Class<?> c = Class.forName(«ModuleUtil.getCamelCase(fbProperty.name)»Configuration.class
						.getCanonicalName());
					BeanInfo beanInfo = Introspector.getBeanInfo(c);
					PropertyDescriptor propertyDescriptor[] = beanInfo
						.getPropertyDescriptors();

					for (int i = 0; i < propertyDescriptor.length; i++) {
						if (!propertyDescriptor[i].getName().equals("configData")
							&& !propertyDescriptor[i].getName().equals("class")) {
							Class<?> type = propertyDescriptor[i].getPropertyType();
							String typeName = (type instanceof Class && ((Class<?>) type)
								.isEnum()) ? "enum_" + type.getSimpleName() : type.getSimpleName();
							configData.put(propertyDescriptor[i].getName(), typeName);
						}

					}
				} catch (Exception e) {
					System.out.println("Exception caught. " + e);
					}
			}
		«IF model.functionblock.configuration!=null»
			«FOR configurationField : model.functionblock.configuration.properties»	
			«IF configurationField.type instanceof PrimitivePropertyType»			
				«var primitiveType = (configurationField.type as PrimitivePropertyType).getType»
				«var primitiveJavaType = PropertyUtil.toJavaFieldType(primitiveType)»
				
					private «primitiveJavaType» «configurationField.name» = «PropertyUtil.getDefaultValue(primitiveType)»;
				
					public «primitiveJavaType» get«configurationField.name.toFirstUpper»() {
						return «configurationField.name»;
					}
							
					public void set«configurationField.name.toFirstUpper»(«primitiveJavaType» «configurationField.name») {
						this.«configurationField.name» = «configurationField.name»;
					}
					
		    «ELSEIF configurationField.type instanceof ObjectPropertyType»
				«var objectType = (configurationField.type as ObjectPropertyType).getType»
				«IF objectType instanceof Entity»
						private «objectType.name.toFirstUpper» «configurationField.name» = new «objectType.name»();
				«ELSEIF objectType instanceof Enum»
					
						private «objectType.name.toFirstUpper» «configurationField.name»;
					
						private «objectType.name.toFirstUpper»[] enum_«objectType.name.toFirstUpper» = «objectType.name.toFirstUpper».values();
					
						public «objectType.name.toFirstUpper»[] getEnum_«objectType.name.toFirstUpper»() {
							return enum_«objectType.name.toFirstUpper»;
						}
					
						public void setEnum_«objectType.name.toFirstUpper»(«objectType.name.toFirstUpper»[] enum_«objectType.name.toFirstUpper») {
							this.enum_«objectType.name.toFirstUpper» = enum_«objectType.name.toFirstUpper»;
						}
						
				«ENDIF»
				
					public «objectType.name.toFirstUpper» get«configurationField.name.toFirstUpper»() {
						return «configurationField.name»;
					}
				
					public void set«configurationField.name.toFirstUpper»(«objectType.name.toFirstUpper» «configurationField.name») {
						this.«configurationField.name» = «configurationField.name»;
					}
			«ENDIF»
			«ENDFOR»
		«ENDIF»			
		
			private Map<String, String> configData = new HashMap<String, String>();
		
			public Map<String, String> getConfigData() {
				return configData;
			}
		
			public void setConfigData(Map<String, String> configData) {
				this.configData = configData;
			}
		}'''
	}
}