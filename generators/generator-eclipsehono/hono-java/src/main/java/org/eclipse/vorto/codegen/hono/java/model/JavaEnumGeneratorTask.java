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
package org.eclipse.vorto.codegen.hono.java.model;

import org.eclipse.vorto.codegen.hono.java.Utils;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.utils.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.plugin.generator.utils.ITemplate;
import org.eclipse.vorto.plugin.generator.utils.javatemplates.JavaEnumTemplate;

public class JavaEnumGeneratorTask extends AbstractTemplateGeneratorTask<Enum> {

  private String javaFileExtension = ".java";
  private InformationModel infomodel;

  public JavaEnumGeneratorTask(InformationModel infomodel) {
    this.infomodel = infomodel;
  }

  @Override
  public String getFileName(Enum entity) {
    return entity.getName() + javaFileExtension;
  }

  @Override
  public String getPath(Enum entity) {
    return Utils.getJavaPackageBasePath(this.infomodel) + "/model/datatypes";
  }

  @Override
  public ITemplate<Enum> getTemplate() {
    return new JavaEnumTemplate(Utils.getJavaPackage(this.infomodel) + ".model.datatypes");
  }

}
