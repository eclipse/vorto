/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
