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
