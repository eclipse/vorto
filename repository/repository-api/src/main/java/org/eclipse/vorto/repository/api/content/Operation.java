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
public class Operation extends DefaultMappedElement {

  private String name;
  private String description;

  private boolean isBreakable = false;

  private ReturnType result;

  private List<Param> params = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isBreakable() {
    return isBreakable;
  }

  public void setBreakable(boolean isBreakable) {
    this.isBreakable = isBreakable;
  }

  public ReturnType getResult() {
    return result;
  }

  public void setResult(ReturnType result) {
    this.result = result;
  }

  public List<Param> getParams() {
    return params;
  }

  public void setParams(List<Param> params) {
    this.params = params;
  }

  @Override
  public String toString() {
    return "OperationDto [name=" + name + ", description=" + description + ", isBreakable="
        + isBreakable + ", result=" + result + ", params=" + params + "]";
  }


}
