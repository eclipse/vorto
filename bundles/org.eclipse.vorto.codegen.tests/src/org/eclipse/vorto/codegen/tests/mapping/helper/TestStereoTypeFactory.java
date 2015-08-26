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

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;


public class TestStereoTypeFactory {

	public static StereoTypeTarget createStereoTypeTarget() {
		StereoTypeTarget stereoType = MappingFactory.eINSTANCE.createStereoTypeTarget();
		Attribute typeAttribute = MappingFactory.eINSTANCE.createAttribute();
		typeAttribute.setName("DummyAttribute");
		typeAttribute.setValue("Dummy Attribute Value");

		stereoType.setName("DummyStereoType");
		stereoType.getAttributes().add(typeAttribute);

		return stereoType;

	}
}
