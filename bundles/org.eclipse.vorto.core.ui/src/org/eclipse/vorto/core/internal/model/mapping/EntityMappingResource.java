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
package org.eclipse.vorto.core.internal.model.mapping;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EntityPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.model.IMapping;

public class EntityMappingResource extends AbstractMappingResource {
	public EntityMappingResource(MappingModel mappingModel, List<IMapping> referenceMappings) {
		super(mappingModel, referenceMappings);
	}

	@Override
	protected void addRuleIfContainsModelObject(EObject modelObject, List<MappingRule> mappingRules, MappingRule rule,
			Source source) {

		if (modelObject instanceof Entity) {
			addRuleIfMatchesEntity((Entity) modelObject, mappingRules, rule, source);
		} else if (modelObject instanceof Property) {
			addRuleIfMatchesEntityProperty(modelObject, mappingRules, rule, source);
		}

	}

	/**
	 * @param modelObject
	 * @param mappingRules
	 * @param rule
	 * @param source
	 */
	private void addRuleIfMatchesEntity(Entity modelObject, List<MappingRule> mappingRules, MappingRule rule,
			Source source) {
		if (source instanceof EntityPropertySource) {
			Entity entity = (Entity) ((EntityPropertySource) source).getModel();
			if (this.matchesModel(modelObject, entity)) {
				mappingRules.add(rule);
			}
		}
	}

	private void addRuleIfMatchesEntityProperty(EObject modelObject, List<MappingRule> mappingRules, MappingRule rule,
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
		if ((property == null) || !(modelObject instanceof Property)) {
			return false;
		}
		Property modelProperty = ((Property) modelObject);
		Entity modelEntity = (Entity) modelProperty.eContainer();
		return this.matchesModel(entity, modelEntity);
	}

	@Override
	protected void addRuleIfContainsAttribute(ModelAttribute modelAttribute, List<MappingRule> mappingRules,
			MappingRule rule, Source source) {

		if (source instanceof EntityAttributeSource) {
			ModelAttribute attribute = ((EntityAttributeSource) source).getAttribute();
			if (modelAttribute == attribute) {
				mappingRules.add(rule);
			}
		}
	}

}
