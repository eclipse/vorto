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
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.mapping.InfoModelChild;
import org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement;
import org.eclipse.vorto.core.api.model.mapping.InfoModelTargetElement;
import org.eclipse.vorto.core.api.model.mapping.InformationModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeElement;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeReference;

public class InfoModelMappingRules implements IMappingRules {
	private MappingModel mappingModel;

	public InfoModelMappingRules(MappingModel mappingModel) {
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
		InfoModelMapping mapping = (InfoModelMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (InfoModelMappingRule rule : mapping.getInfoModelMappingRules()) {
			for (InfoModelSourceElement sourceElement : rule.getInfoModelSourceElements()) {
				this.addRuleIfContainsModelElement(modelElement, mappingRules, rule, sourceElement);
			}
		}

		return mappingRules;
	}

	private void addRuleIfContainsModelElement(EObject modelElement, List<IMappingRule> mappingRules,
			InfoModelMappingRule rule, InfoModelSourceElement sourceElement) {
		InfoModelChild infoModelChild = sourceElement.getInfoModelChild();
		if (infoModelChild instanceof InfoModelFbElement) {
			InfoModelFbElement fbElement = (InfoModelFbElement) infoModelChild;
			FunctionblockModel functionblockModel = fbElement.getFunctionblock().getType();
			if (matchesFunctionBlockModel(functionblockModel, modelElement)) {
				mappingRules.add(new InfoModelMappingRuleWrapper(rule));
			}
		}
	}

	private boolean matchesFunctionBlockModel(FunctionblockModel functionblockModel, EObject modelElement) {
		if (!(modelElement instanceof FunctionblockProperty)) {
			return false;
		}

		FunctionblockModel elementModel = ((FunctionblockProperty) modelElement).getType();
		return StringUtils.equals(elementModel.getName(), functionblockModel.getName())
				&& StringUtils.equals(elementModel.getNamespace(), functionblockModel.getNamespace())
				&& StringUtils.equals(elementModel.getVersion(), functionblockModel.getVersion());

	}

	@Override
	public List<IMappingRule> getRulesContainStereoType(String stereoTypeName) {
		InfoModelMapping mapping = (InfoModelMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (InfoModelMappingRule rule : mapping.getInfoModelMappingRules()) {
			InfoModelTargetElement targetElement = rule.getTarget();
			addRuleIfContainsStereoTypeName(stereoTypeName, mappingRules, rule, targetElement);
		}
		return mappingRules;
	}

	private void addRuleIfContainsStereoTypeName(String stereoTypeName, List<IMappingRule> mappingRules,
			InfoModelMappingRule rule, InfoModelTargetElement targetElement) {
		if (targetElement instanceof StereoTypeReference) {
			StereoTypeReference reference = (StereoTypeReference) targetElement;
			StereoTypeElement stereoTypeElement = reference.getTargetElement();
			if (this.containsStereoType(stereoTypeElement, stereoTypeName)) {
				mappingRules.add(new InfoModelMappingRuleWrapper(rule));
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

	@Override
	public List<IMappingRule> getRules(MappingAttribute mappingAttribute) {
		InfoModelMapping mapping = (InfoModelMapping) this.mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (InfoModelMappingRule rule : mapping.getInfoModelMappingRules()) {
			for (InfoModelSourceElement sourceElement : rule.getInfoModelSourceElements()) {
				addRuleIfContainsAttribute(mappingAttribute, mappingRules, rule, sourceElement);
			}
		}
		return mappingRules;
	}

	private void addRuleIfContainsAttribute(MappingAttribute mappingAttribute, List<IMappingRule> mappingRules,
			InfoModelMappingRule rule, InfoModelSourceElement sourceElement) {
		InfoModelChild infoModelChild = sourceElement.getInfoModelChild();
		if (infoModelChild instanceof InformationModelAttribute) {
			InformationModelAttribute attribute = (InformationModelAttribute) infoModelChild;
			if (StringUtils.equals(attribute.getAttribute().toString(), mappingAttribute.name())) {
				mappingRules.add(new InfoModelMappingRuleWrapper(rule));
			}
		}
	}
}
