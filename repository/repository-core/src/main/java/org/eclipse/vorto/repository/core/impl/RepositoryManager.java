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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Supplier;
import javax.jcr.ImportUUIDBehavior;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IRepositoryManager;

public class RepositoryManager extends AbstractRepositoryOperation implements IRepositoryManager {

  private static Logger logger = Logger.getLogger(RepositoryManager.class);
  
  private Supplier<Session> defaultSessionSupplier;
  
  @Override
  public byte[] backup() {
    return doInSession(session -> {
      try {
        return backupRepository(session);
      } catch (IOException e) {
        logger.error("Exception while making a backup", e);
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

        logger.info("Attempting to restore backup");
        session.getWorkspace().importXML("/", new ByteArrayInputStream(data),
            ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
        logger.info("Restored backup succesfully");

      } catch (RepositoryException | IOException e) {
        logger.error("Backup failed. Will try to revert the restoration with previous data.", e);
        try {
          logger.info("Reverting to old data.");
          session.getWorkspace().importXML("/", new ByteArrayInputStream(oldData),
              ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
          logger.info("Reverted the restoration succesfully");
        } catch (RepositoryException | IOException ex) {
          logger.error("Revert of restoration unsuccesfull", ex);
        }
        throw e;
      }
      return null;
    });
  }

  @Override
  public boolean createTenantWorkspace(final String tenantId) {
    try {
      Workspace workspace = defaultSessionSupplier.get().getWorkspace();
      workspace.createWorkspace(tenantId);
      return true;
    } catch (RepositoryException e) {
      logger.error("Exception while creating workspace", e);
      throw new FatalModelRepositoryException("Cannot create workspace for user", e);
    }
  }
  
  @Override
  public boolean removeTenantWorkspace(final String tenantId) {
    return doInSession(session -> {
      Workspace workspace = session.getWorkspace();
      workspace.deleteWorkspace(tenantId);
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
