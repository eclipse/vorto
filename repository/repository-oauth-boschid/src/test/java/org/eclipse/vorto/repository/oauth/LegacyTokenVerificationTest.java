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
package org.eclipse.vorto.repository.oauth;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.AuthenticationProvider;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.internal.JwtToken;
import org.junit.Test;
import org.mockito.Mockito;

public class LegacyTokenVerificationTest extends AbstractVerifierTest {

  private String jwtToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6InB1YmxpYzoxYjI2ZDEwYi1iMTZjLTQ4MDQtYmM4MC1jMWUzOWJhNTZlMjAiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJmNjAyNGYzMy03NzQwLTQ0MmUtYWVjMC1lNTU4MzI4OTA2OTAiLCJpYXQiOjQxMjcyNjQ0MzQsImV4cCI6NDEyNzI2NDQzNCwibmJmIjo0MTI3MjY0NDM0LCJpc3MiOiJodHRwczovL2FjY2Vzcy5ib3NjaC1pb3Qtc3VpdGUuY29tL2F1dGgvcmVhbG1zL2lvdC1zdWl0ZSIsImF1ZCI6W10sImF6cCI6ImQ3NThhMzVlLTk0ZWYtNDQzZi05NjI1LTdmMDMwOTJlMjAwNSIsInJlc291cmNlX2FjY2VzcyI6eyJzZXJ2aWNlOmlvdC10aGluZ3MtZXUtMTplMTdiNzIwMi0wMDQ3LTQ0ZjMtYThlZC03ZDM2OWFkMTYyNTFfdGhpbmdzIjp7InJvbGVzIjpbImZ1bGwtYWNjZXNzIl19LCJzZXJ2aWNlOmlvdC1odWI6dGUxN2I3MjAyMDA0NzQ0ZjNhOGVkN2QzNjlhZDE2MjUxX2h1YiI6eyJyb2xlcyI6WyJmdWxsLWFjY2VzcyJdfSwic2VydmljZTp2b3J0bzp2b3J0by5wcml2YXRlLmVybGUiOnsicm9sZXMiOlsiZnVsbC1hY2Nlc3MiXX19LCJzdWIiOiJkNzU4YTM1ZS05NGVmLTQ0M2YtOTYyNS03ZjAzMDkyZTIwMDUiLCJleHQiOnt9fQ.obPWi0oMpOD6924-HJ9jrf1y5XsxBpc2cZMXSBZUyjZdsU1kLyXcT15D1SU3MxZ7gRSh9tdTA3VXbeCFqpIfEgZJdUPM4kbb1TiNsik-rr9p4XVrtutZzeRqV8aUzvcEzCv7L9Vm3B_YYuBmiDsWscE5sNtPn0GO1DKa8rcU0AtEs0QytDe70EZDnejs_E-5imnv4s2oXSucqeTJ4eATDCI2gvrafdOMwFfVlNCst3tmDa38PVQz4ptKH_Oho6NWhvkKa9kq_Cgkdk8gwbyU4Hc2KtuDrQ7KAEIsoyxR3qQ9IcwRVNp-kgwqtwEulXaEkbhXWumo3iultaKZ9xZrSC1YfJBhwq-uEpJ_ayVkFBIaoY-EfE8S9Arp12jErw6CpaqE-ALcqT_KHS2JYSINJ0Go1nc4JKAF1bv7PhU_0XG8KyeYUQP3Z86mbMCSWydqlzu3I0cDUbBy8-Q0wtf_EK7oo3BLgpyHdjsGlLiLLUNeyZx9fgozUTw9Rp6nB3tEV-XKo0MjG3UPrC19kYe_io73LW--te0K6cCb2JKSR_DezYjmATJYU-DIGoHpkAbldgjkjewpg_ECExxlnNxq3NvWbwUFL9mCQhXWTTETNfdpMuDxcyY3NJvxIePEI9U0jL0LqfBF_oPhS_Z1BBlda6audjGf39DibQkUsx-42-k";
  
  private BoschIoTSuiteOAuthProviderV1 getVerifier() {
    Tenant tenant = new Tenant("test");
    tenant.setNamespaces(Namespace.toNamespace(Arrays.asList("vorto.private.erle"), tenant).stream().collect(Collectors.toSet()));
    User user = User.create("d758a35e-94ef-443f-9625-7f03092e2005", AuthenticationProvider.GITHUB.name(), null, tenant, Role.USER);
    
    IUserAccountService userAccountService = Mockito.mock(IUserAccountService.class);
    Mockito.when(userAccountService.getUser("d758a35e-94ef-443f-9625-7f03092e2005")).thenReturn(user);
    
    return new BoschIoTSuiteOAuthProviderV1(publicKey(), userAccountService);
  }
  
  @Test
  public void testValid() {
    assertTrue(getVerifier().verify(requestModel("vorto.private.erle:Datatype1:1.0.0"), JwtToken.instance(jwtToken).get()));
  }
  
  @Test
  public void testNoNamespaceAccess() {
    assertFalse(getVerifier().verify(requestModel("vorto.private.someoneelse:Datatype1:1.0.0"), JwtToken.instance(jwtToken).get()));
  }
}
