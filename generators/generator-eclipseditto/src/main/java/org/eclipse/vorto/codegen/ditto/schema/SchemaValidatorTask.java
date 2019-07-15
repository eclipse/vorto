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
package org.eclipse.vorto.codegen.ditto.schema;

import org.eclipse.vorto.codegen.ditto.schema.tasks.ValidationTaskFactory;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.ChainedCodeGeneratorTask;
import org.eclipse.vorto.plugin.generator.utils.ICodeGeneratorTask;
import org.eclipse.vorto.plugin.generator.utils.IGeneratedWriter;

public final class SchemaValidatorTask implements ICodeGeneratorTask<InformationModel> {

  private static final String JSON_SCHEMA_FILE_EXTENSION = ".schema.json";
  private static final String TARGET_PATH = "features";

  @Override
  public void generate(InformationModel infomodel, InvocationContext invocationContext,
      IGeneratedWriter writer) {

    for (FunctionblockProperty fbp : infomodel.getProperties()) {
      FunctionBlock fb = fbp.getType().getFunctionblock();

      generateForFunctionblock(fb,
          invocationContext, TARGET_PATH + "/" + fbp.getType().getNamespace() + "_"
              + fbp.getType().getName() + "_" + fbp.getType().getVersion(),
          JSON_SCHEMA_FILE_EXTENSION, writer);
    }
  }

  private void generateForFunctionblock(FunctionBlock fb, InvocationContext context,
      String targetPath, String jsonFileExt, IGeneratedWriter outputter) {
    if (fb == null) {
      throw new IllegalArgumentException("fb must not be null null");
    }

    String stateTargetPath = targetPath + "/properties";
    String operationTargetPath = targetPath + "/operations";
    String eventTargetPath = targetPath + "/events";

    Configuration configuration = fb.getConfiguration();
    Status status = fb.getStatus();
    Fault fault = fb.getFault();
    if (configuration != null || status != null || fault != null) {
      generateTask(fb, context, outputter,
          ValidationTaskFactory.getPropertiesValidationTask(jsonFileExt, stateTargetPath));
    }
    generateConfiguration(context, jsonFileExt, outputter, stateTargetPath, configuration);
    generateStatus(context, jsonFileExt, outputter, stateTargetPath, status);
    generateFault(context, jsonFileExt, outputter, stateTargetPath, fault);

    generateEvents(fb, context, jsonFileExt, outputter, eventTargetPath);
    generateOperations(fb, context, jsonFileExt, outputter, operationTargetPath);
  }

  private void generateConfiguration(InvocationContext context, String jsonFileExt,
      IGeneratedWriter outputter, String stateTargetPath, Configuration configuration) {
    if (configuration != null) {
      generateTask(configuration, context, outputter,
          ValidationTaskFactory.getPropertiesConfigValidationTask(jsonFileExt, stateTargetPath));

      for (Property property : configuration.getProperties()) {
        generateTask(property, context, outputter,
            ValidationTaskFactory.getPropertiesSinglePropertyValidationTask(
                "-configuration-" + property.getName() + jsonFileExt, stateTargetPath));
      }
    }
  }

  private void generateStatus(InvocationContext context, String jsonFileExt,
      IGeneratedWriter outputter, String stateTargetPath, Status status) {
    if (status != null) {
      generateTask(status, context, outputter,
          ValidationTaskFactory.getPropertiesStatusValidationTask(jsonFileExt, stateTargetPath));

      for (Property property : status.getProperties()) {
        generateTask(property, context, outputter,
            ValidationTaskFactory.getPropertiesSinglePropertyValidationTask(
                "-status-" + property.getName() + jsonFileExt, stateTargetPath));
      }
    }
  }

  private void generateFault(InvocationContext context, String jsonFileExt,
      IGeneratedWriter outputter, String stateTargetPath, Fault fault) {
    if (fault != null) {
      generateTask(fault, context, outputter,
          ValidationTaskFactory.getPropertiesFaultValidationTask(jsonFileExt, stateTargetPath));

      for (Property property : fault.getProperties()) {
        generateTask(property, context, outputter,
            ValidationTaskFactory.getPropertiesSinglePropertyValidationTask(
                "-fault-" + property.getName() + jsonFileExt, stateTargetPath));
      }
    }
  }

  private void generateEvents(FunctionBlock fb, InvocationContext context, String jsonFileExt,
      IGeneratedWriter outputter, String eventTargetPath) {
    if (fb.getEvents() != null) {
      for (Event event : fb.getEvents()) {
        generateTask(event, context, outputter,
            ValidationTaskFactory.getEventValidationTask(jsonFileExt, eventTargetPath));
      }
    }
  }

  private void generateOperations(FunctionBlock fb, InvocationContext context, String jsonFileExt,
      IGeneratedWriter outputter, String operationTargetPath) {
    if (fb.getOperations() != null) {
      for (Operation op : fb.getOperations()) {
        generateTask(op, context, outputter,
            ValidationTaskFactory.getOperationParametersValidationTask(jsonFileExt,
                operationTargetPath),
            ValidationTaskFactory.getOperationReturnTypeValidationTask(jsonFileExt,
                operationTargetPath));
      }
    }
  }

  @SafeVarargs
  private final <K> void generateTask(K element, InvocationContext context,
      IGeneratedWriter outputter, ICodeGeneratorTask<K>... tasks) {
    ChainedCodeGeneratorTask<K> generator = new ChainedCodeGeneratorTask<K>();
    for (ICodeGeneratorTask<K> task : tasks) {
      generator.addTask(task);
    }
    generator.generate(element, context, outputter);
  }
}
