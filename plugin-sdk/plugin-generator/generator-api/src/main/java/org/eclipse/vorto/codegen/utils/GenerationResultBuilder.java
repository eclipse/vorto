/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.utils;

import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.ZipContentExtractCodeGeneratorTask;

public class GenerationResultBuilder {

  private IGenerationResult result;

  private GenerationResultBuilder(IGenerationResult result) {
    this.result = result;
  }

  public static GenerationResultBuilder from(IGenerationResult result) {
    GenerationResultBuilder builder = new GenerationResultBuilder(result);
    return builder;
  }

  public GenerationResultBuilder append(IGenerationResult result) {
    if (result == null) {
      return this;
    }

    ZipContentExtractCodeGeneratorTask task =
        new ZipContentExtractCodeGeneratorTask(result.getContent());
    task.generate(null, InvocationContext.simpleInvocationContext(),
        (IGeneratedWriter) this.result);
    return this;
  }

  public IGenerationResult build() {
    return result;
  }
}
