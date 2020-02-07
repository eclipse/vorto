/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.web.oauth;

import org.eclipse.vorto.repository.oauth.IOAuthFlowConfiguration;

public class OAuthProvider {

  private String id;
  private String label;
  private String loginUrl;
  private String logoHref;
  
  public OAuthProvider(String id, String label, IOAuthFlowConfiguration configuration) {
    this.id = id;
    this.label = label;
    this.logoHref = configuration.getLogoHref();
    this.loginUrl = configuration.getFilterProcessingUrl();
  }
  
  public OAuthProvider() {
    
  }
  
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  public String getLogoHref() {
    return logoHref;
  }
  public void setLogoHref(String logoHref) {
    this.logoHref = logoHref;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }
  
  
  
  
}
