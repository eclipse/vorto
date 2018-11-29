/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
