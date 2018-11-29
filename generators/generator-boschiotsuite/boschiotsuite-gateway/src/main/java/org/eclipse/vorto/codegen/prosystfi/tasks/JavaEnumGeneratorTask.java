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
import org.eclipse.vorto.codegen.templates.java.JavaEnumTemplate;
import org.eclipse.vorto.core.api.model.datatype.Enum;

public class JavaEnumGeneratorTask extends AbstractTemplateGeneratorTask<Enum> {

  private String javaFileExtension;
  private String targetPath;
  private String enumPackage;

  public JavaEnumGeneratorTask(String javaFileExtension, String srcBase, String enumPackage) {
    super();
    this.javaFileExtension = javaFileExtension;
    this.enumPackage = enumPackage;
    this.targetPath = srcBase + '/' + enumPackage.replace('.', '/');
  }

  @Override
  public String getFileName(Enum en) {
    return en.getName() + javaFileExtension;
  }

  @Override
  public String getPath(Enum en) {
    return targetPath;
  }

  @Override
  public ITemplate<Enum> getTemplate() {
    return new JavaEnumTemplate(enumPackage);
  }

}
