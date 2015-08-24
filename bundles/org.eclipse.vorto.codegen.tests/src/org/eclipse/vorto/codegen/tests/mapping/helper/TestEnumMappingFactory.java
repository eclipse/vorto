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

import org.eclipse.vorto.core.api.model.mapping.EnumAttributeElement;
import org.eclipse.vorto.core.api.model.mapping.EnumExpression;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EnumSourceElement;
import org.eclipse.vorto.core.api.model.mapping.Mapping;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;

/**
 * @author sgp0247
 *
 */
public class TestEnumMappingFactory {
	static org.eclipse.vorto.core.api.model.datatype.Enum enumType = TestEnumFactory.createEnum();

	public static MappingModel createEnumMappingModel() {
		MappingModel mappingModel = MappingFactory.eINSTANCE.createMappingModel();
		mappingModel.setName("MyEnumMapping");
		mappingModel.setMapping(createEnumMapping());
		return mappingModel;
	}

	private static Mapping createEnumMapping() {
		EnumMapping enumMapping = MappingFactory.eINSTANCE.createEnumMapping();
		enumMapping.getEnumMappingRules().add(createEnumAttributeToStereoTypeMappingRule());
		enumMapping.getEnumMappingRules().add(createEnumElementToStereoTypeMappingRule());
		return enumMapping;
	}

	public static EnumMappingRule createEnumAttributeToStereoTypeMappingRule() {
		EnumMappingRule rule = MappingFactory.eINSTANCE.createEnumMappingRule();
		rule.getEnumSourceElement().add(createEnumAttributeSourceElement());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeReference());
		return rule;
	}

	private static EnumSourceElement createEnumAttributeSourceElement() {
		EnumAttributeElement sourceElement = MappingFactory.eINSTANCE.createEnumAttributeElement();
		sourceElement.setTypeRef(enumType);
		sourceElement.setAttribute(ModelAttribute.VERSION);
		return sourceElement;
	}

	private static EnumMappingRule createEnumElementToStereoTypeMappingRule() {
		EnumMappingRule rule = MappingFactory.eINSTANCE.createEnumMappingRule();
		rule.getEnumSourceElement().add(createEnumElementSourceElement());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeReference());
		return rule;
	}

	private static EnumSourceElement createEnumElementSourceElement() {
		EnumExpression sourceElement = MappingFactory.eINSTANCE.createEnumExpression();
		sourceElement.setTypeRef(enumType);
		sourceElement.setLiteral(enumType.getEnums().get(0));
		return sourceElement;
	}
}
