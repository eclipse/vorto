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

import org.eclipse.vorto.core.api.model.model.ModelId;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
public class SingleFileContentCodeGeneratorTask implements ICodeGeneratorTask<ModelId> {

  private String fileName;
  private byte[] content;

  public SingleFileContentCodeGeneratorTask(String fileName, byte[] content) {
    this.fileName = fileName;
    this.content = content;
  }

  @Override
  public void generate(ModelId infoModelId, InvocationContext context, IGeneratedWriter outputter) {
    outputter.write(new Generated(fileName, null, content));
  }

}
