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

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelChild;
import org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement;
import org.eclipse.vorto.core.api.model.mapping.InformationModelProperty;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeElement;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeReference;

public class TestMappingInfoModelFactory {
	static InformationModel informationModel = TestInforModelFactory.createInformationModel();

	public static MappingModel createInfoModelMappingModel() {
		MappingModel mappingModel = MappingFactory.eINSTANCE.createMappingModel();
		mappingModel.setName("MyInfoModelMapping");
		mappingModel.setMapping(createInfoModelMapping());
		return mappingModel;
	}

	private static InfoModelMapping createInfoModelMapping() {
		InfoModelMapping infoModelMapping = MappingFactory.eINSTANCE.createInfoModelMapping();

		infoModelMapping.getInfoModelMappingRules().add(createInfoModelStereoTypeMappingRule());
		infoModelMapping.getInfoModelMappingRules().add(createInfoModelReferenceMappingRule());
		return infoModelMapping;
	}

	public static InfoModelMappingRule createInfoModelStereoTypeMappingRule() {
		InfoModelMappingRule rule = MappingFactory.eINSTANCE.createInfoModelMappingRule();
		rule.getInfoModelSourceElements().add(createInfoModelAttributeSourceElement());
		rule.setTarget(createStereoTypeReference());
		return rule;
	}

	private static InfoModelMappingRule createInfoModelReferenceMappingRule() {
		InfoModelMappingRule rule = MappingFactory.eINSTANCE.createInfoModelMappingRule();
		rule.getInfoModelSourceElements().add(createInfoModelModelSourceElement());
		rule.setTarget(createFunctionBlockReference());
		return rule;
	}

	private static FunctionBlockReference createFunctionBlockReference() {
		FunctionBlockReference functionBlockReference = MappingFactory.eINSTANCE.createFunctionBlockReference();

		functionBlockReference.setReference(null);
		return functionBlockReference;
	}

	private static InfoModelSourceElement createInfoModelModelSourceElement() {
		InfoModelSourceElement infoModelSourceElement = MappingFactory.eINSTANCE.createInfoModelSourceElement();
		infoModelSourceElement.setInfoModel(informationModel);
		infoModelSourceElement.setInfoModelChild(createInfoModelFbElement());
		return infoModelSourceElement;
	}

	private static InfoModelFbElement createInfoModelFbElement() {
		InfoModelFbElement infoModelFbElement = MappingFactory.eINSTANCE.createInfoModelFbElement();
		infoModelFbElement.setFunctionblock(informationModel.getProperties().get(0));
		return infoModelFbElement;
	}


	private static InfoModelSourceElement createInfoModelAttributeSourceElement() {
		InfoModelSourceElement infoModelSourceElement = MappingFactory.eINSTANCE.createInfoModelSourceElement();
		infoModelSourceElement.setInfoModel(informationModel);
		infoModelSourceElement.setInfoModelChild(createInformationModelProperty());
		return infoModelSourceElement;
	}

	private static InfoModelChild createInformationModelProperty() {
		InformationModelProperty informationModelProperty = MappingFactory.eINSTANCE.createInformationModelProperty();
		informationModelProperty.setAttribute(InfoModelAttribute.DISPLAYNAME);

		return informationModelProperty;
	}

	private static StereoTypeReference createStereoTypeReference() {

		StereoTypeReference stereoTypeReference = MappingFactory.eINSTANCE.createStereoTypeReference();
		StereoTypeElement stereoTypeElement = MappingFactory.eINSTANCE.createStereoTypeElement();
		stereoTypeElement.getStereoTypes().add(createStereoType());
		stereoTypeReference.setTargetElement(stereoTypeElement);
		return stereoTypeReference;
	}

	private static StereoType createStereoType() {
		StereoType stereoType = MappingFactory.eINSTANCE.createStereoType();
		Attribute typeAttribute = MappingFactory.eINSTANCE.createAttribute();
		typeAttribute.setName("DummyAttribute");
		typeAttribute.setValue("Dummy Attribute Value");

		stereoType.setName("DummyStereoType");
		stereoType.getAttributes().add(typeAttribute);

		return stereoType;

	}

}
