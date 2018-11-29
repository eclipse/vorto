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
package org.eclipse.vorto.repository.api.content;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ModelProperty extends AbstractProperty {

  private List<IPropertyAttribute> attributes = new ArrayList<IPropertyAttribute>();

  public List<IPropertyAttribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<IPropertyAttribute> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String toString() {
    return "ModelProperty [attributes=" + attributes + ", isMandatory=" + mandatory + ", name="
        + name + ", isMultiple=" + isMultiple + ", description=" + description + ", type=" + type
        + ", constraints=" + constraints + "]";
  }


}
