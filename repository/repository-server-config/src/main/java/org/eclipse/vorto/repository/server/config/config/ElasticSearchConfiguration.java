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
package org.eclipse.vorto.repository.server.config.config;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.AWSRequestSigningApacheInterceptor;
import com.google.common.base.Strings;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.search.ElasticSearchService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Elastic Search Service Configuration, supporting local Docker as well as AWS Elastic Service
 *
 */
@Configuration
@Profile(value = {"prod", "int", "local-docker", "local-dev", "local-dev-mysql", "local-benchmark-test"})
public class ElasticSearchConfiguration {
  
  @Value("${server.config.skipSslVerification:false}")
  private boolean skipSslVerification;
  
  @Value("${http.proxyHost:#{null}}")
  private String proxyHost;

  @Value("${http.proxyPort:8080}")
  protected int proxyPort;

  @Value("${http.proxyUser:#{null}}")
  private String proxyUsername;

  @Value("${http.proxyPassword:#{null}}")
  private String proxyPassword;

  @Value("${testcontainers.es:9200}")
  private int esPort;
  
  private final static String SERVICE_NAME = "es";
  
  @Value("${aws.region:eu-central-1}")
  private String region;
  
  @Value("${aws.aesEndpoint:https://search-vorto-test-i566tsfta74oi6s6hbzfwproui.eu-central-1.es.amazonaws.com}")
  private String aesEndpoint;
  
  private AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

  private static final Logger LOGGER = Logger.getLogger(ElasticSearchConfiguration.class);
  
  @Autowired
  private RestHighLevelClient client;
  
  @Autowired
  private IModelRepositoryFactory repositoryFactory;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @Autowired
  private IOAuthProviderRegistry registry;
  

  @Bean
  @Profile(value = { "prod", "int", "local-docker", "local-dev", "local-dev-mysql","local-benchmark-test"})
  public ElasticSearchService elasticSearch() {
    return new ElasticSearchService(client, repositoryFactory, userNamespaceRoleService, registry);
  }
  
  @Bean
  @Profile({ "local-docker", "local-dev", "local-dev-mysql" ,"local-benchmark-test"})
  public RestHighLevelClient indexingClient() {
    RestClientBuilder clientBuilder = RestClient.builder(new HttpHost("localhost", esPort, "http"),
        new HttpHost("localhost", 9201, "http"));
    
    return new RestHighLevelClient(clientBuilder);
  }
  
  @Bean
  @Profile({ "prod" , "int"})
  public RestHighLevelClient awsIndexingClient() {
    LOGGER.debug(
        String.format(
            "Creating an elastic server client with config(serviceName=%s region=%s aesEndpoint=%s",
            SERVICE_NAME, region, aesEndpoint
        )
    );
    
    AWS4Signer signer = new AWS4Signer();
    signer.setServiceName(SERVICE_NAME);
    signer.setRegionName(region);
    
    HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(SERVICE_NAME, signer, credentialsProvider);
    
    RestClientBuilder builder = RestClient.builder(HttpHost.create(aesEndpoint))
        .setHttpClientConfigCallback(httpClientConfig(getProxy(), interceptor));
    
    return new RestHighLevelClient(builder);
  }

  private HttpClientConfigCallback httpClientConfig(Optional<HttpHost> proxy, HttpRequestInterceptor interceptor) {
    return (httpClientBuilder) -> {
      httpClientBuilder.addInterceptorLast(interceptor);
      
      if (proxy.isPresent()) {
        httpClientBuilder.setProxy(proxy.get());
        Optional<CredentialsProvider> credentialProvider = getCredentialsProvider(proxy.get());
        if (credentialProvider.isPresent()) {
          httpClientBuilder.setDefaultCredentialsProvider(credentialProvider.get());
        }
      }
      
      if (skipSslVerification) {
        try {
          SSLContext sslContext = new SSLContextBuilder()
              .loadTrustMaterial(null, (x509Certificates, s) -> true)
              .build();
          
          httpClientBuilder.setSSLContext(sslContext);
          httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
          LOGGER.error("Error while trying to skip SSL verification", e);
        }
      }
      
      return httpClientBuilder;
    };
  }
  
  private Optional<HttpHost> getProxy() {
    if (!Strings.isNullOrEmpty(proxyHost)) {
      return Optional.of(new HttpHost(proxyHost, proxyPort, "http"));
    }
    
    return Optional.empty();
  }
  
  private Optional<CredentialsProvider> getCredentialsProvider(HttpHost host) {
    if (!Strings.isNullOrEmpty(proxyUsername)) {
      CredentialsProvider credsProvider = new BasicCredentialsProvider();
      credsProvider.setCredentials(
              new AuthScope(host.getHostName(), host.getPort()),
              new UsernamePasswordCredentials(proxyUsername, proxyPassword));
      return Optional.of(credsProvider);
    }
    
    return Optional.empty();
  }

  @PreDestroy
  public void deinit() {
    try {
      client.close();
    } catch (IOException e) {
      LOGGER.error("Not able to close indexing client", e);
    }
  }
}
