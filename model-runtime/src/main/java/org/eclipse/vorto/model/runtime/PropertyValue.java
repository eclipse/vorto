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
package org.eclipse.vorto.model.runtime;

import org.eclipse.vorto.model.ModelProperty;

public class PropertyValue {
  private ModelProperty meta;
  private Object value;

  public PropertyValue(ModelProperty meta, Object value) {
    this.meta = meta;
    this.value = value;
  }

  public ModelProperty getMeta() {
    return meta;
  }

  public void setMeta(ModelProperty meta) {
    this.meta = meta;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "PropertyValue [meta=" + meta + ", value=" + value + "]";
  }



}
