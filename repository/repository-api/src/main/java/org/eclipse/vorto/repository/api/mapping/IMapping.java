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
package org.eclipse.vorto.repository.api.mapping;

import org.eclipse.vorto.repository.api.content.IMappedElement;
import org.eclipse.vorto.repository.api.content.ModelProperty;

@Deprecated
public interface IMapping {

  /**
   * creates a new query which lets you find vorto model properties by platform-specfic meta
   * attributes
   * 
   * @param element
   * @return
   */
  IMappingQuery<ModelProperty> newPropertyQuery(IMappedElement element);
}
