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
