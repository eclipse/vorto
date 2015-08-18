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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.codegen.api.mapping.IMappingRule;
import org.eclipse.vorto.codegen.api.mapping.IMappingRules;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationElement;
import org.eclipse.vorto.core.api.model.mapping.EventElement;
import org.eclipse.vorto.core.api.model.mapping.FaultElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement;
import org.eclipse.vorto.core.api.model.mapping.InfoModelChild;
import org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.OperationElement;
import org.eclipse.vorto.core.api.model.mapping.StatusElement;
import org.eclipse.vorto.core.api.model.mapping.StereoType;

public class DefaultMappingRules implements IMappingRules {

	private MappingModel mappingModel;

	public DefaultMappingRules(MappingModel mappingModel) {
		this.mappingModel = mappingModel;
	}

	@Override
	public List<IMappingRule> getRules(EObject currentModelElement) {
		List<IMappingRule> mappingRules = new ArrayList<IMappingRule>();
		for (InfoModelMappingRule rule : mappingModel.getInfoModelMappingRules()) {
			if (currentModelElement instanceof Event) {
				eventCompare(currentModelElement, mappingRules, rule);
			}
			if (currentModelElement instanceof Operation) {
				operationCompare(currentModelElement, mappingRules, rule);
			}
			if (currentModelElement instanceof Property) {
				propertyCompare(currentModelElement, mappingRules, rule);
			}
		}
		return mappingRules;
	}

	private void propertyCompare(EObject currentModelElement,
			List<IMappingRule> mappingRules, InfoModelMappingRule rule) {
		for (InfoModelSourceElement element : rule
				.getInfoModelSourceElements()) {

			FunctionBlockElement fbElement = getFunctionBlockElement(element);
			if (fbElement == null) {
				continue;
			}
			String eObjectName = ((Property) currentModelElement).getName();
			if (fbElement instanceof ConfigurationElement) {
				String nameInRule = ((ConfigurationElement) fbElement)
						.getTypeRef().getProperty().getName();
				addRuleIfSenseful(mappingRules, rule, eObjectName, nameInRule);
			} else if (fbElement instanceof FaultElement) {
				String nameInRule = ((FaultElement) fbElement).getTypeRef().getProperty().getName();
				addRuleIfSenseful(mappingRules, rule, eObjectName, nameInRule);
			} else if (fbElement instanceof StatusElement) {
				String nameInRule = ((StatusElement) fbElement).getTypeRef()
						.getProperty().getName();
				addRuleIfSenseful(mappingRules, rule, eObjectName, nameInRule);
			}
		}
	}

	private void addRuleIfSenseful(List<IMappingRule> mappingRules, InfoModelMappingRule rule,
			String eObjectName, String nameInRule) {
		boolean elementsHaveSameName = nameInRule.equals(eObjectName);
		if (elementsHaveSameName) {
			mappingRules.add(new DefaultMappingRule(rule));
		}
	}

	private void operationCompare(EObject currentModelElement,
			List<IMappingRule> mappingRules, InfoModelMappingRule rule) {
		for (InfoModelSourceElement element : rule
				.getInfoModelSourceElements()) {
			FunctionBlockElement fbElement = getFunctionBlockElement(element);
			if (fbElement == null) {
				continue;
			}

			if (fbElement instanceof OperationElement) {
				String nameInRule = ((OperationElement) fbElement)
						.getOperation().getName();
				String eObjectName = ((Operation) currentModelElement)
						.getName();
				addRuleIfSenseful(mappingRules, rule, eObjectName, nameInRule);
			}
		}
	}

	private FunctionBlockElement getFunctionBlockElement(
			InfoModelSourceElement element) {
		InfoModelChild informationModelChild = element
				.getInfoModelChild();
		if (!(informationModelChild instanceof InfoModelFbElement)) {
			return null;
		}
		InfoModelFbElement functionBlockModelElement = (InfoModelFbElement) informationModelChild;
		return functionBlockModelElement.getFunctionBlockElement();
	}

	private void eventCompare(EObject currentModelElement,
			List<IMappingRule> mappingRules, InfoModelMappingRule rule) {
		for (InfoModelSourceElement element : rule
				.getInfoModelSourceElements()) {
			FunctionBlockElement fbElement = getFunctionBlockElement(element);
			if (fbElement == null) {
				continue;
			}
			if (fbElement instanceof EventElement) {
				String nameInRule = ((EventElement) fbElement).getEvent()
						.getName();
				String eObjectName = ((Event) currentModelElement).getName();
				addRuleIfSenseful(mappingRules, rule, eObjectName, nameInRule);
			}
		}
	}

	@Override
	public List<IMappingRule> getRulesContainStereoType(String stereoTypeName) {
		List<IMappingRule> mappingRules = new ArrayList<>();
		/*for (InfoModelMappingRule rule : this.mappingModel.getInfoModelMappingRules()) {
			for (StereoType stereoType : rule.getTargetElement()
					.getStereoTypes()) {
				if (stereoType.getName().equals(stereoTypeName)) {
					mappingRules.add(new DefaultMappingRule(rule));
					break;
				}
			}
		}*/
		return mappingRules;
	}
}
