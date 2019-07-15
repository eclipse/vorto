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
package org.eclipse.vorto.codegen.api.mapping;

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
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
