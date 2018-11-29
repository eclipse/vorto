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

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;

public final class ValidationTaskFactory {

  private ValidationTaskFactory() {
    throw new AssertionError();
  }

  public static ICodeGeneratorTask<FunctionBlock> getPropertiesValidationTask(String fileExt,
      String path) {
    return new ValidationGeneratorTask<FunctionBlock>("properties" + fileExt, path,
        ValidationTemplates.propertiesValidationTemplate);
  }

  public static ICodeGeneratorTask<Configuration> getPropertiesConfigValidationTask(String fileExt,
      String path) {
    return new ValidationGeneratorTask<Configuration>("properties-configuration" + fileExt, path,
        ValidationTemplates.propertiesConfigValidationTemplate);
  }

  public static ICodeGeneratorTask<Status> getPropertiesStatusValidationTask(String fileExt,
      String path) {
    return new ValidationGeneratorTask<Status>("properties-status" + fileExt, path,
        ValidationTemplates.propertiesStatusValidationTemplate);
  }

  public static ICodeGeneratorTask<Fault> getPropertiesFaultValidationTask(String fileExt,
      String path) {
    return new ValidationGeneratorTask<Fault>("properties-fault" + fileExt, path,
        ValidationTemplates.propertiesFaultValidationTemplate);
  }

  public static ICodeGeneratorTask<Property> getPropertiesSinglePropertyValidationTask(
      String fileExt, String path) {
    return new ValidationGeneratorTask<Property>("properties" + fileExt, path,
        ValidationTemplates.propertiesSinglePropertyValidationTemplate);
  }

  public static ICodeGeneratorTask<Event> getEventValidationTask(String fileExt, String path) {
    return new EventValidationGeneratorTask(fileExt, path,
        ValidationTemplates.eventValidationTemplate);
  }

  public static ICodeGeneratorTask<Operation> getOperationParametersValidationTask(String fileExt,
      String path) {
    return new OperationParametersValidationGeneratorTask(fileExt, path,
        ValidationTemplates.operationPayloadValidationTemplate);
  }

  public static ICodeGeneratorTask<Operation> getOperationReturnTypeValidationTask(String fileExt,
      String path) {
    return new OperationReturnTypeValidationGeneratorTask(fileExt, path,
        ValidationTemplates.operationResponseValidationTemplate);
  }

  private static class ValidationGeneratorTask<Element>
      extends AbstractTemplateGeneratorTask<Element> {

    protected String filename;
    private String path;
    private ITemplate<Element> template;

    public ValidationGeneratorTask(String filename, String path, ITemplate<Element> template) {
      this.filename = filename;
      this.path = path;
      this.template = template;
    }

    @Override
    public String getFileName(Element fragment) {
      return filename;
    }

    @Override
    public String getPath(Element fragment) {
      return path;
    }

    @Override
    public ITemplate<Element> getTemplate() {
      return template;
    }
  }

  private static class EventValidationGeneratorTask extends ValidationGeneratorTask<Event> {
    public EventValidationGeneratorTask(String filename, String path, ITemplate<Event> template) {
      super(filename, path, template);
    }

    @Override
    public String getFileName(Event event) {
      return event.getName() + filename;
    }
  }

  private static class OperationParametersValidationGeneratorTask
      extends ValidationGeneratorTask<Operation> {
    public OperationParametersValidationGeneratorTask(String filename, String path,
        ITemplate<Operation> template) {
      super(filename, path, template);
    }

    @Override
    public String getFileName(Operation operation) {
      return operation.getName() + filename;
    }
  }

  private static class OperationReturnTypeValidationGeneratorTask
      extends ValidationGeneratorTask<Operation> {
    public OperationReturnTypeValidationGeneratorTask(String filename, String path,
        ITemplate<Operation> template) {
      super(filename, path, template);
    }

    @Override
    public String getFileName(Operation operation) {
      return operation.getName() + "-response" + filename;
    }
  }
}
