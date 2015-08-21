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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.codegen.api.mapping.IMappingRule;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;

/**
 * @author sgp0247
 *
 */
public class FunctionBlockOperationRulesFilter {
	public List<IMappingRule> getRules(MappingModel mappingModel, EObject modelElement) {
		FunctionBlockMapping mapping = (FunctionBlockMapping) mappingModel.getMapping();
		List<IMappingRule> mappingRules = new ArrayList<>();
		for (FunctionBlockMappingRule rule : mapping.getFunctionBlockMappingRules()) {
			for (FunctionBlockSourceElement sourceElement : rule.getFunctionBlockSourceElements()) {
				this.addRuleIfContainsFbOperationElement(modelElement, mappingRules, rule, sourceElement);
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
	private void addRuleIfContainsFbOperationElement(EObject modelElement, List<IMappingRule> mappingRules,
			FunctionBlockMappingRule rule, FunctionBlockSourceElement sourceElement) {
		FunctionBlockElement functionBlockElement = sourceElement.getFunctionBlockElement();
		FunctionblockModel functionBlockModel = sourceElement.getFunctionblock();
	}
}
