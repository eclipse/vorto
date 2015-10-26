/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;

public class EntityPropertyUtils {
	public static EList<Entity> getEntityProperties(Entity entity) {
		EList<Entity> resultList = new BasicEList<Entity>();
		for (ObjectPropertyType objectProperty : getObjectProperties(entity)) {
			if (objectProperty.getType() instanceof Entity) {
				resultList.add((Entity)objectProperty.getType());
			}
		}
		return resultList;
	}
	
	public static EList<Enum> getEnumProperties(Entity entity) {
		EList<Enum> resultList = new BasicEList<Enum>();
		for (ObjectPropertyType objectProperty : getObjectProperties(entity)) {
			if (objectProperty.getType() instanceof Enum) {
				resultList.add((Enum)objectProperty.getType());
			}
		}
		return resultList;
	}

	public static EList<PrimitivePropertyType> getPrimitiveProperties(Entity entity) {
		EList<PrimitivePropertyType> resultList = new BasicEList<PrimitivePropertyType>();
		for (Property property : getProperties(entity)) {
			if (property instanceof PrimitivePropertyType) {
				resultList.add((PrimitivePropertyType)property);
			}
		}
		return resultList;
	}
	
	public static EList<ObjectPropertyType> getObjectProperties(Entity entity) {
		EList<ObjectPropertyType> resultList = new BasicEList<ObjectPropertyType>();
		for (Property property : getProperties(entity)) {
			if (property.getType() instanceof ObjectPropertyType) {
				resultList.add((ObjectPropertyType)property.getType());
			}
		}
		return resultList;
	}
	
	public static EList<Property> getProperties(Entity entity) {
		if (entity != null)  {
			return entity.getProperties();
		}
		else {
			return new BasicEList<Property>();
		}
	}
}

