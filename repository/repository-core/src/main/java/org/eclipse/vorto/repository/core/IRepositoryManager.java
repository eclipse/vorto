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
package org.eclipse.vorto.repository.core;

public interface IRepositoryManager {

  /**
   * Backs up the given repository
   * @return backup which can be use to restore at a later time
   */
  byte[] backup();
  
  /**
   * Restores a repository with the given data
   * @param data
   */
  void restore(byte[] data);
  
  /**
   * Creates a workspace with the given tenantId
   * 
   * @return true if creation is successful
   */
  boolean createTenantWorkspace(final String tenantId);
  
  /**
   * Removes a workspace with the given tenantId
   * 
   * @return true if removal is successful
   */
  boolean removeTenantWorkspace(final String tenantId);
}
