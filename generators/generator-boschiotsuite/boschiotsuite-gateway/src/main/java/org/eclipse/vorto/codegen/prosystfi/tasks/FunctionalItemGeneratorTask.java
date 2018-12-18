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
package org.eclipse.vorto.codegen.prosystfi.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.prosystfi.templates.FunctionalItemTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class FunctionalItemGeneratorTask extends AbstractTemplateGeneratorTask<FunctionblockModel> {

  private String javaFileExtension;
  private String targetPath;
  private String targetPackage;
  private String[] imports;

  public FunctionalItemGeneratorTask(String javaFileExtension, String srcBase, String targetPackage,
      String... imports) {
    super();
    this.javaFileExtension = javaFileExtension;
    this.targetPackage = targetPackage;
    this.targetPath = srcBase + '/' + targetPackage.replace('.', '/');
    this.imports = imports;
  }

  @Override
  public String getFileName(FunctionblockModel fbm) {
    return fbm.getName() + javaFileExtension;
  }

  @Override
  public String getPath(FunctionblockModel fbm) {
    return targetPath;
  }

  @Override
  public ITemplate<FunctionblockModel> getTemplate() {
    return new FunctionalItemTemplate(targetPackage, imports);
  }

}
