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
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;

/**
 * @author sgp0247
 *
 */
public class TestEnumFactory {

	public static org.eclipse.vorto.core.api.model.datatype.Enum createEnum() {
		org.eclipse.vorto.core.api.model.datatype.Enum enumType = DatatypeFactory.eINSTANCE.createEnum();

		EnumLiteral option1 = DatatypeFactory.eINSTANCE.createEnumLiteral();
		option1.setName("Option1");

		EnumLiteral option2 = DatatypeFactory.eINSTANCE.createEnumLiteral();
		option1.setName("Option2");
		enumType.getEnums().add(option1);
		enumType.getEnums().add(option2);
		return enumType;
	}
}
