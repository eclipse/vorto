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
package org.eclipse.vorto.mapping.engine;

/**
 * A Mapping Context contains further configuration attributes that are processed during the mapping
 *
 */
public class MappingContext {

  /**
   * Empty Mapping Context tries to map all function block properties of the information model
   * 
   * @return new Mapping Context holding the configuration
   */
  public static MappingContext empty() {
    return new MappingContext();
  }
}
