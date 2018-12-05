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

import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface JwtVerifyAndIdStrategy {
  /*
   * Create a spring security OAuth2Authentication based on the jwt token given
   */
  OAuth2Authentication createAuthentication(JwtToken jwtToken);

  /*
   * Verifies a jwt token
   */
  boolean verify(JwtToken jwtToken);
}
