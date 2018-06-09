/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.core.tests.model.helper;

import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingModel;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EntityPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;

public class TestEntityMappingFactory {
	public static org.eclipse.vorto.core.api.model.datatype.Entity entity = TestEntityFactory.createEntity();

	public static EntityMappingModel createEntityMappingModel() {
		EntityMappingModel entityMapping = MappingFactory.eINSTANCE.createEntityMappingModel();
		entityMapping.setName("MyEntityMapping");
		entityMapping.getRules().add(createEntityAttributeToStereoTypeMappingRule());
		entityMapping.getRules().add(createEntityElementToStereoTypeMappingRule());
		return entityMapping;
	}


	public static EntityMappingRule createEntityAttributeToStereoTypeMappingRule() {
		EntityMappingRule rule = MappingFactory.eINSTANCE.createEntityMappingRule();
		rule.getSources().add(createEntityAttributeSource());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeTarget());
		return rule;
	}

	private static EntityAttributeSource createEntityAttributeSource() {
		EntityAttributeSource source = MappingFactory.eINSTANCE.createEntityAttributeSource();
		source.setModel(entity);
		source.setAttribute(ModelAttribute.VERSION);
		return source;
	}

	private static EntityMappingRule createEntityElementToStereoTypeMappingRule() {
		EntityMappingRule rule = MappingFactory.eINSTANCE.createEntityMappingRule();
		rule.getSources().add(createEntityPropertySource());
		rule.setTarget(TestStereoTypeFactory.createStereoTypeTarget());
		return rule;
	}

	private static EntityPropertySource createEntityPropertySource() {
		EntityPropertySource source = MappingFactory.eINSTANCE.createEntityPropertySource();
		source.setModel(entity);
		source.setProperty(TestEntityFactory.createPrimitiveProperty("testString", PrimitiveType.STRING));	
		return source;
	}


}
