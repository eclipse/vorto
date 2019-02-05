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
import org.eclipse.vorto.codegen.prosystfi.templates.FunctionalItemImplTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class FunctionalItemImplGeneratorTask
    extends AbstractTemplateGeneratorTask<FunctionblockModel> {

  public static final String FI_IMPL_PACKAGE = "impl";
  public static final String SUFFIX = "Impl";

  private String javaFileExtension;
  private String targetPath;
  private String targetPackage;
  private String[] imports;


  public FunctionalItemImplGeneratorTask(String javaFileExtension, String srcBase,
      String interfacePackage, String... imports) {
    super();
    this.javaFileExtension = javaFileExtension;
    this.targetPackage = interfacePackage + '.' + FI_IMPL_PACKAGE;
    this.targetPath = srcBase + '/' + targetPackage.replace('.', '/');
    if (imports != null && imports.length > 0) {
      this.imports = new String[imports.length + 1];
      System.arraycopy(imports, 0, this.imports, 0, imports.length);
    } else {
      this.imports = new String[1];
    }
    this.imports[this.imports.length - 1] = interfacePackage;
  }

  @Override
  public String getFileName(FunctionblockModel fbm) {
    return fbm.getName() + SUFFIX + javaFileExtension;
  }

  @Override
  public String getPath(FunctionblockModel fbm) {
    return targetPath;
  }

  @Override
  public ITemplate<FunctionblockModel> getTemplate() {
    return new FunctionalItemImplTemplate(targetPackage, imports);
  }

}
