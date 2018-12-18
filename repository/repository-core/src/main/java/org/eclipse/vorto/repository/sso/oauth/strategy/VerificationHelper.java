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

import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.Objects;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;

public class VerificationHelper {

  public static boolean verifyJwtToken(PublicKey publicKey, JwtToken token) {
    Objects.requireNonNull(publicKey, "No public key.");
    Objects.requireNonNull(token.getHeaderAndPayload(), "No payload.");
    Objects.requireNonNull(token.getSignature(), "No signature.");

    try {
      Signature publicSignature = Signature.getInstance("SHA256withRSA");
      publicSignature.initVerify(publicKey);
      publicSignature.update(token.getHeaderAndPayload().getBytes());

      byte[] decodedSignature = Base64.getUrlDecoder().decode(token.getSignature().getBytes());

      return publicSignature.verify(decodedSignature);
    } catch (Exception e) {
      return false;
    }
  }

}
