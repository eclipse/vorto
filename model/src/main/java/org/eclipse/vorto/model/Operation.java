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
package org.eclipse.vorto.model;

import java.util.ArrayList;
import java.util.List;

public class Operation extends DefaultMappedElement {

  private String name;
  private String description;

  private boolean isBreakable = false;

  private ReturnType result;

  private List<Param> params = new ArrayList<>();

  public Operation(String name) {
    this.name = name;
  }
  
  protected Operation() {
    
  }
  
  public static Builder Builder(String name) {
    return new Builder(name);
  }

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

  public static class Builder {
    private Operation operation;
    
    public Builder(String name) {
      this.operation = new Operation(name);
    }
    
    public Builder withParam(Param param) {
      this.operation.getParams().add(param);
      return this;
    }
    
    public Builder isBreakable() {
      operation.setBreakable(true);
      return this;
    }
    
    public Builder description(String description) {
      operation.setDescription(description);
      return this;
    }
    
    public Builder withResult(ReturnType type) {
      operation.setResult(type);
      return this;
    }
    
    public Operation build() {
      return this.operation;
    }
  }

}
