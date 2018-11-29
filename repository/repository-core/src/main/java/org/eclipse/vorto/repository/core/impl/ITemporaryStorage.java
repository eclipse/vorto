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
package org.eclipse.vorto.repository.core.impl;

import org.eclipse.vorto.repository.importer.FileUpload;

public interface ITemporaryStorage {

  /**
   * stores the given model content and returns a handleId
   * 
   * @param content
   * @param modelType
   * @return
   */
  StorageItem store(String key, FileUpload value, long timeToLiveSeconds);

  /**
   * retrieves a stored item
   * 
   * @param key
   * @return the stored item or null if no item exists for the specified key
   */
  StorageItem get(String key);

  /**
   * removes the stored item for the specified key
   * 
   * @param key
   * @return removed item
   */
  StorageItem remove(String key);

  /**
   * clears all expired stored items
   */
  void clearExpired();
}
