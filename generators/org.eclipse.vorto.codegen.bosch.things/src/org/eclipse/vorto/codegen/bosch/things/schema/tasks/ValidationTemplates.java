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
package org.eclipse.vorto.codegen.bosch.things.schema.tasks;

import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.ConfigurationValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.ConstraintTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.EntityValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.EnumValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.EventValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.FaultValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.OperationParametersValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.OperationReturnTypeValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.OperationSingleParameterValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.PrimitiveTypeValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.StateValidationTemplate;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.template.StatusValidationTemplate;

public class ValidationTemplates {
	private static EnumValidationTemplate enumValidationTemplate = new EnumValidationTemplate();
	
	private static PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate = new PrimitiveTypeValidationTemplate();
	
	private static ConstraintTemplate constraintTemplate = new ConstraintTemplate();
	
	private static EntityValidationTemplate entityValidationTemplate = 
			new EntityValidationTemplate(enumValidationTemplate, primitiveTypeValidationTemplate, constraintTemplate);
	
	public static StateValidationTemplate stateValidationTemplate = 
			new StateValidationTemplate(
					enumValidationTemplate, entityValidationTemplate, primitiveTypeValidationTemplate, constraintTemplate);
	
	public static ConfigurationValidationTemplate configurationValidationTemplate = 
			new ConfigurationValidationTemplate(
					enumValidationTemplate, entityValidationTemplate, primitiveTypeValidationTemplate, constraintTemplate);
	
	public static StatusValidationTemplate statusValidationTemplate = 
			new StatusValidationTemplate(
					enumValidationTemplate, entityValidationTemplate,  primitiveTypeValidationTemplate, constraintTemplate);
	
	public static FaultValidationTemplate faultValidationTemplate = 
			new FaultValidationTemplate(
					enumValidationTemplate, entityValidationTemplate, primitiveTypeValidationTemplate, constraintTemplate);
	
	public static EventValidationTemplate eventValidationTemplate = 
			new EventValidationTemplate(
					enumValidationTemplate, entityValidationTemplate, primitiveTypeValidationTemplate, constraintTemplate);
	
	public static OperationSingleParameterValidationTemplate operationSingleParameterValidation = 
			new OperationSingleParameterValidationTemplate(
					entityValidationTemplate, enumValidationTemplate, primitiveTypeValidationTemplate, constraintTemplate);
	
	public static OperationParametersValidationTemplate operationParametersValidationTemplate = 
			new OperationParametersValidationTemplate(operationSingleParameterValidation);
	
	public static OperationReturnTypeValidationTemplate operationReturnTypeValidation = 
			new OperationReturnTypeValidationTemplate(
					entityValidationTemplate, enumValidationTemplate, primitiveTypeValidationTemplate);
}
