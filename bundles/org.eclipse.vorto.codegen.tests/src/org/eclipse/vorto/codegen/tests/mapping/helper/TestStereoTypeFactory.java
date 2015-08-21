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
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeElement;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeReference;


public class TestStereoTypeFactory {
	public static StereoTypeReference createStereoTypeReference() {

		StereoTypeReference stereoTypeReference = MappingFactory.eINSTANCE.createStereoTypeReference();
		StereoTypeElement stereoTypeElement = MappingFactory.eINSTANCE.createStereoTypeElement();
		stereoTypeElement.getStereoTypes().add(createStereoType());
		stereoTypeReference.setTargetElement(stereoTypeElement);
		return stereoTypeReference;
	}

	public static StereoType createStereoType() {
		StereoType stereoType = MappingFactory.eINSTANCE.createStereoType();
		Attribute typeAttribute = MappingFactory.eINSTANCE.createAttribute();
		typeAttribute.setName("DummyAttribute");
		typeAttribute.setValue("Dummy Attribute Value");

		stereoType.setName("DummyStereoType");
		stereoType.getAttributes().add(typeAttribute);

		return stereoType;

	}
}
