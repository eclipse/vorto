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
package org.eclipse.vorto.repository.oauth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GithubOAuthProviderConfigurationTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Test
    public void contextStringTestSlash() {
        when(mockRequest.getRequestURI()).thenReturn("/");
        when(mockRequest.getRequestURL()).thenReturn(new StringBuffer("https://vorto.eclipseprojects.io"));
        GithubOAuthProviderConfiguration configuration = new GithubOAuthProviderConfiguration(null, null, "/");
        String result = configuration.getLogoutUrl(mockRequest);
        assertEquals("https://vorto.eclipseprojects.io/logout", result);
    }

    @Test
    public void contextStringTestRepository() {
        when(mockRequest.getRequestURI()).thenReturn("/repository");
        when(mockRequest.getRequestURL()).thenReturn(new StringBuffer("https://vorto.eclipseprojects.io/repository"));
        GithubOAuthProviderConfiguration configuration = new GithubOAuthProviderConfiguration(null, null, "/repository");
        String result = configuration.getLogoutUrl(mockRequest);
        assertEquals("https://vorto.eclipseprojects.io/repository/logout", result);
    }

}
