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
