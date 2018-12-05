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
import java.security.PublicKey;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.eclipse.vorto.repository.sso.oauth.strategy.PublicKeyHelper;
import org.eclipse.vorto.repository.sso.oauth.strategy.VerificationHelper;
import org.junit.Test;

public class VerificationHelperTest {

  private JwtToken jwtToken1() {
    String jwtToken =
        "eyJhbGciOiJSUzI1NiIsImtpZCIgOiAiSWRvR2Y5V2xxSUpROUZiMGFnWktQcDNFRGQ4Zkp1V1hfOEVxQmxMaU1idyJ9.eyJqdGkiOiI0NGM0NTgxMC05YzA0LTRiZGMtYTNmOS00NmEwOWM1ZGU0YWQiLCJleHAiOjAsIm5iZiI6MCwiaWF0IjoxNTM1OTU4MzUwLCJpc3MiOiJodHRwczovL2F1dGguYm9zY2gtaW90LXN1aXRlLmNvbS9hdXRoL3JlYWxtcy9CQ0kiLCJhdWQiOiJodHRwczovL2F1dGguYm9zY2gtaW90LXN1aXRlLmNvbS9hdXRoL3JlYWxtcy9CQ0kiLCJ0eXAiOiJSZWdpc3RyYXRpb25BY2Nlc3NUb2tlbiIsInJlZ2lzdHJhdGlvbl9hdXRoIjoiYXV0aGVudGljYXRlZCJ9.LekU6y2noz8DUQwi0PSSl2PRiwVpyWdU8UJdaj_X-gAyj1q_JSW44KTPRpyo5NAMa6laX5hO7kpxSGJRMB-SwwMWVpqZvzBRwp7p6SphGLa1cq4gNjVXQOQhThyYzlHnFfWyKk1xYyzc9XN6H5dBfkuClOyWIwl_ou7DjpD-HOw6Uv8hIqB8EtTIH9e-OuUxC9rJa8BTSblYeqHIMJkGT2tcVPESWI0IJV7luWGfYSVjKhVCyj9fLJz14LVb4wrDX_AbrlgHBCWVrOjMBV_ovmG2SkSCZoS8s8Vqc6-l_30YvV8EpW5kynC5Rdy_WFGkCeE7iRTCYf1tknUFM65PzA";
    return JwtToken.instance(jwtToken).get();
  }

  private JwtToken jwtToken2() {
    String jwtToken2 =
        "eyJhbGciOiJSUzI1NiIsImtpZCIgOiAiSWRvR2Y5V2xxSUpROUZiMGFnWktQcDNFRGQ4Zkp1V1hfOEVxQmxMaU1idyJ9.eyJqdGkiOiI0NGM0NTgxMC05YzA0LTRiZGMtYTNmOS00NmEwOWM1ZGU0YWQiLCJleHAiOjAsIm5iZiI6MCwiaWF0IjoxNTM1OTU4MzUwLCJpc3MiOiJodHRwczovL2F1dGguYm9zY2gtaW90LXN1aXRlLmNvbS9hdXRoL3JlYWxtcy9CQ0kiLCJhdWQiOiJodHRwczovL2F1dGguYm9zY2gtaW90LXN1aXRlLmNvbS9hdXRoL3JlYWxtcy9CQ0kiLCJ0eXAiOiJSZWdpc3RyYXRpb25BY2Nlc3NUb2tlbiIsInJlZ2lzdHJhdGlvbl9hdXRoIjoiYXV0aGVudGljYXRlZCJ9.LekU6y2noz8DUQwi0PSSl2PRiwVpyWdU8UJdaj_X-gAyj1q_JSW44KTPRpyo5NAMa6laX5hO7kpxSGJRMB-SwwMWVpqZvzBRwp7p6SphGLa1cq4gNjVXQOQhThyYzlHnFfWyKk1xYyzc9XN6H5dBfkuClOyWIwl_ou7DjpD-HOw6Uv8hIqB8EtTIH9e-OuUxC9rJa8BTSblYeqHIMJkGT2tcVPESWI0IJV7luWGfYSVjKhVCyj9fLJz14LVb4wrDX_AbrlgHBCWVrOjMBV_ovmG2SkSCZoS8s8Vqc6-l_30YvV8EpW5kynC5Rdy_WFGkCeE7iRTCYf1knUFM65PzA";
    return JwtToken.instance(jwtToken2).get();
  }

