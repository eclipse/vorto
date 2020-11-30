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

import java.util.Optional;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.UserBuilder;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BoschIoTSuiteOAuthProviderAuthCodeTest extends AbstractVerifierTest {

    public static final String CLIENT_ID = "client-id";
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6InB1YmxpYzoxYjI2ZDEwYi1iMTZjLTQ4MDQtYmM4MC1jMWUzOWJhNTZlMjAiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJmNjAyNGYzMy03NzQwLTQ0MmUtYWVjMC1lNTU4MzI4OTA2OTAiLCJpYXQiOjQxMjcxNzY5ODYsImV4cCI6NDEyNzE3Njk4NiwibmJmIjo0MTI3MTc2OTg2LCJpc3MiOiJodHRwczovL2FjY2Vzcy5ib3NjaC1pb3Qtc3VpdGUuY29tL3YyIiwiYXVkIjpbXSwiY2xpZW50X2lkIjoiZDc1OGEzNWUtOTRlZi00NDNmLTk2MjUtN2YwMzA5MmUyMDA1Iiwic2NwIjpbInNlcnZpY2U6aW90LWh1Yjp0ZTE3YjcyMDIwMDQ3NDRmM2E4ZWQ3ZDM2OWFkMTYyNTFfaHViL2Z1bGwtYWNjZXNzIiwic2VydmljZTppb3QtdGhpbmdzLWV1LTE6ZTE3YjcyMDItMDA0Ny00NGYzLWE4ZWQtN2QzNjlhZDE2MjUxX3RoaW5ncy9mdWxsLWFjY2VzcyIsInNlcnZpY2U6dm9ydG86dm9ydG8ucHJpdmF0ZS5lcmxlL2Z1bGwtYWNjZXNzIl0sInN1YiI6ImQ3NThhMzVlLTk0ZWYtNDQzZi05NjI1LTdmMDMwOTJlMjAwNSIsImV4dCI6e319.azKTuhvFXKDDKGvNYyzVEa8Li_YfGD4-Yv-v5YBGRB3IU0b5yGNFwJcC23FF_mL2h_6Xr4_eJ-duQ3MGJkJk9ADXeYz0izW06CuZQtiFW3joSKKN2xyyVFXy7jpk4CoPTJruQphhAHpfQH2tBiFbQZlsCqE14RPOlrqbxlORTwhOsHVQLBfRssmA1AIlvfstfbBpxKmooqwmqBoDfU-5Kmei6JJWOBos7aoTZGD-9bUbJFWUVXEGyr0VXEyr5IriAjOyY_wba7ktv89JVe4SFqIx3ueSo1vyMbulgK8r5xbt8ZkGGCPOzG6Y2t5Zmd8stTI2WXuf2TgBIoSGIhBjegHT0RGijAajdXdrB8aqTBbJjUU0hTQAfcELHnWOs7BfS6PYPa0Hfg7VqhUARV8t7BsYI6gnE7b24w_1gmb5aOGWq6WcuQ2FsE5ucNbPcYr87fT77JKEaWIXHo_NFG16UGtEC0mvSaniVKoV-KQRZzNzvJQ9r2CINzq0W_XnD1fYBb_rcO3xjILNHZXelgrcWcuOErU95oa0sqWwqwLBg4lcHVrPs-u-Y_rma7WNNdBJ3vDLVdXKXdxyusZzR2Jsjn1V1DHvKAfdu5H2le-WmA-snYyCv35TsVYqEQhwrn8L5xu7F2SkrNPjWXIp74VqQWN7gBe_LWHht4zT_afo-Iw";

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
        when(userAccountService.getUser((IUserContext)any())).thenReturn(null);
        when(userAccountService.getUser((Authentication)any())).thenReturn(null);
        assertNull(sut.authenticate(null, TOKEN));
    }

    @Test
    public void authenticateInvalidTokenTest() {
       Authentication authentication = sut.authenticate(null, "invalid-token");
       assertNull(authentication);
    }

    @Test
    public void authenticateSuccessTest() {
        when(
            userAccountService.getUser(UserDto.of(anyString(), BoschIDOAuthProvider.ID))
        )
            .thenAnswer(a ->
                new UserBuilder()
                    .withName(a.getArguments()[0].toString())
                    .withAuthenticationProviderID(BoschIDOAuthProvider.ID)
                .build()
            );
        Authentication authentication = sut.authenticate(null, TOKEN);
        assertTrue(authentication.isAuthenticated());
    }

}
