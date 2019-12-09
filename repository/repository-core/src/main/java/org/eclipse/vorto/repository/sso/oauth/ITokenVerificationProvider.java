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
package org.eclipse.vorto.repository.sso.oauth;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface ITokenVerificationProvider {
  /**
   * The ID of this provider
   * @return
   */
  String getId();
  
  /**
   * Returns true if the provider can handle this authentication
   * @param auth
   * @return
   */
  boolean canHandle(Authentication auth);
  
  /**
   * returns if this provider can handle this token
   * @param jwtToken
   * @return
   */
  boolean canHandle(String jwtToken);
  
  /*
   * Create a spring security OAuth2Authentication based on the jwt token given
   */
  OAuth2Authentication createAuthentication(HttpServletRequest request, String jwtToken);

  /*
   * Verifies a jwt token
   */
  boolean verify(HttpServletRequest request, String jwtToken);
}
