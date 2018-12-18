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
package org.eclipse.vorto.deviceadapter;

import java.util.List;
import org.eclipse.vorto.model.runtime.PropertyValue;

/**
 * New Configuration which can be set via
 * {@link IDeviceData#setConfiguration(INewConfiguration, String)}}
 *
 */
public interface INewConfiguration {

  /**
   * sets the configuration value
   * 
   * @param name
   * @param value
   * @throws IllegalArgumentException if the given propertyName is not defined in the Information
   *         Model
   */
  void addConfigurationValue(String propertyName, Object propertyValue);

  /**
   * The information model property for which the configuration applies
   * 
   * @return
   */
  String getInfomodelProperty();

  /**
   * Gets the configuration values
   * 
   * @return
   */
  List<PropertyValue> getConfigurationValues();
}
