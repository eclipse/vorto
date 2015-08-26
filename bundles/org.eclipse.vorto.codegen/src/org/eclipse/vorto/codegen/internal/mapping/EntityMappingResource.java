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
package org.eclipse.vorto.codegen.internal.mapping;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.codegen.api.mapping.MappingAttribute;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EntityPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.Source;

/**
 * @author sgp0247
 *
 */
public class EntityMappingResource extends AbstractMappingResource {
	public EntityMappingResource(MappingModel mappingModel) {
		super(mappingModel);
	}

	@Override
	protected void addRuleIfContainsModelObject(EObject modelObject, List<MappingRule> mappingRules, MappingRule rule,
			Source source) {
		if (source instanceof EntityPropertySource) {
			Property property = ((EntityPropertySource) source).getProperty();
			Entity entity = (Entity) ((EntityPropertySource) source).getModel();
			if (matchesEntityProperty(entity, property, modelObject)) {
				mappingRules.add(rule);
			}
		}
	}

	private boolean matchesEntityProperty(Entity entity, Property property, EObject modelObject) {
		if (!(modelObject instanceof Property)) {
			return false;
		}
		Property modelProperty = ((Property) modelObject);
		Entity modelEntity = (Entity) modelProperty.eContainer();
		return StringUtils.equals(modelProperty.getName(), property.getName())
				&& StringUtils.equals(modelEntity.getNamespace(), entity.getNamespace())
				&& StringUtils.equals(modelEntity.getVersion(), entity.getVersion());
	}

	@Override
	protected void addRuleIfContainsAttribute(MappingAttribute mappingAttribute, List<MappingRule> mappingRules,
			MappingRule rule, Source source) {

		if (source instanceof EntityAttributeSource) {
			ModelAttribute attribute = ((EntityAttributeSource) source).getAttribute();
			if (StringUtils.equals(attribute.toString(), mappingAttribute.name())) {
				mappingRules.add(rule);
			}
		}
	}
}
