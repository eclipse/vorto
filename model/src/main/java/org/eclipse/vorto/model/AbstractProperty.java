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
package org.eclipse.vorto.model;

import java.util.ArrayList;
import java.util.List;

public class AbstractProperty extends DefaultMappedElement {

  protected boolean mandatory;
  protected String name;

  protected boolean isMultiple;
  protected String description;

  protected IReferenceType type = null;

  protected List<Constraint> constraints = new ArrayList<>();

  public List<Constraint> getConstraints() {
    return constraints;
  }

  public void setConstraints(List<Constraint> constraints) {
    this.constraints = constraints;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
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

  public IReferenceType getType() {
    return type;
  }

  public void setType(IReferenceType type) {
    this.type = type;
  }

  public boolean isPrimitive() {
    return type instanceof PrimitiveType;
  }

  public boolean isMultiple() {
    return isMultiple;
  }

  public void setMultiple(boolean isMultiple) {
    this.isMultiple = isMultiple;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (mandatory ? 1231 : 1237);
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    AbstractProperty other = (AbstractProperty) obj;
    if (mandatory == other.mandatory && name.equals(other.name) && type.equals(other.type))
      return true;
    else {
      return false;
    }
  }



}
