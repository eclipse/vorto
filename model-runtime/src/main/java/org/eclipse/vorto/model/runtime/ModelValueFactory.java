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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelProperty;

public class ModelValueFactory {

  public static PropertyValue createFBPropertyValue(FunctionblockModel model, String name,
      Object value) {

    Optional<ModelProperty> property =
        getAllStateProperties(model).stream().filter(p -> p.getName().equals(name)).findAny();
    if (!property.isPresent()) {
      throw new IllegalArgumentException(
          "No property defined with this name in Function Block Model");
    }

    return new PropertyValue(property.get(), value);
  }

  private static List<ModelProperty> getAllStateProperties(FunctionblockModel model) {
    List<ModelProperty> properties = new ArrayList<ModelProperty>();
    properties.addAll(model.getConfigurationProperties());
    properties.addAll(model.getStatusProperties());
    return properties;
  }
}
