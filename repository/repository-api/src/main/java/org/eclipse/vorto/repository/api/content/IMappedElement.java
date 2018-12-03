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
