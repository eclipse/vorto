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
package org.eclipse.vorto.core.tests.model.helper;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingModel;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.ReferenceTarget;

public class TestInfoModelMappingFactory {
	public static InformationModel informationModel = TestInfoModelFactory.createInformationModel();

	public static InfoModelMappingModel createInfoModelMappingModel() {
		InfoModelMappingModel mappingModel = MappingFactory.eINSTANCE.createInfoModelMappingModel();
		mappingModel.setName("MyInfoModelMapping");
		mappingModel.getRules().add(createInfoModelStereoTypeMappingRule());
		mappingModel.getRules().add(createInfoModelReferenceMappingRule());
		return mappingModel;
	}

	public static InfoModelMappingRule createInfoModelStereoTypeMappingRule() {
		InfoModelMappingRule rule = MappingFactory.eINSTANCE.createInfoModelMappingRule();
		rule.getSources().add(createInfoModelAttributeSource());
		rule.getSources().add(createInfoModelSource());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeTarget());
		return rule;
	}

	private static InfoModelMappingRule createInfoModelReferenceMappingRule() {
		InfoModelMappingRule rule = MappingFactory.eINSTANCE.createInfoModelMappingRule();
		rule.getSources().add(createInfoModelModelSourceElement());
		rule.setTarget(createReferenceTarget());
		return rule;
	}

	private static ReferenceTarget createReferenceTarget() {
		ReferenceTarget referenceTarget = MappingFactory.eINSTANCE.createReferenceTarget();

		referenceTarget.setMappingModel(null);
		return referenceTarget;
	}

	private static InfoModelPropertySource createInfoModelModelSourceElement() {
		InfoModelPropertySource infoModelPropertySource = MappingFactory.eINSTANCE.createInfoModelPropertySource();
		infoModelPropertySource.setModel(informationModel);
		infoModelPropertySource.setProperty(informationModel.getProperties().get(0));
		
		return infoModelPropertySource;
	}

	private static InfoModelAttributeSource createInfoModelSource() {
		InfoModelAttributeSource infoModelAttributeSource = MappingFactory.eINSTANCE.createInfoModelAttributeSource();
		infoModelAttributeSource.setModel(informationModel);
		return infoModelAttributeSource;
	}
	private static InfoModelAttributeSource createInfoModelAttributeSource() {
		InfoModelAttributeSource infoModelAttributeSource = MappingFactory.eINSTANCE.createInfoModelAttributeSource();
		infoModelAttributeSource.setModel(informationModel);
		infoModelAttributeSource.setAttribute(ModelAttribute.DISPLAYNAME);
		return infoModelAttributeSource;
	}
}
