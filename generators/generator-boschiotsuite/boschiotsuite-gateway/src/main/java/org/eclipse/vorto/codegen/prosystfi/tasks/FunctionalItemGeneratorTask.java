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
