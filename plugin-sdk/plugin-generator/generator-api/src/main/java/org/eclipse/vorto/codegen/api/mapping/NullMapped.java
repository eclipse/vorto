/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.api.mapping;

public class NullMapped<T> implements IMapped<T> {

  private T source;

  public NullMapped(T source) {
    this.source = source;
  }

  @Override
  public T getSource() {
    return source;
  }

  @Override
  public String getStereoType() {
    return "";
  }

  @Override
  public boolean hasAttribute(String attributeName) {
    return false;
  }

  @Override
  public String getAttributeValue(String attributeName, String defaultValue) {
    return defaultValue;
  }

  @Override
  public boolean isMapped() {
    return false;
  }
}
