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
package org.eclipse.vorto.repository.backup;

import java.util.Collection;
import java.util.function.Predicate;
import org.eclipse.vorto.repository.domain.Tenant;

public interface IBackupRestoreService {
  
  /**
   * Creates a zipped backup for tenants who passed the tenantFilter 
   * 
   * @param tenantFilter a predicate that determines who among the tenants are placed in the backup
   * @return a byte array for a zip file that contains the backup
   */
  byte[] createBackup(Predicate<Tenant> tenantFilter);
  
  /**
   * Restores the given backup file to its tenants
   * 
   * @param backupFile the zipped backup file
   * @param tenantFilter a filter for which tenants to restore. If you want to restore to all tenants, 
   * pass a predicate that returns true
   * @return collection of tenants restored
   */
  Collection<Tenant> restoreRepository(byte[] backupFile, Predicate<Tenant> tenantFilter);
}
