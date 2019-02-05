/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
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
