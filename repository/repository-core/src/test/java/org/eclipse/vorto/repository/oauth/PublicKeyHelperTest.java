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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.eclipse.vorto.repository.sso.oauth.strategy.PublicKeyHelper;
import org.eclipse.vorto.repository.sso.oauth.strategy.model.Key;
import org.eclipse.vorto.repository.sso.oauth.strategy.model.Keys;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

public class PublicKeyHelperTest {

  public static final String KEYCLOAK_PUBLIC_KEY_URI = "pkUri2";
  public static final String CIAM_PUBLIC_KEY_URI = "pkUri1";

  @Test
  public void testPublicKeyVerifier() {
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    Mockito.when(restTemplate.getForObject(CIAM_PUBLIC_KEY_URI, Keys.class))
        .thenReturn(getCiamKeys());

    Mockito.when(restTemplate.getForObject(KEYCLOAK_PUBLIC_KEY_URI, Keys.class))
        .thenReturn(getKeyCloakKeys());

    PublicKeyHelper helper = PublicKeyHelper.instance(restTemplate);

    assertFalse(helper.getPublicKey(CIAM_PUBLIC_KEY_URI).isEmpty());
    assertEquals(helper.getPublicKey(CIAM_PUBLIC_KEY_URI).size(), 2);
    assertFalse(helper.getPublicKey(KEYCLOAK_PUBLIC_KEY_URI).isEmpty());
    assertEquals(helper.getPublicKey(KEYCLOAK_PUBLIC_KEY_URI).size(), 1);
  }

  public static Keys getCiamKeys() {
    Keys keys = new Keys();
    Key key = new Key();
    key.kid = "7ZnVKaYt4jhwXQn4Ri_tXltE8lk";
    key.n =
        "tf_vkyj_43dAhkt-xWRg8BkydMg8mO73doOY8E09Khup4CjIJazcML0abPArriZ8LsQr2EQrIxazUrwT2P8NWAyorbcAyauAGc27tNM3JikV7JNUWArP3ZIJsd8LNLVIejG4syDs9rKSXJC78txsd2gEJ0lUAd51sNNqj-o2cf6kxxdnFpH5SDmqP9b5s7ncmydA7CtToTe4HCMPHcmCGsUT63zVNMtL-IT8LDf4BrrOtPdL2vzfJUcbQnsOUxSyUEDTIt9ZZte5QqK9MkZDZ5ekjCD2uL_fy8GuUfjaJnAh71lZqOPf9bHbQU2JX0CtTUQ4evgxnW6LloE_8wL_idO3wbHpAA8tgxDDU-84yo52o5wTVVlzEQLQ8ZOoziXL97o8tERWWcVYhFtSDLMVm-X2tntyARAZmMNyenNZKjwRzOL5euiScwMuuJXX4ggsQbJhBgQmEp0AKJg4zCXnGH6WCwRoUzrL1aqI54vCIPatlJr5nDqwOKsS1hY3lLlbRXBqJTgzMnGNXaIZunXFryHKPzABQyENZWBNNUHg7bivBLPXK27twbcFtUCuRxY4Wgrtsh09_CjnZv0lyGJCs9562kaWgpRScIcKlk5Ojm2Th9fEYRbSe-16nfhHqevkO2zCBSrJTql7NgFEXBIDUpBgIXfXiTfcMpJDvGF7Kn0";
    key.e = "AQAB";
    Key key2 = new Key();
    key2.kid = "Qpro2ZUNBIACA-ZloCD59RUwh3k";
    key2.n =
        "jpnoB6yOx_tMqRU5eeVvIoH_fR5ExDX3T3agje7JwJiD9kxi1KI6ZbfKcomXovPxf0Iyvc44KkE3g5uas-T1VR6jtPFOnOq3RUptGoU65_z0rG2P0y_mnSVCvogVtx-6UtiRMGq6i6YJr8kdAuPLYufza6vssC4RwwMKRlWX6YQ0-v__LfpdPAvVwVAilyL2ecc-0WnO4xwdxr3NQog9vbZfTMkTT2nT36zBX3K8PqivXE7AmPRxH2UdFzClzwW7icMnS043mIKYO7PrgDoErSMz3KsLjISCzAShD-DbGACfoMKe2tu9SZNH3EevUEuP6BGN_aDez8NI1EPiBuL_hw";
    key2.e = "AQAB";
    keys.keys = new Key[] {key, key2};
    return keys;
  }

  public static Keys getKeyCloakKeys() {
    Keys keys = new Keys();
    Key key = new Key();
    key.kid = "IdoGf9WlqIJQ9Fb0agZKPp3EDd8fJuWX_8EqBlLiMbw";
    key.n =
        "wNNVdlmaHYYLbwH8wx6SZwTO6M0aGbRNlpyWIT552B1_WEm_AKDTyI00v0wtOXqgX10lJbDu8F4g0biHmZuoTBZaB5SXvj2UpEYO_rUDZs-nOFRqTxujtvOnnV60cMO91Z94iIOSCBTOlRC9p8twTJBaRYUC3uBOUDKJB5vAmAUNMmpba1DhD1qYC8CWK__R0SfGA0zzPVgg0FDO8WgYiwgBb05NgTLyShICv35YSgw4R9C0f9Cb-i5sEJ21zJZ8plAD-mx7dddiepUJeRSMELqOOkjT43Moq-ElXQCgi54visWcdgLAAMFXuTr5UBXURO5D1gaoB2PvKCQxSa0QLQ";
    key.e = "AQAB";
    keys.keys = new Key[] {key};
    return keys;
  }

}
