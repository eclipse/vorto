/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
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
