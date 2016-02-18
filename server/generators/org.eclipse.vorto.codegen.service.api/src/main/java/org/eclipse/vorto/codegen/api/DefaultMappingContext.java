/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.codegen.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationSource;
import org.eclipse.vorto.core.api.model.mapping.FaultSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.OperationSource;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.mapping.StatusSource;

/**
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class DefaultMappingContext implements IMappingContext {

	private List<MappingModel> allMappingModels = new ArrayList<MappingModel>();
	
	@Override
	public List<MappingRule> getAllRules() {
		List<MappingRule> allRules = new ArrayList<>();
		for (MappingModel mappingModel : allMappingModels) {
			allRules.addAll(mappingModel.getRules());
		}
		return allRules;
	}

	public void addMappingModel(MappingModel mappingModel) {
		this.allMappingModels.add(mappingModel);
	}

	

	@Override
	public List<MappingRule> getMappingRulesByOperation(Operation operation) {
		List<MappingRule> mappingRules = new ArrayList<>();
		
		for (MappingModel mappingModel : allMappingModels) {
			if (mappingModel instanceof FunctionBlockMappingModel) {
				FunctionBlockMappingModel fbMappingModel = (FunctionBlockMappingModel)mappingModel;
				for (MappingRule mappingRule : fbMappingModel.getRules()) {
					for (Source ruleSource : mappingRule.getSources()) {
						if (ruleSource instanceof OperationSource && ((OperationSource)ruleSource).getOperation().equals(operation)) {
							mappingRules.add(mappingRule);
						}
					}
				}
			}
		}
		
		return mappingRules;
	}

	@Override
	public List<MappingRule> getMappingRulesByProperty(Property property) {
		List<MappingRule> mappingRules = new ArrayList<>();
		
		for (MappingModel mappingModel : allMappingModels) {
			if (mappingModel instanceof FunctionBlockMappingModel) {
				FunctionBlockMappingModel fbMappingModel = (FunctionBlockMappingModel)mappingModel;
				for (MappingRule mappingRule : fbMappingModel.getRules()) {
					for (Source ruleSource : mappingRule.getSources()) {
						if (ruleSource instanceof ConfigurationSource && EcoreUtil.equals(((ConfigurationSource)ruleSource).getProperty(),property)) {
							mappingRules.add(mappingRule);
						} else if (ruleSource instanceof StatusSource && EcoreUtil.equals(((StatusSource)ruleSource).getProperty(),property)) {
							mappingRules.add(mappingRule);
						} else if (ruleSource instanceof FaultSource && EcoreUtil.equals(((FaultSource)ruleSource).getProperty(),property)) {
							mappingRules.add(mappingRule);
						}
					}
				}
			}
		}
		return mappingRules;
	}

	@Override
	public List<MappingRule> getRulesByStereoType(String stereoTypeName) {
		List<MappingRule> mappingRules = new ArrayList<>();
		return mappingRules;
	}
}
