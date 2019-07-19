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

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import org.eclipse.vorto.repository.domain.Tenant;

public interface IBackupRestoreService {
  
  static Function<Tenant, String> tenantSignature = (tenant) -> 
    tenant.getNamespaces().stream().findFirst().get().getName();
  
  /**
   * Creates a map with namespace as key and the backup xml (in byte array) as value 
   * 
   * @param tenantFilter filter for the tenants we wish to include. 
   * Return a predicate that returns True if you want all tenants.
   * @return a map of namespace to a backup byte array
   */
   Map<String, byte[]> createBackups(Predicate<Tenant> tenantFilter);
  
  /**
   * Creates a zipped backup (in byte array) of the given backup 
   * 
   * @param backups a map of namespace to backup xml (in byte array)
   * @return a byte array of the resulting zipped file
   */
  byte[] createZippedInputStream(Map<String, byte[]> backups);
  
  /**
   * Restores the given backup file to its tenants
   * 
   * @param backupFile the zipped backup file
   * @param tenantFilter a filter for which tenants to restore. If you want to restore to all tenants, 
   * pass a predicate that returns true
   */
  void restoreRepository(byte[] backupFile, Predicate<Tenant> tenantFilter);
}
