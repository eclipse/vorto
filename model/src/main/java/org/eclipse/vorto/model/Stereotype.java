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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Stereotype {

  private String name;

  public static final String SOURCE = "source";
  public static final String TARGET = "target";
  public static final String FUNCTIONS = "functions";
  public static final String XPATH_ATT = "xpath";
  private static final String KEY_ATT = "key";
  private static final String VALUE_ATT = "value";
  public static final String CONDITION_ATT = "condition";

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

  public static Stereotype createCondition(String condition) {
	  Map<String, String> attributes = new HashMap<String, String>(2);
	    attributes.put("value", condition);
	    return new Stereotype("condition", attributes);
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

public boolean hasAttribute(String attribute) {
	return this.attributes.containsKey(attribute) && !"".equals(this.attributes.get(attribute));
}



}
