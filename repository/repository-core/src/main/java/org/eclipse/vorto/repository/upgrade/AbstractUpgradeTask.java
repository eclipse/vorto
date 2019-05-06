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
package org.eclipse.vorto.repository.upgrade;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import org.eclipse.vorto.repository.core.IModelSearchService;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


public abstract class AbstractUpgradeTask implements IUpgradeTask {

  protected IModelSearchService modelSearchService;

  public AbstractUpgradeTask(IModelSearchService modelSearchService) {
    this.modelSearchService = modelSearchService;
  }

  public Optional<IUpgradeTaskCondition> condition() {
    return Optional.empty();
  }

  public IModelSearchService getModelSearchService() {
    return modelSearchService;
  }

  public void setModelSearchService(IModelSearchService modelSearchService) {
    this.modelSearchService = modelSearchService;
  }

  protected void setAdminUserContext() {
    Authentication dummyAuthentication = createAuthentication();
    SecurityContextHolder.getContext().setAuthentication(dummyAuthentication);
  }

  protected Authentication createAuthentication() {
    return new Authentication() {

      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      @Override
      public String getName() {
        return "admin";
      }

      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return SpringUserUtils.toAuthorityList(new HashSet<>(Arrays.asList(Role.SYS_ADMIN)));
      }

      @Override
      public Object getCredentials() {
        return null;
      }

      @Override
      public Object getDetails() {
        return null;
      }

      @Override
      public Object getPrincipal() {
        return "admin";
      }

      @Override
      public boolean isAuthenticated() {
        return false;
      }

      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // is not invoked since it is a dummy provider
      }

    };
  }
}
