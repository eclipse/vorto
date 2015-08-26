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
import org.eclipse.vorto.core.api.model.mapping.ConfigurationSource;
import org.eclipse.vorto.core.api.model.mapping.FaultSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.OperationSource;
import org.eclipse.vorto.core.api.model.mapping.StatusSource;

public class TestFunctionBlockMappingFactory {
	public static FunctionblockModel functionblockModel = TestFunctionBlockFactory.createFunctionBlockModel();

	public static FunctionBlockMapping createFunctionBlockMappingModel() {
		FunctionBlockMapping mappingModel = MappingFactory.eINSTANCE.createFunctionBlockMapping();
		mappingModel.setName("MyFunctionBlockMapping");
		mappingModel.getRules().add(createFunctionBlockAttributeToStereoTypeMappingRule());
		mappingModel.getRules().add(createFunctionBlockElementToStereoTypeMappingRule());

		return mappingModel;
	}

	public static FunctionBlockMappingRule createFunctionBlockAttributeToStereoTypeMappingRule() {
		FunctionBlockMappingRule rule = MappingFactory.eINSTANCE.createFunctionBlockMappingRule();
		rule.getSources().add(createFunctionBlockAttributeSource());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeTarget());
		return rule;
	}

	private static FunctionBlockAttributeSource createFunctionBlockAttributeSource() {
		FunctionBlockAttributeSource source = MappingFactory.eINSTANCE.createFunctionBlockAttributeSource();
		source.setAttribute(FunctionblockModelAttribute.DESCRIPTION);
		source.setModel(functionblockModel);
		return source;
	}

	private static FunctionBlockMappingRule createFunctionBlockElementToStereoTypeMappingRule() {
		FunctionBlockMappingRule rule = MappingFactory.eINSTANCE.createFunctionBlockMappingRule();
		rule.getSources().add(createConfigurationSource());
		rule.getSources().add(createStatusSource());
		rule.getSources().add(createFaultSource());
		rule.getSources().add(createOperationSource());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeTarget());
		return rule;
	}

	private static ConfigurationSource createConfigurationSource() {
		ConfigurationSource source = MappingFactory.eINSTANCE.createConfigurationSource();
		source.setProperty(TestEntityFactory.createPrimitiveProperty("configStringParam", PrimitiveType.STRING));
		source.setModel(functionblockModel);
		return source;
	}

	private static StatusSource createStatusSource() {
		StatusSource source = MappingFactory.eINSTANCE.createStatusSource();
		source.setProperty(TestEntityFactory.createPrimitiveProperty("statusStringParam", PrimitiveType.STRING));
		source.setModel(functionblockModel);
		return source;
	}

	private static FaultSource createFaultSource() {
		FaultSource source = MappingFactory.eINSTANCE.createFaultSource();
		source.setProperty(TestEntityFactory.createPrimitiveProperty("faultStringParam", PrimitiveType.STRING));
		source.setModel(functionblockModel);
		return source;
	}

	private static OperationSource createOperationSource() {
		OperationSource source = MappingFactory.eINSTANCE.createOperationSource();
		source.setOperation(TestFunctionBlockFactory.createOnOperation());
		source.setModel(functionblockModel);
		return source;
	}

}
