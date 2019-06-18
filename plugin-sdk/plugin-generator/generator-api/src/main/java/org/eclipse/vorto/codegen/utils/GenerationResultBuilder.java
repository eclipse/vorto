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
package org.eclipse.vorto.codegen.utils;

import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.ZipContentExtractCodeGeneratorTask;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
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
