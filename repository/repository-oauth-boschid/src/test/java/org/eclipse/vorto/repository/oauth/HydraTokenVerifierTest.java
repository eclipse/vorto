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

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.internal.JwtToken;
import org.eclipse.vorto.repository.oauth.internal.VerificationHelper;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class HydraTokenVerifierTest extends AbstractVerifierTest {

  private final String jwtToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6InB1YmxpYzoxYjI2ZDEwYi1iMTZjLTQ4MDQtYmM4MC1jMWUzOWJhNTZlMjAiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJmNjAyNGYzMy03NzQwLTQ0MmUtYWVjMC1lNTU4MzI4OTA2OTAiLCJpYXQiOjQxMjcxNzY5ODYsImV4cCI6NDEyNzE3Njk4NiwibmJmIjo0MTI3MTc2OTg2LCJpc3MiOiJodHRwczovL2FjY2Vzcy5ib3NjaC1pb3Qtc3VpdGUuY29tL3YyIiwiYXVkIjpbXSwiY2xpZW50X2lkIjoiZDc1OGEzNWUtOTRlZi00NDNmLTk2MjUtN2YwMzA5MmUyMDA1Iiwic2NwIjpbInNlcnZpY2U6aW90LWh1Yjp0ZTE3YjcyMDIwMDQ3NDRmM2E4ZWQ3ZDM2OWFkMTYyNTFfaHViL2Z1bGwtYWNjZXNzIiwic2VydmljZTppb3QtdGhpbmdzLWV1LTE6ZTE3YjcyMDItMDA0Ny00NGYzLWE4ZWQtN2QzNjlhZDE2MjUxX3RoaW5ncy9mdWxsLWFjY2VzcyIsInNlcnZpY2U6dm9ydG86dm9ydG8ucHJpdmF0ZS5lcmxlL2Z1bGwtYWNjZXNzIl0sInN1YiI6ImQ3NThhMzVlLTk0ZWYtNDQzZi05NjI1LTdmMDMwOTJlMjAwNSIsImV4dCI6e319.azKTuhvFXKDDKGvNYyzVEa8Li_YfGD4-Yv-v5YBGRB3IU0b5yGNFwJcC23FF_mL2h_6Xr4_eJ-duQ3MGJkJk9ADXeYz0izW06CuZQtiFW3joSKKN2xyyVFXy7jpk4CoPTJruQphhAHpfQH2tBiFbQZlsCqE14RPOlrqbxlORTwhOsHVQLBfRssmA1AIlvfstfbBpxKmooqwmqBoDfU-5Kmei6JJWOBos7aoTZGD-9bUbJFWUVXEGyr0VXEyr5IriAjOyY_wba7ktv89JVe4SFqIx3ueSo1vyMbulgK8r5xbt8ZkGGCPOzG6Y2t5Zmd8stTI2WXuf2TgBIoSGIhBjegHT0RGijAajdXdrB8aqTBbJjUU0hTQAfcELHnWOs7BfS6PYPa0Hfg7VqhUARV8t7BsYI6gnE7b24w_1gmb5aOGWq6WcuQ2FsE5ucNbPcYr87fT77JKEaWIXHo_NFG16UGtEC0mvSaniVKoV-KQRZzNzvJQ9r2CINzq0W_XnD1fYBb_rcO3xjILNHZXelgrcWcuOErU95oa0sqWwqwLBg4lcHVrPs-u-Y_rma7WNNdBJ3vDLVdXKXdxyusZzR2Jsjn1V1DHvKAfdu5H2le-WmA-snYyCv35TsVYqEQhwrn8L5xu7F2SkrNPjWXIp74VqQWN7gBe_LWHht4zT_afo-Iw";
  private final String expiredToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6InB1YmxpYzoxYjI2ZDEwYi1iMTZjLTQ4MDQtYmM4MC1jMWUzOWJhNTZlMjAiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJmNjAyNGYzMy03NzQwLTQ0MmUtYWVjMC1lNTU4MzI4OTA2OTAiLCJpYXQiOjE1Mzk1ODQ0MzQsImV4cCI6MTUzOTU4NDQzNCwibmJmIjoxNTM5NTg0NDM0LCJpc3MiOiJodHRwczovL2FjY2Vzcy5ib3NjaC1pb3Qtc3VpdGUuY29tL3YyIiwiYXVkIjpbXSwiY2xpZW50X2lkIjoiZDc1OGEzNWUtOTRlZi00NDNmLTk2MjUtN2YwMzA5MmUyMDA1Iiwic2NwIjpbInNlcnZpY2U6aW90LWh1Yjp0ZTE3YjcyMDIwMDQ3NDRmM2E4ZWQ3ZDM2OWFkMTYyNTFfaHViL2Z1bGwtYWNjZXNzIiwic2VydmljZTppb3QtdGhpbmdzLWV1LTE6ZTE3YjcyMDItMDA0Ny00NGYzLWE4ZWQtN2QzNjlhZDE2MjUxX3RoaW5ncy9mdWxsLWFjY2VzcyIsInNlcnZpY2U6dm9ydG86dm9ydG8ucHJpdmF0ZS5lcmxlL2Z1bGwtYWNjZXNzIl0sInN1YiI6ImQ3NThhMzVlLTk0ZWYtNDQzZi05NjI1LTdmMDMwOTJlMjAwNSIsImV4dCI6e319.OMVa0fWqs6lo2Qsa48gscA69jn8-A9I3tSj5yiHwccIT7Xz7xYKL-EnZ4mTlAyWmXhw_J9KWyIUUrKbkGOyiGXbVjAfUxb-lltaTJMLFr4yZHxE8KOzPxBgtoqC__ZO9JwmGfnO897cblQuRqEwGF2btf-wSixXwIeJgqpkMiNvzjpaWHLfTpi6JskW50dKsCtObclxZhr3D8C0rjgom56kx6xrC-FYkIxM4XJ5FFEwzy8olZvPBPyiOmowWyA6qWAyH7i9CzT6QU71ppKYyh1xNQyqlj1TBNJxuOCwefFLcDWkgoW7YAsU_8PzWg0ST_Ruw0zUwPmbMidY_wnQabcaUrSGHrv8T2IgbIjClWDZwR-byU4nuplCcRVBpot6IKoOH8SrNDuF60Pw6GZntEeMPXSXaA3Q5V2zqCQ1tujyV0YTJXRb_iipF4Smvf7Nh8eO8wyECixtTleyqSaYscqBuiFtDyTTHNRpgxMAR-tcHQTf-VeKxB7i1fGJnHW1ztnS82zEyJxNb0OCDTFOAzvX_GFu77abVI3coX7NF1EZCRivB50JxTbpcOAFEur1XWEdO0ZlsUjzmHo2iHuXrREAs7b2GZV6FY33HXiUQlEFOHpGD4JI9nrq0xNh8UdynuMEIV34g9d6eNQr5-J4eTQeMGZH2xJMW4jjJsQCAQ1s";
  
  private BoschIoTSuiteOAuthProviderV2 getVerifier() {
    Tenant tenant = new Tenant("test");
    tenant.setNamespaces(
            Namespace.toNamespace(Arrays.asList("vorto.private.erle"), tenant).stream().collect(Collectors.toSet()));
    User user = User.create("d758a35e-94ef-443f-9625-7f03092e2005", "GITHUB", null, tenant, Role.USER);
    List<Tenant> tenantList = new ArrayList<>();
    tenantList.add(tenant);
    IUserAccountService userAccountService = Mockito.mock(IUserAccountService.class);
    when(userAccountService.getUser("d758a35e-94ef-443f-9625-7f03092e2005")).thenReturn(user);
    when(userAccountService.getTenants(user)).thenReturn(tenantList);

    UserNamespaceRoleService userNamespaceRoleService = Mockito.mock(UserNamespaceRoleService.class);

    return new BoschIoTSuiteOAuthProviderV2(publicKey(), userAccountService, userNamespaceRoleService);
  }
  
  @Test
  public void verifyJwtToken() {
    assertTrue(VerificationHelper.verifyJwtToken(publicKey().get().get("public:1b26d10b-b16c-4804-bc80-c1e39ba56e20"), 
        JwtToken.instance(jwtToken).get()));
  }
  
  @Test
  public void verifyValid() {
    assertTrue(getVerifier().verify(requestModel("vorto.private.erle:Datatype1:1.0.0"), JwtToken.instance(jwtToken).get()));
  }
  
  @Test
  public void verifyExpired() {
    assertFalse(getVerifier().verify(requestModel("vorto.private.erle:Datatype1:1.0.0"), JwtToken.instance(expiredToken).get()));
  }
  
}
