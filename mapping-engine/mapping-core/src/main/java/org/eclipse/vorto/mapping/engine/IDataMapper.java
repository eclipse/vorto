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

import java.util.Optional;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.model.runtime.PropertyValue;

/**
 * Data Mapper that maps specific device payload to Vorto compliant data and vica versa.
 *
 * @param <Result>
 */
public interface IDataMapper {


  @Deprecated
  /**
   * Use {@link#mapSource} instead
   * 
   * @param input
   * @param context
   * @return
   */
  InfomodelValue map(Object input, MappingContext context);

  /**
   * Maps the given source object to Vorto compliant data structure
   * 
   * @param input source input data that is supposed to get mapped.
   * @return mapped payload that complies to Vorto Information Model
   */
  InfomodelValue mapSource(Object input);

  /**
   * Maps the given new property
   * 
   * @param newValue new function block property that is supposed to get mapped to the target
   *        platform
   * @param oldValue old/current function block property
   * @param infomodelProperty property of the information model
   * @return
   */
  Object mapTarget(PropertyValue newValue, Optional<PropertyValue> oldValue,
      String infoModelProperty);

  static DataMapperBuilder newBuilder() {
    return new DataMapperBuilder();
  }
}
