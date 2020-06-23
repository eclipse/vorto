/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
import org.eclipse.vorto.repository.domain.Namespace;

public interface IBackupRestoreService {

  /**
   * Creates a zipped backup for tenants who passed the tenantFilter
   *
   * @param namespaceFilter a predicate that determines who among the namespaces are placed in the backup
   * @return a byte array for a zip file that contains the backup
   */
  byte[] createBackup(Predicate<Namespace> namespaceFilter);

  /**
   * Restores the given backup file to its tenants
   *
   * @param backupFile      the zipped backup file
   * @param namespaceFilter a filter for which namespaces to restore. If you want to restore to all
   *                        namespaces, pass a predicate that returns {@literal true}.
   * @return collection of tenants restored
   */
  Collection<Namespace> restoreRepository(byte[] backupFile, Predicate<Namespace> namespaceFilter);
}
