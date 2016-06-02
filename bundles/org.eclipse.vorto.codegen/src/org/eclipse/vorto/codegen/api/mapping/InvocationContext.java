/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.api.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationSource;
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.FaultSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.InfomodelSource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.OperationSource;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.mapping.StatusSource;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.api.model.model.Model;

public class InvocationContext {

	private List<MappingRule> mappingRules;

	public InvocationContext(List<MappingModel> mappingModels) {
		this.mappingRules = new ArrayList<MappingRule>();
		for (MappingModel mappingModel : mappingModels) {
			this.mappingRules.addAll(mappingModel.getRules());
		}
	}

	public static InvocationContext simpleInvocationContext() {
		return new InvocationContext(Collections.emptyList());
	}

	public IMapped<InformationModel> getMappedElement(final InformationModel informationModel,
			final String stereoType) {
		for (MappingRule rule : mappingRules) {
			for (Source ruleSource : rule.getSources()) {
				if (ruleSource instanceof InfomodelSource
						&& EcoreUtil.equals(((InfomodelSource) ruleSource).getModel(), informationModel)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<InformationModel>(informationModel, (StereoTypeTarget) rule.getTarget());
				}
			}
		}
		return new NullMapped<InformationModel>(informationModel);
	}

	public IMapped<FunctionblockModel> getMappedElement(final FunctionblockModel functionblockModel,
			final String stereoType) {
		for (MappingRule rule : mappingRules) {
			for (Source ruleSource : rule.getSources()) {
				if (ruleSource instanceof FunctionBlockSource
						&& EcoreUtil.equals(((InfomodelSource) ruleSource).getModel(), functionblockModel)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<FunctionblockModel>(functionblockModel,
							(StereoTypeTarget) rule.getTarget());
				}
			}
		}
		return new NullMapped<FunctionblockModel>(functionblockModel);
	}

	private boolean matchesStereoType(final String stereoType, final StereoTypeTarget stereoTypeTarget) {
		return stereoTypeTarget.getName().equalsIgnoreCase(stereoType);
	}

	public IMapped<Property> getMappedElement(final Property property, final String stereoType) {

		for (MappingRule rule : mappingRules) {
			for (Source ruleSource : rule.getSources()) {
				if (ruleSource instanceof ConfigurationSource
						&& EcoreUtil.equals(((ConfigurationSource) ruleSource).getProperty(), property)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<Property>(property, (StereoTypeTarget) rule.getTarget());
				} else if (ruleSource instanceof StatusSource
						&& EcoreUtil.equals(((StatusSource) ruleSource).getProperty(), property)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<Property>(property, (StereoTypeTarget) rule.getTarget());
				} else if (ruleSource instanceof FaultSource
						&& EcoreUtil.equals(((FaultSource) ruleSource).getProperty(), property)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<Property>(property, (StereoTypeTarget) rule.getTarget());
				}
			}
		}

		return new NullMapped<Property>(property);
	}

	public IMapped<Operation> getMappedElement(final Operation operation, final String stereoType) {
		for (MappingRule rule : mappingRules) {
			for (Source ruleSource : rule.getSources()) {
				if (ruleSource instanceof OperationSource
						&& EcoreUtil.equals(((OperationSource) ruleSource).getOperation(), operation)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<Operation>(operation, (StereoTypeTarget) rule.getTarget());
				}
			}
		}

		return new NullMapped<Operation>(operation);
	}

	public IMapped<ModelAttribute> getMappedModelAttribute(final Model model, final ModelAttribute attribute,
			final String stereoType) {
		for (MappingRule rule : mappingRules) {
			for (Source ruleSource : rule.getSources()) {
				if (model instanceof InformationModel && ruleSource instanceof InfoModelAttributeSource
						&& ((InfoModelAttributeSource) ruleSource).getAttribute().equals(attribute)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<ModelAttribute>(attribute, (StereoTypeTarget) rule.getTarget());
				} else if (model instanceof FunctionblockModel && ruleSource instanceof FunctionBlockAttributeSource
						&& ((FunctionBlockAttributeSource) ruleSource).getAttribute().equals(attribute)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<ModelAttribute>(attribute, (StereoTypeTarget) rule.getTarget());
				} else if (model instanceof Enum && ruleSource instanceof EnumAttributeSource
						&& ((EnumAttributeSource) ruleSource).getAttribute().equals(attribute)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<ModelAttribute>(attribute, (StereoTypeTarget) rule.getTarget());
				} else if (model instanceof Entity && ruleSource instanceof EnumAttributeSource
						&& ((EntityAttributeSource) ruleSource).getAttribute().equals(attribute)
						&& matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
					return new DefaultMapped<ModelAttribute>(attribute, (StereoTypeTarget) rule.getTarget());
				}
			}
		}

		return new NullMapped<ModelAttribute>(attribute);
	}
}
