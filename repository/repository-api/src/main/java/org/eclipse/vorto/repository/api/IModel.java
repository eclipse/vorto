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
package org.eclipse.vorto.repository.api;

import java.util.Collection;

@Deprecated
public interface IModel {

  /**
   * unique id of the model
   * 
   * @return
   */
  ModelId getId();

  /**
   * Type of model
   * 
   * @return
   */
  ModelType getType();

  /**
   * Gets the display name of the model as described in the vorto model, can be null
   * 
   * @return
   */
  String getDisplayName();

  /**
   * Gets the description of the model as described in the vorto model, can be null
   * 
   * @return
   */
  String getDescription();

  /**
   * Gets all model ids that the model depends on, never null
   * 
   * @return
   */
  Collection<ModelId> getReferences();
}
