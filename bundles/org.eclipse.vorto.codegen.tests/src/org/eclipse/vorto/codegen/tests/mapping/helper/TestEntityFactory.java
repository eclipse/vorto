/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.tests.mapping.helper;

import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;

/**
 * @author sgp0247
 *
 */
public class TestEntityFactory {
	public static Entity createEntity() {
		Entity entity = DatatypeFactory.eINSTANCE.createEntity();
		Property stringProperty = createPrimitiveProperty("testString", PrimitiveType.STRING);
		entity.getProperties().add(stringProperty);
		return entity;
	}

	public static Property createPrimitiveProperty(String propName, PrimitiveType type) {
		Property prop = DatatypeFactory.eINSTANCE.createProperty();
		prop.setName(propName);
		PrimitivePropertyType primi = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		primi.setType(type);
		prop.setType(primi);
		return prop;
	}
}
