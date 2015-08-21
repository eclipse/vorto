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

import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationElement;
import org.eclipse.vorto.core.api.model.mapping.FBTypeElement;
import org.eclipse.vorto.core.api.model.mapping.FaultElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.Mapping;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.OperationElement;
import org.eclipse.vorto.core.api.model.mapping.StatusElement;

public class TestFunctionBlockMappingFactory {
	static FunctionblockModel functionblockModel = TestFunctionBlockFactory.createFunctionBlockModel();

	public static MappingModel createFunctionBlockMappingModel() {
		MappingModel mappingModel = MappingFactory.eINSTANCE.createMappingModel();
		mappingModel.setName("MyFunctionBlockMapping");
		mappingModel.setMapping(createFunctionBlockMapping());
		return mappingModel;
	}

	private static Mapping createFunctionBlockMapping() {
		FunctionBlockMapping functionBlockMapping = MappingFactory.eINSTANCE.createFunctionBlockMapping();
		functionBlockMapping.getFunctionBlockMappingRules().add(createFunctionBlockAttributeToStereoTypeMappingRule());
		functionBlockMapping.getFunctionBlockMappingRules().add(createFunctionBlockElementToStereoTypeMappingRule());
		return functionBlockMapping;
	}

	public static FunctionBlockMappingRule createFunctionBlockAttributeToStereoTypeMappingRule() {
		FunctionBlockMappingRule rule = MappingFactory.eINSTANCE.createFunctionBlockMappingRule();
		rule.getFunctionBlockSourceElements().add(createFunctionBlockAttributeSourceElement());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeReference());
		return rule;
	}

	private static FunctionBlockSourceElement createFunctionBlockAttributeSourceElement() {
		FunctionBlockElementAttribute attributeElement = MappingFactory.eINSTANCE.createFunctionBlockElementAttribute();
		attributeElement.setAttribute(FunctionblockModelAttribute.DESCRIPTION);

		FunctionBlockSourceElement functionBlockSourceElement = MappingFactory.eINSTANCE
				.createFunctionBlockSourceElement();
		functionBlockSourceElement.setFunctionblock(functionblockModel);
		functionBlockSourceElement.setFunctionBlockElement(attributeElement);
		return functionBlockSourceElement;
	}

	private static FunctionBlockMappingRule createFunctionBlockElementToStereoTypeMappingRule() {
		FunctionBlockMappingRule rule = MappingFactory.eINSTANCE.createFunctionBlockMappingRule();
		rule.getFunctionBlockSourceElements().add(createFunctionBlockConfigSourceElement());
		rule.getFunctionBlockSourceElements().add(createFunctionBlockStatusSourceElement());
		rule.getFunctionBlockSourceElements().add(createFunctionBlockFaultSourceElement());
		rule.getFunctionBlockSourceElements().add(createFunctionBlockOperationSourceElement());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeReference());
		return rule;
	}

	private static FunctionBlockSourceElement createFunctionBlockConfigSourceElement() {
		ConfigurationElement sourceElement = MappingFactory.eINSTANCE.createConfigurationElement();
		sourceElement.setTypeRef(createFBTypeElement("configStringParam"));

		FunctionBlockSourceElement functionBlockSourceElement = createFunctionBlockSourceElement(sourceElement);
		return functionBlockSourceElement;
	}

	private static FunctionBlockSourceElement createFunctionBlockStatusSourceElement() {
		StatusElement sourceElement = MappingFactory.eINSTANCE.createStatusElement();
		sourceElement.setTypeRef(createFBTypeElement("statusStringParam"));

		FunctionBlockSourceElement functionBlockSourceElement = createFunctionBlockSourceElement(sourceElement);
		return functionBlockSourceElement;
	}

	private static FunctionBlockSourceElement createFunctionBlockFaultSourceElement() {
		FaultElement sourceElement = MappingFactory.eINSTANCE.createFaultElement();
		sourceElement.setTypeRef(createFBTypeElement("faultStringParam"));

		FunctionBlockSourceElement functionBlockSourceElement = createFunctionBlockSourceElement(sourceElement);
		return functionBlockSourceElement;
	}

	private static FunctionBlockSourceElement createFunctionBlockOperationSourceElement() {
		OperationElement sourceElement = MappingFactory.eINSTANCE.createOperationElement();
		sourceElement.setOperation(TestFunctionBlockFactory.createOnOperation());

		FunctionBlockSourceElement functionBlockSourceElement = createFunctionBlockSourceElement(sourceElement);
		return functionBlockSourceElement;
	}

	private static FunctionBlockSourceElement createFunctionBlockSourceElement(
			FunctionBlockChildElement sourceElement) {
		FunctionBlockSourceElement functionBlockSourceElement = MappingFactory.eINSTANCE
				.createFunctionBlockSourceElement();
		functionBlockSourceElement.setFunctionblock(functionblockModel);
		functionBlockSourceElement.setFunctionBlockElement(sourceElement);
		return functionBlockSourceElement;
	}

	private static FBTypeElement createFBTypeElement(String paramName) {
		FBTypeElement fbTypeElement = MappingFactory.eINSTANCE.createFBTypeElement();
		fbTypeElement.setProperty(TestEntityFactory.createPrimitiveProperty(paramName, PrimitiveType.STRING));
		return fbTypeElement;
	}

}
