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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.oauth.IOAuthFlowConfiguration;
import org.eclipse.vorto.repository.oauth.IOAuthProvider;

public class OAuthProvider {

  private static final String BOSCH_SUITE_OAUTH_LABEL = "Bosch IoT Suite OAuth";

  private String id;
  private String label;
  private String loginUrl;
  private String logoHref;
  
  public OAuthProvider(String id, String label, IOAuthFlowConfiguration configuration) {
    this.id = id;
    this.label = label;
    this.logoHref = configuration != null ? configuration.getLogoHref() : "";
    this.loginUrl = configuration != null ? configuration.getFilterProcessingUrl() : "";
  }

  /**
   * The only purpose of this ugly utility method is to "merge" the various versions of Bosch IoT
   * Suite OAuth provider (v.1 and v.2 at the time of writing), in order to present a single
   * provider to the end user, who is agnostic to which version they will be using. <br/>
   * This feature is used in the {@link org.eclipse.vorto.repository.web.HomeController}, when
   * populating the list of non-webflow {@link IOAuthProvider} DTOs for the UI. <br/>
   * In the UI, this is not used at login (since we are dealing with non-webflow providers), but
   * rather when choosing the desired authentication methodology for new technical users.<br/>
   * Note: the merging is done by comparing the label to a constant, since importing the suite oauth
   * providers would introduce a circular dependency.
   */
  public static List<OAuthProvider> mergeBoschIotSuiteOAuthProviders(List<OAuthProvider> providers) {
    // reducing any number of providers with Bosch Suite OAuth label to one, if applicable
    // we don't care which one, as both versions have the same presentational properties ("fake" picture
    // and label)
    Optional<OAuthProvider> singleSuiteOAuthProvider = providers.stream().filter(i -> i.getLabel().equals(BOSCH_SUITE_OAUTH_LABEL)).reduce((a,b) -> a);
    // getting non-Bosch Suite OAuth providers if any
    List<OAuthProvider> result = providers.stream().filter(i -> !i.getLabel().equals(BOSCH_SUITE_OAUTH_LABEL)).collect(
        Collectors.toList());
    // adding reduced Suite OAuth providers if any
    if (singleSuiteOAuthProvider.isPresent()) {
      result.add(singleSuiteOAuthProvider.get());
    }
    return result;
  }

  public static OAuthProvider from(IOAuthProvider source) {
    OAuthProvider result = new OAuthProvider();
    result.setId(source.getId());
    result.setLabel(source.getLabel());
    if (source.getWebflowConfiguration().isPresent()) {
      IOAuthFlowConfiguration config = source.getWebflowConfiguration().get();
      result.setLogoHref(config.getLogoHref());
      result.setLoginUrl(config.getFilterProcessingUrl());
      // TODO this is temporary and ugly: assigning the Bosch ID icon to the non-webflow providers
      // because there will only be the two Suite OAuth versions in there  for now.
    } else {
      result.setLogoHref("webjars/repository-web/dist/images/bosch-social.png");
    }
    return result;
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
