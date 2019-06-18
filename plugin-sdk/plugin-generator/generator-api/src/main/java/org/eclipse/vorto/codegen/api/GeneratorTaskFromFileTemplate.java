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
package org.eclipse.vorto.codegen.api;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
public class GeneratorTaskFromFileTemplate<T> extends AbstractTemplateGeneratorTask<T>
    implements ICodeGeneratorTask<T> {

  private IFileTemplate<T> fileTemplate;

  public GeneratorTaskFromFileTemplate(IFileTemplate<T> template) {
    this.fileTemplate = template;
  }

  @Override
  public String getFileName(T ctx) {
    return fileTemplate.getFileName(ctx);
  }

  @Override
  public String getPath(T ctx) {
    return fileTemplate.getPath(ctx);
  }

  @Override
  public ITemplate<T> getTemplate() {
    return fileTemplate;
  }
}
