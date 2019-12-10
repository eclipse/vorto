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

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface IOAuthProvider {
  /**
   * A unique ID of this provider
   * @return
   */
  String getId();
  
  /**
   * 
   * @return
   */
  String getLabel();
  
  /**
   * Returns true if the provider can handle this authentication
   * @param auth
   * @return
   */
  boolean canHandle(Authentication authentication);
  
  /**
   * returns if this provider can handle this token
   * @param jwtToken
   * @return
   */
  boolean canHandle(String jwtToken);

  /**
   * 
   * @param request
   * @param jwtToken
   * @return
   * @throws OAuthAuthenticationException if the specific token request cannot be verified
   */
  Authentication authenticate(HttpServletRequest request, String jwtToken) throws OAuthAuthenticationException;
  
  
  /**
   * Creates an OAuth user for the given authentication object
   * 
   * @param authentication
   * @return
   */
  OAuthUser createUser(Authentication authentication);
  
  /**
   * returns true if the oauth provider supports web flow via the Repository UI
   * @return
   */
  boolean supportsWebflow();
  
  /**
   * OAuth Webflow configuration
   * @return
   */
  Optional<IOAuthFlowConfiguration> getWebflowConfiguration();
  
  
  public class OAuthAuthenticationException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public OAuthAuthenticationException(Throwable t) {
      super(t);
    }
    
    public OAuthAuthenticationException(String msg) {
      super(msg);
    }
    public OAuthAuthenticationException(String msg, Throwable t) {
      super(msg,t);
    }
  }
}
