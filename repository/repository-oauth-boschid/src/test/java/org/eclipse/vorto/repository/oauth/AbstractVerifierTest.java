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

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.mockito.Mockito;
import sun.misc.BASE64Decoder;

public class AbstractVerifierTest {
  
  protected Supplier<Map<String, PublicKey>> publicKey() {
    return () -> {
      Map<String, PublicKey> pubKey = new HashMap<>();
      pubKey.put("public:1b26d10b-b16c-4804-bc80-c1e39ba56e20",
          createPublicKey());
      return pubKey;
    };
  }

  private PublicKey createPublicKey() {
    try {
      String publicKeyPEM = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzyis1ZjfNB0bBgKFMSv\n"
          + "vkTtwlvBsaJq7S5wA+kzeVOVpVWwkWdVha4s38XM/pa/yr47av7+z3VTmvDRyAHc\n"
          + "aT92whREFpLv9cj5lTeJSibyr/Mrm/YtjCZVWgaOYIhwrXwKLqPr/11inWsAkfIy\n"
          + "tvHWTxZYEcXLgAXFuUuaS3uF9gEiNQwzGTU1v0FqkqTBr4B8nW3HCN47XUu0t8Y0\n"
          + "e+lf4s4OxQawWD79J9/5d3Ry0vbV3Am1FtGJiJvOwRsIfVChDpYStTcHTCMqtvWb\n"
          + "V6L11BWkpzGXSW4Hv43qa+GSYOD2QU68Mb59oSk2OB+BtOLpJofmbGEGgvmwyCI9\n"
          + "MwIDAQAB";

      BASE64Decoder base64Decoder = new BASE64Decoder();
      byte[] publicKeyBytes = base64Decoder.decodeBuffer(publicKeyPEM);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(keySpec);
    } catch (Exception e) {
      throw new IllegalStateException("Public key creation failed.");
    }
  }

  protected HttpServletRequest requestModel(String modelId) {
    HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
    Mockito.when(httpRequest.getRequestURI()).thenReturn("/infomodelrepository/api/v1/models/" + modelId);
    Mockito.when(httpRequest.getContextPath()).thenReturn("/infomodelrepository");
    Mockito.when(httpRequest.getMethod()).thenReturn("GET");
    return httpRequest;
  }
  
}
