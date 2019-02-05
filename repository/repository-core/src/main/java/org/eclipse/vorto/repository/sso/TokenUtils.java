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

import java.util.Arrays;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;

public class TokenUtils {
    public static AccessTokenProvider proxiedAccessTokenProvider(String proxyHost, int proxyPort,
        String proxyUser, String proxyPassword) {
        ClientHttpRequestFactory requestFactory =
            proxyAuthenticatedRequestFactory(proxyHost, proxyPort, proxyUser, proxyPassword);
        return buildAccessTokenProvider(requestFactory);
    }

    public static AccessTokenProvider proxiedAccessTokenProvider(String proxyHost, int proxyPort) {
        ClientHttpRequestFactory requestFactory =
            proxyAuthenticatedRequestFactory(proxyHost, proxyPort);
        return buildAccessTokenProvider(requestFactory);
    }

    private static AccessTokenProvider buildAccessTokenProvider(
        ClientHttpRequestFactory requestFactory) {
        AuthorizationCodeAccessTokenProvider authorizationCodeAccessTokenProvider =
            new AuthorizationCodeAccessTokenProvider();
        authorizationCodeAccessTokenProvider.setRequestFactory(requestFactory);

        ImplicitAccessTokenProvider implicitAccessTokenProvider = new ImplicitAccessTokenProvider();
        implicitAccessTokenProvider.setRequestFactory(requestFactory);

        return new AccessTokenProviderChain(
            Arrays.<AccessTokenProvider>asList(authorizationCodeAccessTokenProvider,
                implicitAccessTokenProvider, new ResourceOwnerPasswordAccessTokenProvider(),
                new ClientCredentialsAccessTokenProvider()));
    }

    public static AccessTokenProvider accessTokenProvider() {
        AuthorizationCodeAccessTokenProvider authorizationCodeAccessTokenProvider =
            new AuthorizationCodeAccessTokenProvider();

        ImplicitAccessTokenProvider implicitAccessTokenProvider = new ImplicitAccessTokenProvider();

        return new AccessTokenProviderChain(
            Arrays.<AccessTokenProvider>asList(authorizationCodeAccessTokenProvider,
                implicitAccessTokenProvider, new ResourceOwnerPasswordAccessTokenProvider(),
                new ClientCredentialsAccessTokenProvider()));
    }

    public static ClientHttpRequestFactory proxyAuthenticatedRequestFactory(String proxyHost,
        int proxyPort, String proxyUser, String proxyPassword) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(proxyHost, proxyPort),
            new UsernamePasswordCredentials(proxyUser, proxyPassword));
        return getClientBuilder(proxyHost, proxyPort, credsProvider);
    }

    private static ClientHttpRequestFactory proxyAuthenticatedRequestFactory(String proxyHost,
        int proxyPort) {
        return getClientBuilder(proxyHost, proxyPort, null);
    }

    private static ClientHttpRequestFactory getClientBuilder(String proxyHost, int proxyPort,
        CredentialsProvider credsProvider) {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(new HttpHost(proxyHost, proxyPort));
        if (credsProvider != null){
            clientBuilder.setDefaultCredentialsProvider(credsProvider);
        }
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

        CloseableHttpClient client = clientBuilder.build();

        HttpComponentsClientHttpRequestFactory factory =
            new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);

        return factory;
    }


}
