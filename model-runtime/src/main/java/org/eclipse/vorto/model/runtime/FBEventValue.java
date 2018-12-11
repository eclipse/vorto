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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.vorto.model.ModelEvent;
import org.eclipse.vorto.model.ModelProperty;

public class FBEventValue {

  private ModelEvent meta;

  private List<PropertyValue> eventProperties = new ArrayList<PropertyValue>();

  public FBEventValue(ModelEvent meta) {
    super();
    this.meta = meta;
  }

  public void withProperty(String name, Object value) {
    Optional<ModelProperty> mp =
        meta.getProperties().stream().filter(p -> p.getName().equals(name)).findFirst();
    if (!mp.isPresent()) {
      throw new IllegalArgumentException(
          "Event property with given name is not defined in Function Block Event Type");
    }
    this.eventProperties.add(new PropertyValue(mp.get(), value));
  }

  public List<PropertyValue> getProperties() {
    return Collections.unmodifiableList(eventProperties);
  }

  public ModelEvent getMeta() {
    return meta;
  }

  public Map<String, Object> serialize() {
    Map<String, Object> result = new HashMap<String, Object>();

    Map<String, Object> data = new HashMap<String, Object>();
    for (PropertyValue eventProperty : eventProperties) {
      data.put(eventProperty.getMeta().getName(), eventProperty.getValue());
    }
    result.put(meta.getName(), data);
    return result;
  }
}
