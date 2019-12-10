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

/**
 * 
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 *
 */
public interface IOAuthProvider {
  
  /**
   * A unique ID of this provider
   * @return
   */
  String getId();
  
  /**
   * A descriptive label for the OAuth Provider
   * @return
   */
  String getLabel();
  
  /**
   * Checks if the provider can handle the given authentication object
   * @param auth
   * @return
   */
  boolean canHandle(Authentication authentication);
  
  /**
   * 
   * Checks if this provider can handle the given token
   * @param jwtToken
   * @return
   */
  boolean canHandle(String jwtToken);

  /**
   * Performs the actual authentication for the given token
   * @param request http servlet request
   * @param jwtToken JSON Webtoken to authenticate
   * @return Authentication object if the provided token is valid 
   * 
   * @throws OAuthAuthenticationException if the specific token request cannot be verified
   */
  Authentication authenticate(HttpServletRequest request, String jwtToken) throws OAuthAuthenticationException;
  
  
  /**
   * Creates an OAuth user for the given authentication object
   * 
   * @param authentication
   * @return OAuthUser containing information about the authenticated user
   */
  OAuthUser createUser(Authentication authentication);
  
  /**
   * Indicates if the provider supports OAuth Webflow. If yes, a {@link IOAuthProvider#getWebflowConfiguration()}} can be read
   * @return true if the provider supports web flow , false otherwise
   */
  boolean supportsWebflow();
  
  /**
   * Returns a webflow configuration for the oauth provider
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
