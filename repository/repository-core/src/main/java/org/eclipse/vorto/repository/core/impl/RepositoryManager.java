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
package org.eclipse.vorto.repository.core.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;
import javax.jcr.ImportUUIDBehavior;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IRepositoryManager;

public class RepositoryManager extends AbstractRepositoryOperation implements IRepositoryManager {

  private static final Logger LOGGER = Logger.getLogger(RepositoryManager.class);

  private Supplier<Session> defaultSessionSupplier;

  @Override
  public byte[] backup() {
    return doInSession(session -> {
      try {
        return backupRepository(session);
      } catch (IOException e) {
        LOGGER.error("Exception while making a backup", e);
        throw new FatalModelRepositoryException(
            "Something went wrong while making a backup of the system.", e);
      }
    });
  }

  private byte[] backupRepository(Session session) throws RepositoryException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    session.exportSystemView("/", baos, false, false);
    baos.close();
    return baos.toByteArray();
  }

  @Override
  public void restore(byte[] data) {
    doInSession(session -> {
      byte[] oldData = null;
      try {
        oldData = backupRepository(session);

        LOGGER.info("Attempting to restore backup");
        session.getWorkspace().importXML("/", new ByteArrayInputStream(data),
            ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
        LOGGER.info("Restored backup successfully");

      } catch (RepositoryException | IOException e) {
        LOGGER.error("Backup failed. Will try to revert the restoration with previous data.", e);
        try {
          LOGGER.info("Reverting to old data.");
          session.getWorkspace().importXML("/", new ByteArrayInputStream(oldData),
              ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
          LOGGER.info("Reverted the restoration successfully");
        } catch (RepositoryException | IOException ex) {
          LOGGER.error("Revert of restoration unsuccessful", ex);
        }
        throw e;
      }
      return null;
    });
  }

  @Override
  public boolean createWorkspace(final String workspaceId) {
    try {
      Workspace workspace = defaultSessionSupplier.get().getWorkspace();
      if (!exists(workspace, workspaceId)) {
        workspace.createWorkspace(workspaceId);
        return true;
      } else {
        LOGGER.info("Workspace with ID '" + workspaceId + "' already exists.");
        return false;
      }
    } catch (RepositoryException e) {
      LOGGER.error("Exception while creating workspace", e);
      throw new FatalModelRepositoryException("Cannot create workspace for user", e);
    }
  }

  @Override
  public boolean exists(final String workspaceId) {
    try {
      Workspace workspace = defaultSessionSupplier.get().getWorkspace();
      return exists(workspace, workspaceId);
    } catch (RepositoryException e) {
      LOGGER.error("Exception while accessing workspace", e);
      throw new FatalModelRepositoryException("Cannot access workspace for user", e);
    }
  }

  private boolean exists(final Workspace workspace, final String workspaceId)
      throws RepositoryException {
    return Arrays.asList(workspace.getAccessibleWorkspaceNames()).contains(workspaceId);
  }

  @Override
  public boolean removeWorkspace(final String workspaceId) {
    return doInSession(session -> {
      Workspace workspace = session.getWorkspace();
      workspace.deleteWorkspace(workspaceId);
      return true;
    });
  }

  public Supplier<Session> getDefaultSessionSupplier() {
    return defaultSessionSupplier;
  }

  public void setDefaultSessionSupplier(Supplier<Session> defaultSessionSupplier) {
    this.defaultSessionSupplier = defaultSessionSupplier;
  }
}
