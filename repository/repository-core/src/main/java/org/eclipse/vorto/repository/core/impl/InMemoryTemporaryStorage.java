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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.importer.FileUpload;

public class InMemoryTemporaryStorage implements ITemporaryStorage {

  private Map<String, StorageItem> storage = new HashMap<String, StorageItem>();

  private static Logger logger = Logger.getLogger(InMemoryTemporaryStorage.class);

  @Override
  public StorageItem store(String key, FileUpload value, long timeToLiveSeconds) {
    StorageItem newItem = new StorageItem(key, value, new Date(), timeToLiveSeconds);
    storage.put(key, newItem);
    logger.info("Added " + newItem + " in temporary storage");
    return newItem;
  }

  @Override
  public StorageItem get(String key) {
    StorageItem item = storage.get(key);
    if (item != null && item.isExpired()) {
      remove(key);
      item = null;
    }
    return item;
  }

  @Override
  public StorageItem remove(String key) {
    logger.info("Removing " + key + " from temporary storage");
    return storage.remove(key);
  }

  @Override
  public void clearExpired() {
    logger.info("Clearing expired storage items from temporary storage");
    for (Iterator<String> iter = storage.keySet().iterator(); iter.hasNext();) {
      String key = iter.next();
      StorageItem item = storage.get(key);
      if (item.isExpired()) {
        logger.info("Removing " + key + " from temporary storage");
        iter.remove();
      }
    }

  }


}
