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
