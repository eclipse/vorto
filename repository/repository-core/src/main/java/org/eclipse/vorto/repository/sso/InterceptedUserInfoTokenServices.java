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
package org.eclipse.vorto.repository.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.stereotype.Component;

@Component("githubUserInfoTokenService")
public class InterceptedUserInfoTokenServices extends UserInfoTokenServices {

  @Autowired
  public InterceptedUserInfoTokenServices(
      @Value("${github.oauth2.resource.userInfoUri}") String userInfoEndpointUrl,
      @Value("${github.oauth2.client.clientId}") String clientId) {
    super(userInfoEndpointUrl, clientId);
  }
}
