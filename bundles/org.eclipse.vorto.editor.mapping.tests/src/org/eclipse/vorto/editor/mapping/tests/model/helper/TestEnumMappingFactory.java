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
package org.eclipse.vorto.editor.mapping.tests.model.helper;

import org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EnumPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;

/**
 * @author sgp0247
 *
 */
public class TestEnumMappingFactory {
	static org.eclipse.vorto.core.api.model.datatype.Enum enumType = TestEnumFactory.createEnum();

	public static EnumMapping createEnumMappingModel() {
		EnumMapping mappingModel = MappingFactory.eINSTANCE.createEnumMapping();
		mappingModel.setName("MyEnumMapping");
		mappingModel.getRules().add(createEnumAttributeToStereoTypeMappingRule());
		mappingModel.getRules().add(createEnumElementToStereoTypeMappingRule());
		return mappingModel;
	}

	public static EnumMappingRule createEnumAttributeToStereoTypeMappingRule() {
		EnumMappingRule rule = MappingFactory.eINSTANCE.createEnumMappingRule();
		rule.getSources().add(createEnumAttributeSource());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeTarget());
		return rule;
	}

	private static EnumAttributeSource createEnumAttributeSource() {
		EnumAttributeSource source = MappingFactory.eINSTANCE.createEnumAttributeSource();
		source.setModel(enumType);
		source.setAttribute(ModelAttribute.VERSION);
		return source;
	}

	private static EnumMappingRule createEnumElementToStereoTypeMappingRule() {
		EnumMappingRule rule = MappingFactory.eINSTANCE.createEnumMappingRule();
		rule.getSources().add(createEnumElementSourceElement());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeTarget());
		return rule;
	}

	static EnumPropertySource createEnumElementSourceElement() {
		EnumPropertySource source = MappingFactory.eINSTANCE.createEnumPropertySource();
		source.setModel(enumType);
		source.setProperty(enumType.getEnums().get(0));
		return source;
	}
}
