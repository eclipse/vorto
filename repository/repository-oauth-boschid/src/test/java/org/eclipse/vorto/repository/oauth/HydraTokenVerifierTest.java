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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.internal.JwtToken;
import org.eclipse.vorto.repository.oauth.internal.VerificationHelper;
import org.eclipse.vorto.repository.services.UserBuilder;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HydraTokenVerifierTest extends AbstractVerifierTest {

  private final String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InB1YmxpYzoxYjI2ZDEwYi1iMTZjLTQ4MDQtYmM4MC1jMWUzOWJhNTZlMjAifQ.eyJhdWQiOltdLCJjbGllbnRfaWQiOiJhNzY3MzdhNC0zMTUyLTQ5MzgtYTg4OC0yZTA3ZDQyNzI4NjciLCJleHAiOjQxMjcxNzY5ODYsImV4dCI6e30sImlhdCI6MTYwNDQwMDc4OSwiaXNzIjoiaHR0cHM6Ly9hY2Nlc3MuYm9zY2gtaW90LXN1aXRlLmNvbS92Mi8iLCJqdGkiOiJiMDM1NmY0MS1mZTg1LTQ3YjUtOTE0NC03NTQxMGZmZWY5ZGQiLCJuYmYiOjE2MDQ0MDA3ODksInNjcCI6WyJzZXJ2aWNlOmlvdC1odWItcHJvZDp0NTZmMDcxMjgxMzZlNDE5Y2JiNjg0ZWMzZGQ5ZDA0MDFfaHViL2Z1bGwtYWNjZXNzIiwic2VydmljZTppb3QtdGhpbmdzLWV1LTE6NTZmMDcxMjgtMTM2ZS00MTljLWJiNjgtNGVjM2RkOWQwNDAxX3RoaW5ncy9mdWxsLWFjY2VzcyIsInNlcnZpY2U6dm9ydG8iXSwic3ViIjoiYTc2NzM3YTQtMzE1Mi00OTM4LWE4ODgtMmUwN2Q0MjcyODY3In0.DIhx8eZHExyL2ooZ8JwVc56GshEnfw7MXUv5Y7o_H3UOJaU09yAd4DbIdN2QJi1vz2isSv2gS2Ada5yxCrJQ8W_1rVfdgP3L-_X-clu6skIYX6Dp1gnv5UipzBBj8zAkomdas4b85jbcV6OLvOnCzOpreEiJfCZRCpjMMufg6_Or2Fx-nFG7jJBDv7TR3UcJy0_YlENItkkLjgjjKeTIVNuM5YAF0MxN69gV5dB5j-fwRDmDbcJbZl4WCOf02yBNBaDUgyOzR7DjEiYcbcCprAusH1zIcWySsa9eCD2kqCgQ6ihlJebpUz1ZPZiGjoTNeXfanHbD4YkGNekcmxWOnw";
  private final String expiredToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InB1YmxpYzoxYjI2ZDEwYi1iMTZjLTQ4MDQtYmM4MC1jMWUzOWJhNTZlMjAifQ.eyJhdWQiOltdLCJjbGllbnRfaWQiOiJhNzY3MzdhNC0zMTUyLTQ5MzgtYTg4OC0yZTA3ZDQyNzI4NjciLCJleHAiOjE2MDQ0MDA3ODksImV4dCI6e30sImlhdCI6MTYwNDQwMDc4OSwiaXNzIjoiaHR0cHM6Ly9hY2Nlc3MuYm9zY2gtaW90LXN1aXRlLmNvbS92Mi8iLCJqdGkiOiJiMDM1NmY0MS1mZTg1LTQ3YjUtOTE0NC03NTQxMGZmZWY5ZGQiLCJuYmYiOjE2MDQ0MDA3ODksInNjcCI6WyJzZXJ2aWNlOmlvdC1odWItcHJvZDp0NTZmMDcxMjgxMzZlNDE5Y2JiNjg0ZWMzZGQ5ZDA0MDFfaHViL2Z1bGwtYWNjZXNzIiwic2VydmljZTppb3QtdGhpbmdzLWV1LTE6NTZmMDcxMjgtMTM2ZS00MTljLWJiNjgtNGVjM2RkOWQwNDAxX3RoaW5ncy9mdWxsLWFjY2VzcyIsInNlcnZpY2U6dm9ydG8iXSwic3ViIjoiYTc2NzM3YTQtMzE1Mi00OTM4LWE4ODgtMmUwN2Q0MjcyODY3In0.gjWgQbwfcZjJNo4N9XSI3uvp6_H-0bDrT10UmkSxCWRV_hxwdna7EJ0cDKhIidKtKtbel6ya3rhNmB15kd7PZlVTBe9wUVc6ZfhEhJjc_r3t0rrtIUWx-hnPA7QSK6_jhRMJuLzDRT5cw51Xy8EvQLGEfXJDp-h2pund5ol20o1eklGM9FV8VKbPhlAIqJT_USIu6xuJYNsNTVaWGCcg3miubsWvkqgvNu3cpmzfcGX-pH3Hl9UskdaqXjc_RdwYOxBVatIYeY50DE0GadqWbkXGn6IdLt6FLHj1D3OVRssASpk2JNqkjpqPO9fss8Ve9nCmVkUYMmJK-YHmMPY7qQ";

  @Mock
  private BoschIoTSuiteOAuthProviderConfiguration configuration;

  private BoschIoTSuiteOAuthProviderAuthCode getVerifier() throws InvalidUserException {
    User user = new UserBuilder().withName("d758a35e-94ef-443f-9625-7f03092e2005").withAuthenticationProviderID("GITHUB").build();
    DefaultUserAccountService userAccountService = Mockito.mock(DefaultUserAccountService.class);
    when(userAccountService.getUser("d758a35e-94ef-443f-9625-7f03092e2005")).thenReturn(user);

    UserNamespaceRoleService userNamespaceRoleService = Mockito
        .mock(UserNamespaceRoleService.class);

    return new BoschIoTSuiteOAuthProviderAuthCode("", publicKey(), configuration, userAccountService,
        userNamespaceRoleService);
  }

  @Test
  public void verifyJwtToken() {
    assertTrue(VerificationHelper
        .verifyJwtToken(publicKey().get().get("public:1b26d10b-b16c-4804-bc80-c1e39ba56e20"),
            JwtToken.instance(jwtToken).get()));
  }

  @Test
  public void verifyValid() throws Exception {
    assertTrue(getVerifier().verify(requestModel("vorto.private.erle:Datatype1:1.0.0"),
        JwtToken.instance(jwtToken).get()));
  }

  @Test
  public void verifyExpired() throws Exception {
    assertFalse(getVerifier().verify(requestModel("vorto.private.erle:Datatype1:1.0.0"),
        JwtToken.instance(expiredToken).get()));
  }

}
