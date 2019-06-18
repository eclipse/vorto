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
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.utils.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.plugin.generator.utils.ITemplate;
import org.eclipse.vorto.plugin.generator.utils.javatemplates.JavaClassFieldGetterTemplate;
import org.eclipse.vorto.plugin.generator.utils.javatemplates.JavaClassFieldSetterTemplate;
import org.eclipse.vorto.plugin.generator.utils.javatemplates.JavaClassFieldTemplate;
import org.eclipse.vorto.plugin.generator.utils.javatemplates.JavaEntityTemplate;

public class JavaClassGeneratorTask extends AbstractTemplateGeneratorTask<Entity> {

  private String javaFileExtension = ".java";
  private String getterPrefix = "get";
  private String setterPrefix = "set";

  private InformationModel infomodel;

  public JavaClassGeneratorTask(InformationModel infomodel) {
    this.infomodel = infomodel;
  }

  @Override
  public String getFileName(Entity entity) {
    return entity.getName() + javaFileExtension;
  }

  @Override
  public String getPath(Entity entity) {
    return Utils.getJavaPackageBasePath(this.infomodel) + "/model/datatypes";
  }

  @Override
  public ITemplate<Entity> getTemplate() {
    // Configure a Java class field template
    JavaClassFieldTemplate fieldTemplate = new JavaClassFieldTemplate();

    // Configure a Java class getter template
    JavaClassFieldGetterTemplate getterTemplate = new JavaClassFieldGetterTemplate(getterPrefix);

    // Configure a Java class setter template
    JavaClassFieldSetterTemplate setterTemplate = new JavaClassFieldSetterTemplate(setterPrefix);

    // Configure and return the Java class template
    return new JavaEntityTemplate(Utils.getJavaPackage(this.infomodel) + ".model.datatypes",
        fieldTemplate, getterTemplate, setterTemplate);
  }


}
