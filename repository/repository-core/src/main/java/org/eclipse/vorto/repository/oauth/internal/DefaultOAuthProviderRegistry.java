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
package org.eclipse.vorto.repository.oauth.internal;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import org.eclipse.vorto.repository.oauth.IOAuthProvider;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class DefaultOAuthProviderRegistry implements IOAuthProviderRegistry {
  
  @Autowired
  private Collection<IOAuthProvider> providers = new ArrayList<IOAuthProvider>();

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
    return providers.stream().filter(authProvider -> authProvider.canHandle(auth)).findFirst().get();

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
