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
package org.eclipse.vorto.codegen.examples.bosch.things.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.ConstraintTemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.EntityValidationTemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.EnumValidationTemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.OperationParametersValidationTemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.OperationSingleParameterValidationTemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.PrimitiveTypeValidationTemplate;
import org.eclipse.vorto.core.api.model.functionblock.Operation;

public class OperationParametersValidationTask extends AbstractTemplateGeneratorTask<Operation> {
	
	private String jsonSchmaFileExtension;
	private String targetPath;
	
	public OperationParametersValidationTask(String jsonSchmaFileExtension, String targetPath) {
		super();
		this.jsonSchmaFileExtension = jsonSchmaFileExtension;
		this.targetPath = targetPath;
	}
	
	@Override
	public String getFileName(Operation operation) {
		return operation.getName() + jsonSchmaFileExtension;
	}

	@Override
	public String getPath(Operation operation) {
		return targetPath;
	}

	@Override
	public ITemplate<Operation> getTemplate() {
		OperationSingleParameterValidationTemplate operationSingleParameterValidation = new OperationSingleParameterValidationTemplate(
				new EntityValidationTemplate(new EnumValidationTemplate(), new PrimitiveTypeValidationTemplate(),
						new ConstraintTemplate()),
				new EnumValidationTemplate(), new PrimitiveTypeValidationTemplate(), new ConstraintTemplate());
		return new OperationParametersValidationTemplate(operationSingleParameterValidation);
	}

}
