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
