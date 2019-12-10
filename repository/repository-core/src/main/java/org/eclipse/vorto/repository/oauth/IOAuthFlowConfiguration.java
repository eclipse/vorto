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
package org.eclipse.vorto.repository.oauth;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;

/**
 * OAuth2 Webflow Configuration 
 * 
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 *
 */
public interface IOAuthFlowConfiguration {

  /**
   * Creates the filter that activates this oauth provider
   * @return
   */
  Filter createFilter();
  
  /**
   * Returns the user token information service for this provider
   * @return
   */
  UserInfoTokenServices getUserInfoTokenService();

  /**
   * Gets the logout Url for this oauth provider
   * @param request
   * @return
   */
  String getLogoutUrl(HttpServletRequest request);

  /**
   * Gets the oauth provider specific logo url 
   * @return
   */
  String getLogoHref();
  
  /**
   * Gets the url to initiate the oauth web flow
   * @return
   */
  String getFilterProcessingUrl();
}
