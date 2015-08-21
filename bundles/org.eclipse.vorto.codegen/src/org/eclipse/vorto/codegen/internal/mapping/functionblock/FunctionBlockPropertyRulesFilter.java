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
package org.eclipse.vorto.codegen.internal.mapping.functionblock;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.codegen.api.mapping.IMappingRule;
import org.eclipse.vorto.codegen.internal.mapping.FunctionBlockMappingRuleWrapper;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationElement;
import org.eclipse.vorto.core.api.model.mapping.FaultElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.StatusElement;

public class FunctionBlockPropertyRulesFilter {

/*	public List<IMappingRule> getRules(MappingModel mappingModel, EObject modelElement) {
		FunctionBlockMapping mapping = (FunctionBlockMapping) mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (FunctionBlockMappingRule rule : mapping.getFunctionBlockMappingRules()) {
			for (FunctionBlockSourceElement sourceElement : rule.getFunctionBlockSourceElements()) {
				this.addRuleIfContainsFbPropertyElement(modelElement, mappingRules, rule, sourceElement);
			}
		}

		return mappingRules;
	}

	private void addRuleIfContainsFbPropertyElement(EObject modelElement, List<IMappingRule> mappingRules,
			FunctionBlockMappingRule rule, FunctionBlockSourceElement sourceElement) {

		FunctionBlockElement functionBlockElement = sourceElement.getFunctionBlockElement();
		FunctionblockModel functionBlockModel = sourceElement.getFunctionblock();

		if (isSameFunctionBlockProperty(functionBlockElement, modelElement)) {
			Property property = this.getFunctionBlockProperty(functionBlockElement);
			if (matchesFunctionBlockModel(functionBlockModel, property, modelElement)) {
				mappingRules.add(new FunctionBlockMappingRuleWrapper(rule));
			}
		}
	}

	private boolean matchesFunctionBlockModel(FunctionblockModel functionBlockModel, Property functionBlockProperty,
			EObject modelElement) {
		if (!(modelElement instanceof Property)) {
			return false;
		}

		Property modelElementProperty = ((Property) modelElement);
		FunctionblockModel elementFunctionBlock = (FunctionblockModel) modelElementProperty.eContainer().eContainer()
				.eContainer();
		return StringUtils.equals(modelElementProperty.getName(), functionBlockProperty.getName())
				&& StringUtils.equals(elementFunctionBlock.getNamespace(), functionBlockModel.getNamespace())
				&& StringUtils.equals(elementFunctionBlock.getVersion(), functionBlockModel.getVersion());
	}

	private boolean isSameFunctionBlockProperty(FunctionBlockElement functionBlockElement, EObject modelElement) {
		if (!(modelElement instanceof Property)) {
			return false;
		}

		Property modelElementProperty = (Property) modelElement;
		if ((functionBlockElement instanceof ConfigurationElement)
				&& (modelElementProperty.eContainer() instanceof Configuration)) {
			return true;
		}

		if ((functionBlockElement instanceof StatusElement) && (modelElementProperty.eContainer() instanceof Status)) {
			return true;
		}

		if ((functionBlockElement instanceof FaultElement) && (modelElementProperty.eContainer() instanceof Fault)) {
			return true;
		}

		return false;
	}

	private Property getFunctionBlockProperty(FunctionBlockElement functionBlockElement) {
		if ((functionBlockElement instanceof ConfigurationElement)) {
			return ((ConfigurationElement) functionBlockElement).getTypeRef().getProperty();
		}

		if ((functionBlockElement instanceof StatusElement)) {
			return ((StatusElement) functionBlockElement).getTypeRef().getProperty();
		}

		if ((functionBlockElement instanceof FaultElement)) {
			return ((FaultElement) functionBlockElement).getTypeRef().getProperty();
		}

		return null;
	}*/
}
