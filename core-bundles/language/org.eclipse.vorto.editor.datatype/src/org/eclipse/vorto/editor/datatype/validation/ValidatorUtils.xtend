/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.editor.datatype.validation

import com.google.common.base.Function
import com.google.common.collect.Lists
import java.util.Collection
import java.util.HashMap
import java.util.Map
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.datatype.PropertyType
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.core.api.model.model.ModelReference
import org.eclipse.vorto.editor.datatype.validation.ValidatorUtils.ModelTypeBasedChildrenSupplier

public class ValidatorUtils {
	
	public static def EObject getParentOfType(EObject obj, Class<?> type) {
		if (obj === null) {
			return null;
		}
		
		if(type.isInstance(obj)) {
			return obj;
		} else {
			return getParentOfType(obj.eContainer, type);	
		}
	}
	
	public static def boolean isTypeInReferences(Model type, EList<ModelReference> references) {
		val propertySig = type.namespace + "." + type.name + ":" +  type.version
		for(ref : references) {
			val refSig = ref.importedNamespace + ":" + ref.version
			if (propertySig.equals(refSig)) {
				return true;
			}
		}
		return false
	}
	
	public static def boolean hasCircularReference(Model parent, Model child, ModelTypeBasedChildrenSupplier modelTypeChildrenSupplier) {
		if (isEquals(parent, child)) {
			return true;
		}
		
		val childrenSupplier = modelTypeChildrenSupplier.apply(child.class);
		if (childrenSupplier !== null) {
			for(Model childrenOfChildren : childrenSupplier.apply(child)) {
				if(hasCircularReference(parent, childrenOfChildren, modelTypeChildrenSupplier)) {
					return true;
				}	
			}	
		}
		
		return false;
	}
	
	private static def isEquals(Model a, Model b) {
		if ((a instanceof Entity && b instanceof Entity) || 
			(a instanceof Enum && b instanceof Enum) ||
			(a instanceof FunctionblockModel && b instanceof FunctionblockModel) ||
			(a instanceof InformationModel && b instanceof InformationModel)) {
			return a.namespace.equals(b.namespace) && a.name.equals(b.name) && a.version.equals(b.version);	
		}
		return false;
	}
	
	public static interface ModelTypeBasedChildrenSupplier extends Function<Class<?>, Function<Model, Collection<Model>>> {}
	public static val entityTypeToChildrenSupplierFunction = newModelTypeChildrenSupplier(newEntityToFunctionMap());
	
	private static def newEntityToFunctionMap() {
		val entityToFunctionMap = new HashMap<Class<?>, Function<Model, Collection<Model>>>();
		entityToFunctionMap.put(typeof(Entity), new EntityChildrenSupplier());
		return entityToFunctionMap;
	}
	
	public static def newModelTypeChildrenSupplier(Map<Class<?>, Function<Model, Collection<Model>>> entityToFunctionMap) {
		return new ModelTypeBasedChildrenSupplier() {
			override apply(Class<?> input) {
				for(entry : entityToFunctionMap.entrySet) {
					if (entry.key.isAssignableFrom(input)) {
						return entry.value;
					}
				}
				return null;
			}
		};
	}
	
	public static abstract class AbstractChildrenSupplier implements Function<Model, Collection<Model>> {
		def Collection<Model> getReferenceModels(Collection<Property> properties) {
			val models = Lists.newArrayList();
			for(property : properties) {
				models.addAll(getReferenceModels(property.type));
			}
			return models;
		}
		
		def Collection<Model> getReferenceModels(PropertyType type) {
			val models = Lists.newArrayList();
			if (type instanceof ObjectPropertyType) {
				val propertyType = type as ObjectPropertyType;
				models.add(propertyType.type);
			} else if (type instanceof DictionaryPropertyType) {
				val dictionaryType = type as DictionaryPropertyType;
				models.addAll(getReferenceModels(dictionaryType.keyType));
				models.addAll(getReferenceModels(dictionaryType.valueType));
			}
			return models;
		}
	}
	
	public static class EntityChildrenSupplier extends AbstractChildrenSupplier {		
		override apply(Model input) {
			val children = Lists.newArrayList();
			val parent = input as Entity;
			
			if (parent.superType !== null) {
				children.add(parent.superType);	
			}
			
			children.addAll(getReferenceModels(parent.properties))
			
			return children;
		}
	}	
}
