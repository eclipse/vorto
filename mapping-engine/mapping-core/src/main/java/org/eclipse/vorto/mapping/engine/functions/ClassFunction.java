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
package org.eclipse.vorto.mapping.engine.functions;

public class ClassFunction implements IFunction {

  private String namespace;

  private Class<?> functionClass;

  public ClassFunction(String namespace, Class<?> classFunction) {
    this.namespace = namespace;
    this.functionClass = classFunction;
  }

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public Class<?> getFunctionClass() {
    return functionClass;
  }

}
