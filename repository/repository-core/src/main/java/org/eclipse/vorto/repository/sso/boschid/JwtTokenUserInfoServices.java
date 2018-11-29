/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.sso.boschid;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;

public class JwtTokenUserInfoServices extends UserInfoTokenServices {

  private Interceptor interceptor;

  public JwtTokenUserInfoServices(String userInfoEndpointUrl, String clientId) {
    super(userInfoEndpointUrl, clientId);
  }

  public JwtTokenUserInfoServices(String userInfoEndpointUrl, String clientId,
      Interceptor interceptor) {
    this(userInfoEndpointUrl, clientId);
    this.interceptor = interceptor;
  }

  public Interceptor getInterceptor() {
    return interceptor;
  }

  public void setInterceptor(Interceptor interceptor) {
    this.interceptor = interceptor;
  }
}