  private PublicKey keycloakPublicKey() {
    String publicKeyMod =
        "wNNVdlmaHYYLbwH8wx6SZwTO6M0aGbRNlpyWIT552B1_WEm_AKDTyI00v0wtOXqgX10lJbDu8F4g0biHmZuoTBZaB5SXvj2UpEYO_rUDZs-nOFRqTxujtvOnnV60cMO91Z94iIOSCBTOlRC9p8twTJBaRYUC3uBOUDKJB5vAmAUNMmpba1DhD1qYC8CWK__R0SfGA0zzPVgg0FDO8WgYiwgBb05NgTLyShICv35YSgw4R9C0f9Cb-i5sEJ21zJZ8plAD-mx7dddiepUJeRSMELqOOkjT43Moq-ElXQCgi54visWcdgLAAMFXuTr5UBXURO5D1gaoB2PvKCQxSa0QLQ";
    String publicKeyExp = "AQAB";

    return PublicKeyHelper.toPublicKey(publicKeyMod, publicKeyExp);
  }

  private PublicKey publicKey2() {
    String publicKeyMod2 =
        "tf_vkyj_43dAhkt-xWRg8BkydMg8mO73doOY8E09Khup4CjIJazcML0abPArriZ8LsQr2EQrIxazUrwT2P8NWAyorbcAyauAGc27tNM3JikV7JNUWArP3ZIJsd8LNLVIejG4syDs9rKSXJC78txsd2gEJ0lUAd51sNNqj-o2cf6kxxdnFpH5SDmqP9b5s7ncmydA7CtToTe4HCMPHcmCGsUT63zVNMtL-IT8LDf4BrrOtPdL2vzfJUcbQnsOUxSyUEDTIt9ZZte5QqK9MkZDZ5ekjCD2uL_fy8GuUfjaJnAh71lZqOPf9bHbQU2JX0CtTUQ4evgxnW6LloE_8wL_idO3wbHpAA8tgxDDU-84yo52o5wTVVlzEQLQ8ZOoziXL97o8tERWWcVYhFtSDLMVm-X2tntyARAZmMNyenNZKjwRzOL5euiScwMuuJXX4ggsQbJhBgQmEp0AKJg4zCXnGH6WCwRoUzrL1aqI54vCIPatlJr5nDqwOKsS1hY3lLlbRXBqJTgzMnGNXaIZunXFryHKPzABQyENZWBNNUHg7bivBLPXK27twbcFtUCuRxY4Wgrtsh09_CjnZv0lyGJCs9562kaWgpRScIcKlk5Ojm2Th9fEYRbSe-16nfhHqevkO2zCBSrJTql7NgFEXBIDUpBgIXfXiTfcMpJDvGF7Kn0";
    String publicKeyExp2 = "AQAB";
    return PublicKeyHelper.toPublicKey(publicKeyMod2, publicKeyExp2);
  }

  @Test
  public void testCorrectTokenCorrectPublicKeyVerification() {
    assertTrue(VerificationHelper.verifyJwtToken(keycloakPublicKey(), jwtToken1()));
  }

  @Test
  public void testMalformedTokenCorrectPublicKey() {
    assertFalse(VerificationHelper.verifyJwtToken(keycloakPublicKey(), jwtToken2()));
  }

  @Test
  public void testCorrectTokenWrongPublicKey() {
    assertFalse(VerificationHelper.verifyJwtToken(publicKey2(), jwtToken1()));
  }

}
