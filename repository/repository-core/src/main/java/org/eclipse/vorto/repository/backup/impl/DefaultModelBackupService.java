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
package org.eclipse.vorto.repository.backup.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import javax.jcr.ImportUUIDBehavior;
import javax.jcr.InvalidSerializedDataException;
import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import org.eclipse.vorto.repository.backup.IModelBackupService;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.security.SpringSecurityCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DefaultModelBackupService implements IModelBackupService {

  @Autowired
  private IModelRepository modelRepository;

  @Autowired
  private Repository repository;

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  public final String EXCEPTION_MESSAGE_CURRENT_CONTENT =
      "Not able to create a backup of the current content";
  public final String EXCEPTION_MESSAGE_RESTORABLE_CONTENT =
      "Not able to restore the imported backup";

  @Override
  public byte[] backup() throws Exception {
    org.modeshape.jcr.api.Session session = (org.modeshape.jcr.api.Session) getSession();
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      session.exportSystemView("/", baos, false, false);
      baos.close();
      return baos.toByteArray();
    } finally {
      session.logout();
    }
  }

  protected Session getSession() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      return repository.login(new SpringSecurityCredentials(authentication));
    } catch (RepositoryException ex) {
      throw new FatalModelRepositoryException("Cannot create repository session for user", ex);
    }
  }

  @Override
  public void restore(byte[] restorableContent) throws Exception {
    byte[] currentContent = backup();

    org.modeshape.jcr.api.Session session = (org.modeshape.jcr.api.Session) getSession();
    try {

      try {
        LOGGER.info("attempting to import imported backup");
        doRestore(restorableContent, session);
      } catch (Exception invalidContent) {
        doRestore(currentContent, session);
        throw new Exception(this.EXCEPTION_MESSAGE_RESTORABLE_CONTENT, invalidContent);
      }
    } finally {
      session.logout();
    }
  }

  private void doRestore(byte[] backup, org.modeshape.jcr.api.Session session)
      throws AccessDeniedException, VersionException, PathNotFoundException, ItemExistsException,
      ConstraintViolationException, InvalidSerializedDataException, LockException, IOException,
      RepositoryException {

    session.getWorkspace().importXML("/", new ByteArrayInputStream(backup),
        ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
    LOGGER.info("created backup succesfully");
  }

  public IModelRepository getModelRepository() {
    return modelRepository;
  }

  public void setModelRepository(IModelRepository modelRepository) {
    this.modelRepository = modelRepository;
  }
}
