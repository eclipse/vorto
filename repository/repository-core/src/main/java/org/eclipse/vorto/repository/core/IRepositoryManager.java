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
package org.eclipse.vorto.repository.core;

public interface IRepositoryManager {

  /**
   * Backs up the given repository
   *
   * @return backup which can be use to restore at a later time
   */
  byte[] backup();

  /**
   * Restores a repository with the given data
   *
   * @param data
   */
  void restore(byte[] data);

  /**
   * Creates a workspace with the given workspaceId
   *
   * @return true if creation is successful
   */
  boolean createWorkspace(final String workspaceId);

  /**
   * Returns whether the workspace for this tenant exist or not
   *
   * @param workspaceId
   * @return
   */
  boolean exists(final String workspaceId);

  /**
   * Removes a workspace with the given workspaceId
   *
   * @return true if removal is successful
   */
  boolean removeWorkspace(final String workspaceId);
}
