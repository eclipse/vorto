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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.ModelProperty;

public class EntityValue {

  private EntityModel meta;

  private List<PropertyValue> entityProperties = new ArrayList<PropertyValue>();
  
  public EntityValue(EntityModel meta) {
    super();
    this.meta = meta;
  }

  public void withProperty(String name, Object value) {
    Optional<ModelProperty> mp =
        meta.getProperties().stream().filter(p -> p.getName().equals(name)).findFirst();
    if (!mp.isPresent()) {
      throw new IllegalArgumentException(
          "Entity property with given name is not defined");
    }
    this.entityProperties.add(new PropertyValue(mp.get(), value));
  }

  public List<PropertyValue> getProperties() {
    return Collections.unmodifiableList(entityProperties);
  }
  
  public Optional<PropertyValue> getPropertyValue(String name) {
    return getProperties().stream().filter(p -> p.getMeta().getName().equals(name)).findAny();
  }

  public EntityModel getMeta() {
    return meta;
  }

  public Map<String, Object> serialize() {
    Map<String, Object> result = new HashMap<String, Object>();

    for (PropertyValue entityValue : entityProperties) {
      result.put(entityValue.getMeta().getName(), entityValue.getValue());
    }
    return result;
  }
}
