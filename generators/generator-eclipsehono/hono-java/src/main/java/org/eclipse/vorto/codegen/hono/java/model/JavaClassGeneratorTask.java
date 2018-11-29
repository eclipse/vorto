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
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldGetterTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldSetterTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaEntityTemplate;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

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
