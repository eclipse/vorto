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

import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.ModelReferentialIntegrityException;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractRepositoryOperation {
  
  protected static final String FILE_NODES = "*.type | *.fbmodel | *.infomodel | *.mapping ";

  private static Logger logger = Logger.getLogger(AbstractRepositoryOperation.class);

  private Supplier<RequestRepositorySessionHelper> repositorySessionHelperSupplier;

  public <ReturnType> ReturnType doInSession(SessionFunction<ReturnType> fn) {
    Session session = null;
    try {
      session = repositorySessionHelperSupplier.get().getSession();
      return fn.apply(session);
    } catch (PathNotFoundException e) {
      logger.error(e);
      throw new ModelNotFoundException(e);
    } catch (RepositoryException ex) {
      logger.error(ex);
      throw new FatalModelRepositoryException("Cannot create repository session for user", ex);
    } catch (NotAuthorizedException | ModelReferentialIntegrityException e) {
      throw e;
    } catch (Exception ex) {
      logger.warn("Unexpected exception", ex);
      throw new FatalModelRepositoryException("Cannot create repository session for user", ex);
    } finally {
      if (session != null) {
        repositorySessionHelperSupplier.get().logoutSessionIfNotReusable(session);
      }
    }
  }

  public <T> void doInElevatedSession(SessionFunction<T> fn, IUserContext userContext) {
    RequestRepositorySessionHelper helper = new RequestRepositorySessionHelper(false);
    IUserContext elevatedUserContext = getUserContextForCreatingAttachment(userContext);
    try {
      helper.setUser(elevatedUserContext.getAuthentication());
      helper.setRepository(repositorySessionHelperSupplier.get().getRepository());
      helper.setRolesInTenant(Stream.of(Role.SYS_ADMIN).collect(Collectors.toSet()));
      helper.setTenantId(repositorySessionHelperSupplier.get().getTenantId());
      fn.apply(helper.getSession());
    } catch (Exception e) {
      throw new FatalModelRepositoryException("Unexpected exception", e);
    } finally {
      helper.logoutSessionIfNotReusable(helper.getSession());
    }
  }

  private IUserContext getUserContextForCreatingAttachment(IUserContext userContext) {
    if (userContext.isAnonymous()) {
      return PrivilegedUserContextProvider.systemAdminContext();
    }
    return PrivilegedUserContextProvider.systemAdminContext(userContext.getUsername());
  }

  public void setRepositorySessionHelperSupplier(Supplier<RequestRepositorySessionHelper> repositorySessionHelperSupplier) {
    this.repositorySessionHelperSupplier = repositorySessionHelperSupplier;
  }

  @FunctionalInterface
  public interface SessionFunction<K> {
    K apply(Session session) throws Exception;
  }
  
}
