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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Stereotype {

  private String name;

  private static final String SOURCE = "source";
  private static final String TARGET = "target";
  private static final String FUNCTIONS = "functions";
  private static final String XPATH_ATT = "xpath";
  private static final String KEY_ATT = "key";
  private static final String VALUE_ATT = "value";
  private static final String CONDITION_ATT = "condition";

  private Map<String, String> attributes = new HashMap<String, String>();

  public static Stereotype createWithFunction(String functionName, String functionBody) {
    Map<String, String> attributes = new HashMap<String, String>(1);
    attributes.put(functionName, functionBody);
    return new Stereotype(FUNCTIONS, attributes);
  }

  public static Stereotype createWithXpath(String xpath) {
    Map<String, String> attributes = new HashMap<String, String>(1);
    attributes.put(XPATH_ATT, xpath);
    return new Stereotype(SOURCE, attributes);
  }

  public static Stereotype createOperationTarget(String key, String value) {
    Map<String, String> attributes = new HashMap<String, String>(1);
    attributes.put(KEY_ATT, key);
    attributes.put(VALUE_ATT, value);
    return new Stereotype(TARGET, attributes);
  }

  public static Stereotype createTarget() {
    Map<String, String> attributes = new HashMap<String, String>(1);
    return new Stereotype(TARGET, attributes);
  }

  public static Stereotype createWithConditionalXpath(String condition, String xpath) {
    Map<String, String> attributes = new HashMap<String, String>(2);
    attributes.put(CONDITION_ATT, condition);
    attributes.put(XPATH_ATT, xpath);
    return new Stereotype(SOURCE, attributes);
  }

  public static Stereotype create(String name, Map<String, String> attributes) {
    return new Stereotype(name, attributes);
  }

  Stereotype(String name, Map<String, String> attributes) {
    this.name = name;
    this.attributes = attributes;
  }

  protected Stereotype() {

  }

  public String getName() {
    return name;
  }

  public Map<String, String> getAttributes() {
    return Collections.unmodifiableMap(attributes);
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String toString() {
    return "Stereotype [name=" + name + ", attributes=" + attributes + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Stereotype other = (Stereotype) obj;
    if (attributes == null) {
      if (other.attributes != null)
        return false;
    } else if (!attributes.equals(other.attributes))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }



}
