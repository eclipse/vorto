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
