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
package org.eclipse.vorto.repository.oauth.internal;

import org.eclipse.vorto.repository.oauth.AnonymousOAuthProvider;
import org.eclipse.vorto.repository.oauth.IOAuthProvider;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class DefaultOAuthProviderRegistry implements IOAuthProviderRegistry {
  
  private Collection<IOAuthProvider> providers = new ArrayList<>();

  public DefaultOAuthProviderRegistry(@Autowired Collection<IOAuthProvider> providers) {
    this.providers = providers;
  }

  public Optional<IOAuthProvider> getProviderFor(String jwtToken) {
    return providers.stream().filter(provider -> provider.canHandle(jwtToken)).findFirst();
  }
  
  public Optional<IOAuthProvider> getProviderFor(Authentication auth) {
    return providers.stream().filter(authProvider -> authProvider.canHandle(auth)).findFirst();
  }

  @Override
  public IOAuthProvider getByToken(String token) {
    return providers.stream().filter(provider -> provider.canHandle(token)).findFirst().get();
  }

  @Override
  public IOAuthProvider getByAuthentication(Authentication auth) {
    return providers
        .stream()
        .filter(authProvider -> authProvider.canHandle(auth))
        .findFirst()
        .orElse(new AnonymousOAuthProvider());
  }
  
  @Override
  public Optional<IOAuthProvider> getById(String providerId) {
    return providers.stream().filter(authProvider -> authProvider.getId().equals(providerId)).findFirst();
  }

  @Override
  public void registerOAuthProvider(IOAuthProvider provider) {
     if (this.providers.stream().filter(p -> p.getId().equalsIgnoreCase(provider.getId())).count() > 0) {
       throw new IllegalArgumentException("Provider with specified ID already exists");
     }
     this.providers.add(provider);
  }

  @Override
  public void unregisterOAuthProvider(String id) {
    for (Iterator<IOAuthProvider> iter = this.providers.iterator();iter.hasNext();) {
      IOAuthProvider providerToRemove = iter.next();
      if (providerToRemove.getId().equals(id)) {
        iter.remove();
      }
    }
  }

  @Override
  public IOAuthProvider getByPrincipal(Principal user) {
    return getByAuthentication((Authentication)user);
  }

  @Override
  public Collection<IOAuthProvider> list() {
    return Collections.unmodifiableCollection(this.providers);
  }
}
