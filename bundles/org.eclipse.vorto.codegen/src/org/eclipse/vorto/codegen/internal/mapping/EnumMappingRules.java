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
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.mapping.EnumAttributeElement;
import org.eclipse.vorto.core.api.model.mapping.EnumExpression;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EnumSourceElement;
import org.eclipse.vorto.core.api.model.mapping.EnumSourceElement;
import org.eclipse.vorto.core.api.model.mapping.EnumSourceElement;
import org.eclipse.vorto.core.api.model.mapping.EnumTargetElement;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeElement;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeReference;


public class EnumMappingRules implements IMappingRules {
	private MappingModel mappingModel;

	public EnumMappingRules(MappingModel mappingModel) {
		this.mappingModel = mappingModel;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRules#getRules(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public List<IMappingRule> getRules(EObject modelElement) {
		EnumMapping mapping = (EnumMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (EnumMappingRule rule : mapping.getEnumMappingRules()) {
			for (EnumSourceElement sourceElement : rule.getEnumSourceElement()) {
				this.addRuleIfContainsModelElement(modelElement, mappingRules, rule, sourceElement);
			}
		}

		return mappingRules;
	}

	private void addRuleIfContainsModelElement(EObject modelElement, List<IMappingRule> mappingRules,
			EnumMappingRule rule, EnumSourceElement sourceElement) {

		if (sourceElement instanceof EnumExpression) {
			EnumLiteral enumLiteral = ((EnumExpression) sourceElement).getLiteral();
			Enum enumType = sourceElement.getTypeRef();
			if (matchesFunctionBlockModel(enumType,enumLiteral, modelElement)) {
				mappingRules.add(new EnumMappingRuleWrapper(rule));
			}
		}
	}
	/**
	 * @param enumType
	 * @param enumLiteral
	 * @param modelElement
	 * @return
	 */
	private boolean matchesFunctionBlockModel(Enum enumType, EnumLiteral enumLiteral, EObject modelElement) {
		if (!(modelElement instanceof EnumLiteral)) {
			return false;
		}

		EnumLiteral elementEnumLiteral = ((EnumLiteral) modelElement);
		Enum elementEnum = (Enum)elementEnumLiteral.eContainer();
		return StringUtils.equals(elementEnumLiteral.getName(), enumLiteral.getName())
				&& StringUtils.equals(elementEnum.getNamespace(), enumType.getNamespace())
				&& StringUtils.equals(elementEnum.getVersion(), enumType.getVersion());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRules#getRules(java.lang.String)
	 */
	@Override
	public List<IMappingRule> getRules(MappingAttribute mappingAttribute) {
		EnumMapping mapping = (EnumMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (EnumMappingRule rule : mapping.getEnumMappingRules()) {
			for (EnumSourceElement sourceElement : rule.getEnumSourceElement()) {
				addRuleIfContainsAttribute(mappingAttribute, mappingRules, rule, sourceElement);
			}
		}
		return mappingRules;
	}

	private void addRuleIfContainsAttribute(MappingAttribute mappingAttribute, List<IMappingRule> mappingRules,
			EnumMappingRule rule, EnumSourceElement sourceElement) {

		if (sourceElement instanceof EnumAttributeElement) {
			EnumAttributeElement attributeElement = (EnumAttributeElement) sourceElement;
			if (StringUtils.equals(attributeElement.getAttribute().toString(), mappingAttribute.name())) {
				mappingRules.add(new EnumMappingRuleWrapper(rule));
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRules#getRulesContainStereoType(java.lang.String)
	 */
	@Override
	public List<IMappingRule> getRulesContainStereoType(String stereoTypeName) {
		EnumMapping mapping = (EnumMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (EnumMappingRule rule : mapping.getEnumMappingRules()) {
			EnumTargetElement targetElement = rule.getTarget();
			addRuleIfContainsStereoTypeName(stereoTypeName, mappingRules, rule, targetElement);
		}
		return mappingRules;
	}

	private void addRuleIfContainsStereoTypeName(String stereoTypeName, List<IMappingRule> mappingRules,
			EnumMappingRule rule, EnumTargetElement targetElement) {
		if (targetElement instanceof StereoTypeReference) {
			StereoTypeReference reference = (StereoTypeReference) targetElement;
			StereoTypeElement stereoTypeElement = reference.getTargetElement();
			if (this.containsStereoType(stereoTypeElement, stereoTypeName)) {
				mappingRules.add(new EnumMappingRuleWrapper(rule));
			}

		}
	}

	private boolean containsStereoType(StereoTypeElement stereoTypeElement, String stereoTypeName) {
		for (StereoType stereoType : stereoTypeElement.getStereoTypes()) {
			if (StringUtils.endsWith(stereoType.getName(), stereoTypeName)) {
				return true;
			}
		}
		return false;
	}
}
