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
package org.eclipse.vorto.repository.api.content;

import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.repository.api.ModelId;

@Deprecated
public interface IMappedElement {

  /**
   * Target Platform of the mapping
   * 
   * @return
   */
  String getTargetPlatformKey();

  /**
   * Gets all stereotypes that are associated with the current model element
   * 
   * @return
   */
  List<Stereotype> getStereotypes();

  /**
   * Gets a specific stereotype by name for the current model element
   * 
   * @param name
   * @return
   */
  Optional<Stereotype> getStereotype(String name);

  /**
   * Gets the referenced mapping model Id for the current model element
   * 
   * @return
   */
  ModelId getMappingReference();


}
