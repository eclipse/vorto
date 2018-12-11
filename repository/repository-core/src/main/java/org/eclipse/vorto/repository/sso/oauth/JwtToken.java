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
package org.eclipse.vorto.repository.sso.oauth;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JwtToken {
  private String header;
  private String payload;
  private String signature;

  public static Optional<JwtToken> instance(String jwtToken) {
    if (jwtToken == null) {
      return Optional.empty();
    }

    String[] jwtParts = jwtToken.split("\\.");
    if (jwtParts.length != 3) {
      return Optional.empty();
    }

    return Optional.of(new JwtToken(jwtParts[0], jwtParts[1], jwtParts[2]));
  }

  private JwtToken(String header, String payload, String signature) {
    this.header = header;
    this.payload = payload;
    this.signature = signature;
  }

  public String getHeader() {
    return header;
  }

  public String getHeaderAndPayload() {
    return header + "." + payload;
  }

  public Map<String, Object> getHeaderMap() {
    Type type = new TypeToken<Map<String, Object>>() {}.getType();
    return new Gson().fromJson(new String(Base64.getUrlDecoder().decode(header)), type);
  }

  public String getJwtToken() {
    return header + "." + payload + "." + signature;
  }

  public String getPayload() {
    return payload;
  }

  public Map<String, Object> getPayloadMap() {
    Type type = new TypeToken<Map<String, Object>>() {}.getType();
    return new Gson().fromJson(new String(Base64.getUrlDecoder().decode(payload)), type);
  }

  public String getSignature() {
    return signature;
  }

  @Override
  public String toString() {
    return "JwtToken [header=" + header + ",\npayload=" + payload + ",\nsignature=" + signature
        + "]";
  }
}
