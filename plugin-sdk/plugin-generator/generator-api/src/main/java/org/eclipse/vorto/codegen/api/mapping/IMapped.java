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
package org.eclipse.vorto.codegen.api.mapping;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
public interface IMapped<T> {

  boolean isMapped();

  /**
   * source element which is mapped
   * 
   * @return
   */
  T getSource();

  /**
   * Returns the generator specific stereotype that the generator understands to generate additional
   * source code for the mapped element.
   * 
   * @return
   */
  String getStereoType();

  /**
   * Checks if the mapping exposes the given attribute
   * 
   * @param attributeName
   * @return
   */
  boolean hasAttribute(String attributeName);

  /**
   * Returns the actual value of the attribute name
   * 
   * @param attributeName
   * @param defaultValue
   * @return
   */
  String getAttributeValue(String attributeName, String defaultValue);

}
