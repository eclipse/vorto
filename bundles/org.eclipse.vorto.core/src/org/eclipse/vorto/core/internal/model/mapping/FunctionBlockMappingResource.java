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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationSource;
import org.eclipse.vorto.core.api.model.mapping.EventSource;
import org.eclipse.vorto.core.api.model.mapping.FaultSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.OperationSource;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.mapping.StatusSource;
import org.eclipse.vorto.core.model.MappingAttribute;


public class FunctionBlockMappingResource extends AbstractMappingResource {

	public FunctionBlockMappingResource(MappingModel mappingModel) {
		super(mappingModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.codegen.api.mapping.IMappingRules#getRules(org.eclipse.
	 * emf.ecore.EObject)
	 */
	@Override
	protected void addRuleIfContainsModelObject(EObject modelObject, List<MappingRule> mappingRules, MappingRule rule,
			Source source) {

		FunctionblockModel functionblockModel = (FunctionblockModel) ((FunctionBlockSource)source).getModel();
		if (source instanceof ConfigurationSource) {
			addRuleIfMatchesProperty(modelObject, mappingRules, rule, functionblockModel,
					((ConfigurationSource) source).getProperty());
		} else if (source instanceof StatusSource) {
			addRuleIfMatchesProperty(modelObject, mappingRules, rule, functionblockModel,
					((StatusSource) source).getProperty());
		} else if (source instanceof FaultSource) {
			addRuleIfMatchesProperty(modelObject, mappingRules, rule, functionblockModel,
					((FaultSource) source).getProperty());
		} else if (source instanceof OperationSource) {
			addRuleIfMatchesOperation(modelObject, mappingRules, rule, functionblockModel,
					((OperationSource) source).getOperation());
		} else if (source instanceof EventSource) {

		}

	}

	private void addRuleIfMatchesOperation(EObject modelObject, List<MappingRule> mappingRules, MappingRule rule,
			FunctionblockModel functionblockModel, Operation operation) {
		if (this.matchesOperation(functionblockModel, operation, modelObject)) {
			mappingRules.add(rule);
		}
	}

	private boolean matchesOperation(FunctionblockModel functionblockModel, Operation operation, EObject modelObject) {
		if (!(modelObject instanceof Operation)) {
			return false;
		}
		Operation modelObjectOperation = ((Operation) modelObject);
		FunctionblockModel modelObjectPropertyFunctionBlock = (FunctionblockModel) modelObjectOperation.eContainer()
				.eContainer();
		return StringUtils.equals(modelObjectOperation.getName(), operation.getName())
				&& StringUtils.equals(modelObjectPropertyFunctionBlock.getNamespace(),
						functionblockModel.getNamespace())
				&& StringUtils.equals(modelObjectPropertyFunctionBlock.getVersion(), functionblockModel.getVersion());
	}

	private void addRuleIfMatchesProperty(EObject modelObject, List<MappingRule> mappingRules, MappingRule rule,
			FunctionblockModel functionblockModel, Property property) {
		if (this.matchesFunctionBlockProperty(functionblockModel, property, modelObject)) {
			mappingRules.add(rule);
		}
	}

	protected boolean matchesFunctionBlockProperty(FunctionblockModel functionBlockModel,
			Property functionBlockProperty, EObject modelObject) {
		if (!(modelObject instanceof Property)) {
			return false;
		}
		Property modelObjectProperty = ((Property) modelObject);
		FunctionblockModel modelObjectPropertyFunctionBlock = (FunctionblockModel) modelObjectProperty.eContainer()
				.eContainer().eContainer();
		return StringUtils.equals(modelObjectProperty.getName(), functionBlockProperty.getName())
				&& StringUtils.equals(modelObjectPropertyFunctionBlock.getNamespace(),
						functionBlockModel.getNamespace())
				&& StringUtils.equals(modelObjectPropertyFunctionBlock.getVersion(), functionBlockModel.getVersion());
	}

	@Override
	protected void addRuleIfContainsAttribute(MappingAttribute mappingAttribute, List<MappingRule> mappingRules,
			MappingRule rule, Source source) {

		if (source instanceof FunctionBlockAttributeSource) {
			FunctionblockModelAttribute attribute = ((FunctionBlockAttributeSource) source).getAttribute();
			if (StringUtils.equals(attribute.toString(), mappingAttribute.name())) {
				mappingRules.add(rule);
			}
		}
	}
}
