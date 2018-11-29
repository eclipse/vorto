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

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;

public class DefaultMapped<T> implements IMapped<T> {

  private T source;

  private StereoTypeTarget stereoType;

  public DefaultMapped(T source, StereoTypeTarget stereoType) {
    this.source = source;
    this.stereoType = stereoType;
  }

  @Override
  public T getSource() {
    return source;
  }

  @Override
  public String getStereoType() {
    return stereoType.getName();
  }

  @Override
  public boolean hasAttribute(String attributeName) {
    for (Attribute attribute : stereoType.getAttributes()) {
      if (attribute.getName().equalsIgnoreCase(attributeName)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public String getAttributeValue(String attributeName, String defaultValue) {
    for (Attribute attribute : stereoType.getAttributes()) {
      if (attribute.getName().equalsIgnoreCase(attributeName)) {
        return attribute.getValue();
      }
    }

    return defaultValue;
  }

  @Override
  public boolean isMapped() {
    return true;
  }

}
