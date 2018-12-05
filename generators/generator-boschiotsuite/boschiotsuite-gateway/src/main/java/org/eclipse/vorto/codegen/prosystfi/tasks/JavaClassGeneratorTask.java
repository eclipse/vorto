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
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldGetterTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldSetterTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaClassFieldTemplate;
import org.eclipse.vorto.codegen.templates.java.JavaEntityTemplate;
import org.eclipse.vorto.core.api.model.datatype.Entity;

public class JavaClassGeneratorTask extends AbstractTemplateGeneratorTask<Entity> {

  private String javaFileExtension;
  private String targetPath;
  private String classPackage;
  private String getterPrefix;
  private String setterPrefix;

  public JavaClassGeneratorTask(String javaFileExtension, String srcBase, String classPackage,
      String getterPrefix, String setterPrefix) {
    super();
    this.javaFileExtension = javaFileExtension;
    this.classPackage = classPackage;
    this.targetPath = srcBase + '/' + classPackage.replace('.', '/');
    this.getterPrefix = getterPrefix;
    this.setterPrefix = setterPrefix;
  }

  @Override
  public String getFileName(Entity entity) {
    return entity.getName() + javaFileExtension;
  }

  @Override
  public String getPath(Entity entity) {
    return targetPath;
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
    return new JavaEntityTemplate(classPackage, fieldTemplate, getterTemplate, setterTemplate);
  }

}
