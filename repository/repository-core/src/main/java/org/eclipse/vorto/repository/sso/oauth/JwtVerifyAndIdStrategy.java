/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
