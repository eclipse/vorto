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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.codegen.api.mapping.IMappingRule;
import org.eclipse.vorto.codegen.api.mapping.IMappingRules;
import org.eclipse.vorto.codegen.api.mapping.MappingAttribute;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeElement;
import org.eclipse.vorto.core.api.model.mapping.EntityExpression;
import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EntitySourceElement;
import org.eclipse.vorto.core.api.model.mapping.EntityTargetElement;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeElement;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeReference;

/**
 * @author sgp0247
 *
 */
public class EntityMappingRules implements IMappingRules {
	private MappingModel mappingModel;

	public EntityMappingRules(MappingModel mappingModel) {
		this.mappingModel = mappingModel;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRules#getRules(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public List<IMappingRule> getRules(EObject modelElement) {
		EntityMapping mapping = (EntityMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (EntityMappingRule rule : mapping.getEntityMappingRules()) {
			for (EntitySourceElement sourceElement : rule.getEntitySourceElement()) {
				this.addRuleIfContainsModelElement(modelElement, mappingRules, rule, sourceElement);
			}
		}

		return mappingRules;
	}

	private void addRuleIfContainsModelElement(EObject modelElement, List<IMappingRule> mappingRules,
			EntityMappingRule rule, EntitySourceElement sourceElement) {

		if (sourceElement instanceof NestedEntityExpression) {
			 NestedEntityExpression nestedEntityExpression = (NestedEntityExpression)sourceElement;
			 Property entityProperty = nestedEntityExpression.getTail();
			 EntityExpression entityExpression = (EntityExpression) nestedEntityExpression.getRef();
			 Entity entity = entityExpression.getEntity();
			if (matchesFunctionBlockModel(entity,entityProperty, modelElement)) {
				mappingRules.add(new EntityMappingRuleWrapper(rule));
			}
		}
	}
	/**
	 * @param entityType
	 * @param entityLiteral
	 * @param modelElement
	 * @return
	 */
	private boolean matchesFunctionBlockModel(Entity entity, Property entityProperty, EObject modelElement) {
		if (!(modelElement instanceof Property)) {
			return false;
		}

		Property modelElementProperty = ((Property) modelElement);
		Entity elementEntity = (Entity)modelElementProperty.eContainer();
		return StringUtils.equals(modelElementProperty.getName(), entityProperty.getName())
				&& StringUtils.equals(elementEntity.getNamespace(), entity.getNamespace())
				&& StringUtils.equals(elementEntity.getVersion(), entity.getVersion());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRules#getRules(java.lang.String)
	 */
	@Override
	public List<IMappingRule> getRules(MappingAttribute mappingAttribute) {
		EntityMapping mapping = (EntityMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (EntityMappingRule rule : mapping.getEntityMappingRules()) {
			for (EntitySourceElement sourceElement : rule.getEntitySourceElement()) {
				addRuleIfContainsAttribute(mappingAttribute, mappingRules, rule, sourceElement);
			}
		}
		return mappingRules;
	}

	private void addRuleIfContainsAttribute(MappingAttribute mappingAttribute, List<IMappingRule> mappingRules,
			EntityMappingRule rule, EntitySourceElement sourceElement) {

		if (sourceElement instanceof EntityAttributeElement) {
			EntityAttributeElement attributeElement = (EntityAttributeElement) sourceElement;
			if (StringUtils.equals(attributeElement.getAttribute().toString(), mappingAttribute.name())) {
				mappingRules.add(new EntityMappingRuleWrapper(rule));
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRules#getRulesContainStereoType(java.lang.String)
	 */
	@Override
	public List<IMappingRule> getRulesContainStereoType(String stereoTypeName) {
		EntityMapping mapping = (EntityMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (EntityMappingRule rule : mapping.getEntityMappingRules()) {
			EntityTargetElement targetElement = rule.getTarget();
			addRuleIfContainsStereoTypeName(stereoTypeName, mappingRules, rule, targetElement);
		}
		return mappingRules;
	}

	private void addRuleIfContainsStereoTypeName(String stereoTypeName, List<IMappingRule> mappingRules,
			EntityMappingRule rule, EntityTargetElement targetElement) {
		if (targetElement instanceof StereoTypeReference) {
			StereoTypeReference reference = (StereoTypeReference) targetElement;
			StereoTypeElement stereoTypeElement = reference.getTargetElement();
			if (StereoTypeHelper.containsStereoType(stereoTypeElement.getStereoTypes(), stereoTypeName)) {
				mappingRules.add(new EntityMappingRuleWrapper(rule));
			}

		}
	}
}
