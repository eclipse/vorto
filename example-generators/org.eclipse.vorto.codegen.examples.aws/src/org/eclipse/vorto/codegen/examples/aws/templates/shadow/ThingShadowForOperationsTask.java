/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.examples.aws.templates.shadow;

import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.mapping.IMapped;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd.)
 * 
 * Generates AWS Lambda functions to update the thing shadow for writable operations
 *
 */
public class ThingShadowForOperationsTask implements ICodeGeneratorTask<InformationModel> {

	private static final String STEREOTYPE_AWS_THINGSHADOW = "thingshadow";
	
	@Override
	public void generate(InformationModel model, InvocationContext context, IGeneratedWriter writer) {
		for (FunctionblockProperty fbProperty : model.getProperties()) {
			for (Operation operation : fbProperty.getType().getFunctionblock().getOperations()) {
				IMapped<Operation> mappedElement = context.getMappedElement(operation,STEREOTYPE_AWS_THINGSHADOW);
				if (isUpdateableOperation(mappedElement)) {
					ICodeGeneratorTask<Operation> generator = new GeneratorTaskFromFileTemplate<>(new UpdateThingShadowLambdaTemplate(mappedElement));
					generator.generate(operation,context,writer);
				}
				else if (!operation.getParams().isEmpty() && !operation.getName().startsWith("get")) {
					ICodeGeneratorTask<Operation> generator = new GeneratorTaskFromFileTemplate<>(new UpdateThingShadowLambdaTemplate(mappedElement));
					generator.generate(operation,context,writer);
				}
			}
			
			if (fbProperty.getType().getFunctionblock().getStatus() != null) {
				for (Property statusProperty : fbProperty.getType().getFunctionblock().getStatus().getProperties()) {
					ICodeGeneratorTask<Property> generator = new GeneratorTaskFromFileTemplate<>(new GetThingShadowLambdaTemplate());
					generator.generate(statusProperty,context,writer);
				}
			}
		}
		
	}
	
	private boolean isUpdateableOperation(IMapped<Operation> mapped) {
		return Boolean.getBoolean(mapped.getAttributeValue("updatable", "false"));
	}
}
