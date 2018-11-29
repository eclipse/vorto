/*******************************************************************************
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.ditto.schema.tasks;

import org.eclipse.vorto.codegen.ditto.schema.tasks.template.ConstraintTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.EntityValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.EnumValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.EventValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.OperationPayloadValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.OperationResponseValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.OperationSingleParameterValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.PrimitiveTypeValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.PropertiesConfigurationValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.PropertiesFaultValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.PropertiesSinglePropertyValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.PropertiesStatusValidationTemplate;
import org.eclipse.vorto.codegen.ditto.schema.tasks.template.PropertiesValidationTemplate;

final class ValidationTemplates {
  public static EnumValidationTemplate enumValidationTemplate = new EnumValidationTemplate();

  public static PrimitiveTypeValidationTemplate primitiveTypeValidationTemplate =
      new PrimitiveTypeValidationTemplate();

  public static ConstraintTemplate constraintTemplate = new ConstraintTemplate();

  public static EntityValidationTemplate entityValidationTemplate = new EntityValidationTemplate();

  public static PropertiesValidationTemplate propertiesValidationTemplate =
      new PropertiesValidationTemplate();
  public static PropertiesConfigurationValidationTemplate propertiesConfigValidationTemplate =
      new PropertiesConfigurationValidationTemplate();
  public static PropertiesStatusValidationTemplate propertiesStatusValidationTemplate =
      new PropertiesStatusValidationTemplate();
  public static PropertiesFaultValidationTemplate propertiesFaultValidationTemplate =
      new PropertiesFaultValidationTemplate();
  public static PropertiesSinglePropertyValidationTemplate propertiesSinglePropertyValidationTemplate =
      new PropertiesSinglePropertyValidationTemplate();

  public static EventValidationTemplate eventValidationTemplate = new EventValidationTemplate();

  public static OperationSingleParameterValidationTemplate operationSingleParameterValidationTemplate =
      new OperationSingleParameterValidationTemplate();

  public static OperationPayloadValidationTemplate operationPayloadValidationTemplate =
      new OperationPayloadValidationTemplate();

  public static OperationResponseValidationTemplate operationResponseValidationTemplate =
      new OperationResponseValidationTemplate();

  private ValidationTemplates() {
    throw new AssertionError();
  }
}
