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
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeElement;
import org.eclipse.vorto.core.api.model.mapping.EntityExpression;
import org.eclipse.vorto.core.api.model.mapping.EntityExpressionRef;
import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EntitySourceElement;
import org.eclipse.vorto.core.api.model.mapping.Mapping;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression;

/**
 * @author sgp0247
 *
 */
public class TestEntityMappingFactory {
	static org.eclipse.vorto.core.api.model.datatype.Entity entity = TestEntityFactory.createEntity();

	public static MappingModel createEntityMappingModel() {
		MappingModel mappingModel = MappingFactory.eINSTANCE.createMappingModel();
		mappingModel.setName("MyEntityMapping");
		mappingModel.setMapping(createEntityMapping());
		return mappingModel;
	}

	private static Mapping createEntityMapping() {
		EntityMapping entityMapping = MappingFactory.eINSTANCE.createEntityMapping();
		entityMapping.getEntityMappingRules().add(createEntityAttributeToStereoTypeMappingRule());
		entityMapping.getEntityMappingRules().add(createEntityElementToStereoTypeMappingRule());
		return entityMapping;
	}

	public static EntityMappingRule createEntityAttributeToStereoTypeMappingRule() {
		EntityMappingRule rule = MappingFactory.eINSTANCE.createEntityMappingRule();
		rule.getEntitySourceElement().add(createEntityAttributeSourceElement());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeReference());
		return rule;
	}

	private static EntitySourceElement createEntityAttributeSourceElement() {
		EntityAttributeElement sourceElement = MappingFactory.eINSTANCE.createEntityAttributeElement();
		sourceElement.setTypeRef(entity);
		sourceElement.setAttribute(ModelAttribute.DESCRIPTION);
		return sourceElement;
	}

	private static EntityMappingRule createEntityElementToStereoTypeMappingRule() {
		EntityMappingRule rule = MappingFactory.eINSTANCE.createEntityMappingRule();
		rule.getEntitySourceElement().add(createEntityElementSourceElement());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeReference());
		return rule;
	}

	private static EntitySourceElement createEntityElementSourceElement() {
		NestedEntityExpression sourceElement = MappingFactory.eINSTANCE.createNestedEntityExpression();
		sourceElement.setRef(createEntityExpression());
		sourceElement.setTail(TestEntityFactory.createPrimitiveProperty("testString", PrimitiveType.STRING));
		return sourceElement;
	}

	private static EntityExpressionRef createEntityExpression() {
		EntityExpression entityExpression =MappingFactory.eINSTANCE.createEntityExpression();
		entityExpression.setEntity(entity);
		return entityExpression;
	}

}
