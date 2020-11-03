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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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

@RunWith(MockitoJUnitRunner.class)
public class BoschIoTSuiteOAuthProviderAuthCodeTest extends AbstractVerifierTest {

    public static final String CLIENT_ID = "client-id";
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InB1YmxpYzoxYjI2ZDEwYi1iMTZjLTQ4MDQtYmM4MC1jMWUzOWJhNTZlMjAifQ.eyJhdWQiOltdLCJjbGllbnRfaWQiOiJhNzY3MzdhNC0zMTUyLTQ5MzgtYTg4OC0yZTA3ZDQyNzI4NjciLCJleHAiOjQxMjcxNzY5ODYsImV4dCI6e30sImlhdCI6MTYwNDQwMDc4OSwiaXNzIjoiaHR0cHM6Ly9hY2Nlc3MuYm9zY2gtaW90LXN1aXRlLmNvbS92Mi8iLCJqdGkiOiJiMDM1NmY0MS1mZTg1LTQ3YjUtOTE0NC03NTQxMGZmZWY5ZGQiLCJuYmYiOjE2MDQ0MDA3ODksInNjcCI6WyJzZXJ2aWNlOmlvdC1odWItcHJvZDp0NTZmMDcxMjgxMzZlNDE5Y2JiNjg0ZWMzZGQ5ZDA0MDFfaHViL2Z1bGwtYWNjZXNzIiwic2VydmljZTppb3QtdGhpbmdzLWV1LTE6NTZmMDcxMjgtMTM2ZS00MTljLWJiNjgtNGVjM2RkOWQwNDAxX3RoaW5ncy9mdWxsLWFjY2VzcyIsInNlcnZpY2U6dm9ydG8iXSwic3ViIjoiYTc2NzM3YTQtMzE1Mi00OTM4LWE4ODgtMmUwN2Q0MjcyODY3In0.DIhx8eZHExyL2ooZ8JwVc56GshEnfw7MXUv5Y7o_H3UOJaU09yAd4DbIdN2QJi1vz2isSv2gS2Ada5yxCrJQ8W_1rVfdgP3L-_X-clu6skIYX6Dp1gnv5UipzBBj8zAkomdas4b85jbcV6OLvOnCzOpreEiJfCZRCpjMMufg6_Or2Fx-nFG7jJBDv7TR3UcJy0_YlENItkkLjgjjKeTIVNuM5YAF0MxN69gV5dB5j-fwRDmDbcJbZl4WCOf02yBNBaDUgyOzR7DjEiYcbcCprAusH1zIcWySsa9eCD2kqCgQ6ihlJebpUz1ZPZiGjoTNeXfanHbD4YkGNekcmxWOnw";

    private BoschIoTSuiteOAuthProviderAuthCode sut;

    @Mock
    private BoschIoTSuiteOAuthProviderConfiguration configuration;

    @Mock
    private DefaultUserAccountService userAccountService;

    @Mock
    private UserNamespaceRoleService userNamespaceRoleService;

    @Before
    public void instantiate() {
        sut = new BoschIoTSuiteOAuthProviderAuthCode(CLIENT_ID, publicKey(), configuration, userAccountService, userNamespaceRoleService);
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
