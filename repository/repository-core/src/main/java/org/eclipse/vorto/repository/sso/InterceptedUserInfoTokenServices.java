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
