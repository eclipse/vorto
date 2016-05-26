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
package org.eclipse.vorto.codegen.examples.aws.templates.shadow;

import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IMappingContext;
import org.eclipse.vorto.codegen.api.utils.MappingRuleUtils;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;

/**
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd.)
 * 
 * Generates AWS Lambda functions to update the thing shadow for writable operations
 *
 */
public class ThingShadowForOperationsTask implements ICodeGeneratorTask<InformationModel> {

	private static final String STEREOTYPE_AWS_THINGSHADOW = "thingshadow";
	
	@Override
	public void generate(InformationModel model, IMappingContext mappingContext, IGeneratedWriter writer) {
		for (FunctionblockProperty fbProperty : model.getProperties()) {
			for (Operation operation : fbProperty.getType().getFunctionblock().getOperations()) {
				if (hasMappingRuleForOperation(operation, mappingContext)) {
					MappingRule mappingRule = mappingContext.getMappingRuleByOperationAndStereoType(operation,STEREOTYPE_AWS_THINGSHADOW);
					if (isUpdateableOperation(mappingRule)) {
						ICodeGeneratorTask<Operation> generator = new GeneratorTaskFromFileTemplate<>(new UpdateThingShadowLambdaTemplate(mappingRule.getTarget()));
						generator.generate(operation,mappingContext,writer);
					}
				} else if (!operation.getParams().isEmpty() && !operation.getName().startsWith("get")) {
					ICodeGeneratorTask<Operation> generator = new GeneratorTaskFromFileTemplate<>(new UpdateThingShadowLambdaTemplate(null));
					generator.generate(operation,mappingContext,writer);
				}
			}
			
			if (fbProperty.getType().getFunctionblock().getStatus() != null) {
				for (Property statusProperty : fbProperty.getType().getFunctionblock().getStatus().getProperties()) {
					ICodeGeneratorTask<Property> generator = new GeneratorTaskFromFileTemplate<>(new GetThingShadowLambdaTemplate());
					generator.generate(statusProperty,mappingContext,writer);
				}
			}
		}
		
	}
	
	private boolean hasMappingRuleForOperation(Operation operation, IMappingContext mappingContext) {
		return mappingContext.getMappingRuleByOperationAndStereoType(operation,STEREOTYPE_AWS_THINGSHADOW) != null;
	}

	private boolean isUpdateableOperation(MappingRule rule) {
		if (rule.getTarget() instanceof StereoTypeTarget) {
			return MappingRuleUtils.getAttributeBoolValue((StereoTypeTarget)rule.getTarget(),"updatable",false);
		}
		return false;
	}
}
