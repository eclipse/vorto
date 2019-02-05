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
