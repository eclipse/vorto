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
import org.eclipse.vorto.codegen.internal.mapping.functionblock.FunctionBlockPropertyRulesFilter;
import org.eclipse.vorto.codegen.internal.mapping.functionblock.FunctionBlockRuleFilterFactory;
import org.eclipse.vorto.codegen.internal.mapping.functionblock.FunctionBlockRulesFilter;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockTargetElement;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeElement;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeReference;

public class FunctionBlockMappingRules implements IMappingRules {
	private MappingModel mappingModel;

	public FunctionBlockMappingRules(MappingModel mappingModel) {
		this.mappingModel = mappingModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.codegen.api.mapping.IMappingRules#getRules(org.eclipse.
	 * emf.ecore.EObject)
	 */
	@Override
	public List<IMappingRule> getRules(EObject modelElement) {

		FunctionBlockMapping mapping = (FunctionBlockMapping) mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (FunctionBlockMappingRule rule : mapping.getFunctionBlockMappingRules()) {
			for (FunctionBlockSourceElement sourceElement : rule.getFunctionBlockSourceElements()) {
				addRuleIfContainsEObject(modelElement, mappingRules, rule, sourceElement);
			}
		}


		return mappingRules;
	}

	/**
	 * @param modelElement
	 * @param mappingRules
	 * @param rule
	 * @param sourceElement
	 */
	private void addRuleIfContainsEObject(EObject modelElement, List<IMappingRule> mappingRules,
			FunctionBlockMappingRule rule, FunctionBlockSourceElement sourceElement) {
		FunctionBlockRulesFilter functionBlockRulesFilter = FunctionBlockRuleFilterFactory.getFunctionBlockRulesFilter(sourceElement.getFunctionBlockElement(), modelElement);
		if(functionBlockRulesFilter!=null && functionBlockRulesFilter.isRuleContainsContainsModelElement(rule, sourceElement, modelElement)){
			mappingRules.add(new FunctionBlockMappingRuleWrapper(rule));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.codegen.api.mapping.IMappingRules#getRules(java.lang.
	 * String)
	 */
	@Override
	public List<IMappingRule> getRules(MappingAttribute mappingAttribute) {
		FunctionBlockMapping mapping = (FunctionBlockMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (FunctionBlockMappingRule rule : mapping.getFunctionBlockMappingRules()) {
			for (FunctionBlockSourceElement sourceElement : rule.getFunctionBlockSourceElements()) {
				addRuleIfContainsAttribute(mappingAttribute, mappingRules, rule, sourceElement);
			}
		}
		return mappingRules;
	}

	private void addRuleIfContainsAttribute(MappingAttribute mappingAttribute, List<IMappingRule> mappingRules,
			FunctionBlockMappingRule rule, FunctionBlockSourceElement sourceElement) {

		FunctionBlockElement functionBlockElement = sourceElement.getFunctionBlockElement();
		if (functionBlockElement instanceof FunctionBlockElementAttribute) {
			FunctionBlockElementAttribute attributeElement = (FunctionBlockElementAttribute) functionBlockElement;
			if (StringUtils.equals(attributeElement.getAttribute().toString(), mappingAttribute.name())) {
				mappingRules.add(new FunctionBlockMappingRuleWrapper(rule));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRules#
	 * getRulesContainStereoType(java.lang.String)
	 */
	@Override
	public List<IMappingRule> getRulesContainStereoType(String stereoTypeName) {
		FunctionBlockMapping mapping = (FunctionBlockMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (FunctionBlockMappingRule rule : mapping.getFunctionBlockMappingRules()) {
			FunctionBlockTargetElement targetElement = rule.getTarget();
			addRuleIfContainsStereoTypeName(stereoTypeName, mappingRules, rule, targetElement);
		}
		return mappingRules;
	}

	private void addRuleIfContainsStereoTypeName(String stereoTypeName, List<IMappingRule> mappingRules,
			FunctionBlockMappingRule rule, FunctionBlockTargetElement targetElement) {
		if (targetElement instanceof StereoTypeReference) {
			StereoTypeReference reference = (StereoTypeReference) targetElement;
			StereoTypeElement stereoTypeElement = reference.getTargetElement();
			if (StereoTypeHelper.containsStereoType(stereoTypeElement.getStereoTypes(), stereoTypeName)) {
				mappingRules.add(new FunctionBlockMappingRuleWrapper(rule));
			}

		}
	}
}
