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
package org.eclipse.vorto.codegen.tests;

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.Rule;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.TargetElement;

public class TestMappingModelFactory {
	public static MappingModel createRuleModel() {
		MappingModel mappingModel = MappingFactory.eINSTANCE
				.createMappingModel();
		mappingModel.setInfomodel(TestInforModelFactory
				.createInformationModel());
		mappingModel.getRules().add(createChannelTypeRule());
		mappingModel.getRules().add(createConfigurationRule());
		return mappingModel;
	}

	private static Rule createChannelTypeRule() {
		Rule rule = MappingFactory.eINSTANCE.createRule();
		TargetElement targetElement = MappingFactory.eINSTANCE
				.createTargetElement();
		StereoType stereoType = MappingFactory.eINSTANCE.createStereoType();
		Attribute typeAttribute = MappingFactory.eINSTANCE.createAttribute();
		typeAttribute.setName("itemType");
		typeAttribute.setValue("Color");

		Attribute nameAttribute = MappingFactory.eINSTANCE.createAttribute();
		nameAttribute.setName("name");
		nameAttribute.setValue("color");

		stereoType.setName("channelType");
		stereoType.getAttributes().add(nameAttribute);
		stereoType.getAttributes().add(typeAttribute);

		targetElement.getStereoTypes().add(stereoType);

		rule.setTargetElement(targetElement);
		return rule;
	}

	private static Rule createConfigurationRule() {
		// rule.setTargetElement("config-description:brightness @type:Number");

		Rule rule = MappingFactory.eINSTANCE.createRule();
		TargetElement targetElement = MappingFactory.eINSTANCE
				.createTargetElement();
		StereoType stereoType = MappingFactory.eINSTANCE.createStereoType();

		Attribute typeAttribute = MappingFactory.eINSTANCE.createAttribute();
		typeAttribute.setName("type");
		typeAttribute.setValue("Number");

		Attribute nameAttribute = MappingFactory.eINSTANCE.createAttribute();
		nameAttribute.setName("name");
		nameAttribute.setValue("brightness");

		stereoType.setName("configDescription");
		stereoType.getAttributes().add(nameAttribute);
		stereoType.getAttributes().add(typeAttribute);

		targetElement.getStereoTypes().add(stereoType);

		rule.setTargetElement(targetElement);

		return rule;
	}
}
