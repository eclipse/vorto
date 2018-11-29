/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.hono.java.model;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.hono.java.Utils;
import org.eclipse.vorto.codegen.templates.java.JavaEnumTemplate;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

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
