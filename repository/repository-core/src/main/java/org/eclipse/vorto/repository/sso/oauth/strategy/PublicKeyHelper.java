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
package org.eclipse.vorto.repository.sso.oauth.strategy;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.sso.oauth.strategy.model.Keys;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.client.RestTemplate;

public class PublicKeyHelper {

  private RestTemplate restTemplate;

  public static PublicKeyHelper instance(RestTemplate restTemplate) {
    return new PublicKeyHelper(Objects.requireNonNull(restTemplate));
  }

  private PublicKeyHelper(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  private Map<String, CommonKey> getKeys(Keys keys) {
    return Arrays.stream(keys.keys)
        .collect(Collectors.toMap(entry -> entry.kid, entry -> new CommonKey(entry.n, entry.e)));
  }

  public Map<String, PublicKey> getPublicKey(String publicKeyUri) {
    return retrievePublicKeys(publicKeyUri).map(this::getKeys).map(this::keysToPublicKeyMap)
        .orElse(Collections.emptyMap());
  }

  private Map<String, PublicKey> keysToPublicKeyMap(Map<String, CommonKey> keys) {
    return keys.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
      try {
        CommonKey publicKey = entry.getValue();
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(
            new RSAPublicKeySpec(publicKey.getModulus(), publicKey.getPublicExponent()));
      } catch (Exception e) {
        throw new InvalidTokenException("Problem converting the common keys to public keys", e);
      }
    }));
  }

  private Optional<Keys> retrievePublicKeys(String publicKeyUri) {
    return Optional.ofNullable(restTemplate.getForObject(publicKeyUri, Keys.class));
  }

  class CommonKey {
    private BigInteger modulus = null, publicExponent = null;

    public CommonKey(String n, String e) {
      Decoder urlDecoder = Base64.getUrlDecoder();
      modulus = new BigInteger(1, urlDecoder.decode(n));
      publicExponent = new BigInteger(1, urlDecoder.decode(e));
    }

    public BigInteger getModulus() {
      return modulus;
    }

    public BigInteger getPublicExponent() {
      return publicExponent;
    }
  }

  public static PublicKey toPublicKey(String publicKey) {
    try {
      String pemPublicKey =
          String.format("-----BEGIN CERTIFICATE-----\n%s\n-----END CERTIFICATE-----\n", publicKey);
      CertificateFactory factory = CertificateFactory.getInstance("X.509");
      X509Certificate certificate = (X509Certificate) factory
          .generateCertificate(new ByteArrayInputStream(pemPublicKey.getBytes("UTF-8")));
      return certificate.getPublicKey();
    } catch (Exception e) {
      throw new InvalidTokenException(
          "Problem converting the string '" + publicKey + "' to public key", e);
    }
  }

  public static PublicKey toPublicKey(String mod, String exp) {
    try {
      Decoder urlDecoder = Base64.getUrlDecoder();
      BigInteger modulus = new BigInteger(1, urlDecoder.decode(mod));
      BigInteger publicExponent = new BigInteger(1, urlDecoder.decode(exp));

      KeyFactory kf = KeyFactory.getInstance("RSA");
      return kf.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
    } catch (Exception e) {
      throw new InvalidTokenException("Problem converting the common keys to public keys", e);
    }
  }
}
