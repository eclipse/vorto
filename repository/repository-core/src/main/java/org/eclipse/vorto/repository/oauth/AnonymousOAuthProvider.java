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
package org.eclipse.vorto.repository.oauth;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * Provides a fake {@link IOAuthProvider} for anonymous users. <br/>
 * Returned by implementations of {@link IOAuthProviderRegistry} when
 * {@link IOAuthProviderRegistry#getByAuthentication(Authentication)} is invoked on an anonymous
 * user authentication.
 */
public class AnonymousOAuthProvider implements IOAuthProvider {

  @Override
  public String getId() {
    return "";
  }

  @Override
  public String getLabel() {
    return "";
  }

  @Override
  public boolean canHandle(Authentication authentication) {
    return false;
  }

  @Override
  public boolean canHandle(String jwtToken) {
    return false;
  }

  @Override
  public Authentication authenticate(HttpServletRequest request, String jwtToken) {
    return null;
  }

  @Override
  public OAuthUser createUser(Authentication authentication) {
    return null;
  }

  @Override
  public boolean supportsWebflow() {
    return false;
  }

  @Override
  public Optional<IOAuthFlowConfiguration> getWebflowConfiguration() {
    return Optional.empty();
  }
}
