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
package org.eclipse.vorto.codegen.api;

import org.eclipse.vorto.core.api.model.model.ModelId;

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
