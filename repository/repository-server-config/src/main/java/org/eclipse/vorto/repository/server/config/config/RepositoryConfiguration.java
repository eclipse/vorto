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
package org.eclipse.vorto.repository.server.config.config;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.search.impl.SimpleSearchService;
import org.eclipse.vorto.repository.sso.TokenUtils;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.utils.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RepositoryConfiguration extends BaseConfiguration {

  @Value("${http.proxyHost:#{null}}")
  private String proxyHost;

  @Value("${http.proxyPort:8080}")
  protected int proxyPort;

  @Value("${http.proxyUser:#{null}}")
  private String proxyUsername;

  @Value("${http.proxyPassword:#{null}}")
  private String proxyPassword;

  @Value("${repo.configFile}")
  private String repositoryConfigFile = null;
  
  @Autowired
  private ITenantService tenantService;
  
  @Autowired
  private IModelRepositoryFactory repositoryFactory;

  @Bean
  public org.modeshape.jcr.RepositoryConfiguration repoConfiguration() throws Exception {
    return org.modeshape.jcr.RepositoryConfiguration
        .read(new ClassPathResource(repositoryConfigFile).getURL());
  }

  @Bean
  public AccessTokenProvider accessTokenProvider() {
    if (proxyHost != null && proxyUsername != null) {
      return TokenUtils.proxiedAccessTokenProvider(proxyHost, proxyPort, proxyUsername,
          proxyPassword);
    } else if (proxyHost != null && proxyUsername == null) {
      return TokenUtils.proxiedAccessTokenProvider(proxyHost, proxyPort);
    } else {
      return TokenUtils.accessTokenProvider();
    }
  }
  
  @Bean
  @Profile(value = {"local","local-test", "local-https"})
  public SimpleSearchService simpleSearch() {
    return new SimpleSearchService(this.tenantService, this.repositoryFactory);
  }
  
  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add(new LoggingInterceptor());
    restTemplate.setInterceptors(interceptors);
    return restTemplate;
  }
}
