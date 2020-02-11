/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

public final class PropertyValueFactory {

  public static PropertyValue create(ModelProperty meta, Object value) {
    if (value instanceof EntityValue) {
      return new EntityPropertyValue(meta, (EntityValue)value);
    } else if (value instanceof EnumValue) {
      return new EnumPropertyValue(meta,(EnumValue)value);
    } else {
      return new PropertyValue(meta, value);
    }
  }
}
