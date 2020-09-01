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

import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BoschIoTSuiteOAuthProviderAuthCodeTest {

    public static final String CLIENT_ID = "client-id";
    public static final String TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6InB1YmxpYzo5YzI0MTg0OC01MmQyLTRkM2YtYmFmOS1mNzE3OTYxMmE3YWMiLCJ0eXAiOiJKV1QifQ.eyJhdWQiOltdLCJjbGllbnRfaWQiOiIzYTQzZmJjMC1iNjM5LTQzNmYtYWJhNC1kNzA1MTZkNDRiZDIiLCJleHAiOjE1OTcxMzMwODAsImV4dCI6e30sImlhdCI6MTU5NzEyOTQ4MCwiaXNzIjoiaHR0cHM6Ly9hY2Nlc3MuYm9zY2gtaW90LXN1aXRlLmNvbS92Mi8iLCJqdGkiOiI3YTZkNmEyMy00MmEzLTQ2OWEtOWJmNy0wYzNhNWQ2NmJmYjgiLCJuYmYiOjE1OTcxMjk0ODAsInNjcCI6WyJzZXJ2aWNlOmlvdC1odWItcHJvZDp0NTZmMDcxMjgxMzZlNDE5Y2JiNjg0ZWMzZGQ5ZDA0MDFfaHViL2Z1bGwtYWNjZXNzIiwic2VydmljZTppb3QtdGhpbmdzLWV1LTE6NTZmMDcxMjgtMTM2ZS00MTljLWJiNjgtNGVjM2RkOWQwNDAxX3RoaW5ncy9mdWxsLWFjY2VzcyJdLCJzdWIiOiIzYTQzZmJjMC1iNjM5LTQzNmYtYWJhNC1kNzA1MTZkNDRiZDIifQ.jMxGGYgCFRh9vvGyvyAssVsnE98I0nEJaTqpri7W6x4kBEc6xElW0P1adBrQPY9Ou37AEN0KmyyCOTWpu3rYbfylf-azF6G67iqkQnCXjReoxdwmRxcxluo6FNlodnl7krE96AgFXEWcuAy9KEJzDHNUAymqWpIbG7aIPmjw3JPRBXs_aADg8vSnDGL080qS44f5l3F2g6DBl1M0uUPTrrP8p2r-GfKOwRSSsPaCMIjPj36VUKKvPAhcFeIzL52zgWW2pPpNAeZjLIMC_LY6Dm5fgfyILvmmJKAvVgs8YoAOeQBPhJNyLtHw_XVxEQ1NttM8CS-2BQREDHwXdounCxoNL8Qi_jZgiLE3B6J3Ufcsgf0vBXtXmZNge_5m2_J4vzlGLRou9abVDjsDsrltPk5vR--e43FKvo7AbyQlKesfI4kbUfa9Wq00eQ2OHSpMOldvBwVElDiefd5FiGrmOhxS9M1PquBfzqCaupu_1an0BSGW-zQ6JRMtbGeBu-K9L6TF-plJHLcCo_sBN-Ay7JglKKv2wWoxwUbeYPWNeio5CIMCE6XQuSfPiCe0XEiD4QpHgkqmi_qwxLsFOP4XFovSEkkrQAuRLfxeycfv-MkL3JjKU09hxiMAwcacBbFiIvXecC4NEYvo-vuUFECCCbcp_1ChJpTqM2-OdMkVLlk";

    private BoschIoTSuiteOAuthProviderAuthCode sut;

    @Mock
    private BoschIoTSuiteOAuthProviderConfiguration configuration;

    @Mock
    private DefaultUserAccountService userAccountService;

    @Mock
    private UserNamespaceRoleService userNamespaceRoleService;

    @Before
    public void instantiate() {
        sut = new BoschIoTSuiteOAuthProviderAuthCode(CLIENT_ID, configuration, userAccountService, userNamespaceRoleService);
    }

    @Test
    public void canHandleSuccessTest() {
        assertTrue(sut.canHandle(TOKEN));
    }

    @Test
    public void canHandleFailureTest() {
        assertFalse(sut.canHandle("invalid-token"));
    }

    @Test
    public void canHandleAuthenticationSuccessTest() {
        OAuth2Request request = new OAuth2Request(null, CLIENT_ID, null, true, null, null, null, null,null);
        OAuth2Authentication authentication = new OAuth2Authentication(request, null);
        assertTrue(sut.canHandle(authentication));
    }

    @Test
    public void canHandleAuthenticationFailureTest() {
        OAuth2Request request = new OAuth2Request(null, "invalid-client", null, true, null, null, null, null,null);
        OAuth2Authentication authentication = new OAuth2Authentication(request, null);
        assertFalse(sut.canHandle(authentication));
    }

    @Test
    public void authenticateUserNotFoundTest() {
        when(userAccountService.getUser(any())).thenReturn(null);
        assertNull(sut.authenticate(null, TOKEN));
    }

    @Test
    public void authenticateInvalidTokenTest() {
       Authentication authentication = sut.authenticate(null, "invalid-token");
       assertNull(authentication);
    }

    @Test
    public void authenticateSuccessTest() {
        when(userAccountService.getUser(any())).thenReturn(new User());
        Authentication authentication = sut.authenticate(null, TOKEN);
        assertTrue(authentication.isAuthenticated());
    }



}
