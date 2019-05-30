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
package org.eclipse.vorto.repository.conversion;

import java.util.Optional;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;

/**
 * Converts Vorto DSL Models to and from different formats, e.g. Json Schema , ... 
 * @author Alexander Edelmann
 *
 * @param <T> other Vorto object model
 */
public interface IModelConverter<T> {

  /**
   * Converts
   * @param modelId
   * @param platformKey
   * @return
   */
  T convertTo(ModelId modelId, Optional<String> platformKey);
  
  /**
   * 
   * @param source
   * @return
   */
  IModelWorkspace convertFrom(T source);
}
