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
package org.eclipse.vorto.repository.utils;

import com.google.common.base.Strings;
import java.util.Collection;

public class PreConditions {

  public static void withPattern(String pattern, String item, String name) {
    notNullOrEmpty(item, name);
    if (!item.matches(pattern)) {
      throw new IllegalArgumentException(String.format("%s '%s' does not match pattern: '%s'", name, item, pattern));
    }
  }
  
  public static void notNullOrEmpty(String item, String name) {
    if (Strings.nullToEmpty(item).trim().isEmpty()) {
      throw new IllegalArgumentException(name + " shouldn't be null or empty"); 
    }
  }
  
  public static void notNull(Object o, String message) {
    if (o == null) {
      throw new IllegalArgumentException(message); 
    }
  }
  
  public static <K> void notNullOrEmpty(K[] k, String message) {
    if (k == null || k.length < 1) {
      throw new IllegalArgumentException(message); 
    }
  }
  
  public static <K> void notNullOrEmpty(Collection<K> k, String message) {
    if (k == null || k.size() < 1) {
      throw new IllegalArgumentException(message); 
    }
  }
}
