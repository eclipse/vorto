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
package org.eclipse.vorto.codegen.api.mapping;

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
