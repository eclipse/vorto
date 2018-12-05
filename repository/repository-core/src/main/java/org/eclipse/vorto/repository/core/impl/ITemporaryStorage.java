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
