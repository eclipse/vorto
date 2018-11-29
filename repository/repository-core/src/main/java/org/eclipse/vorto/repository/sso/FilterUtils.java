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
package org.eclipse.vorto.repository.sso;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import com.google.common.base.Strings;

public class FilterUtils {

  private static final String BEARER = "Bearer";
  private static final String AUTHORIZATION = "Authorization";

  public static Optional<String> getBearerToken(HttpServletRequest request) {
    String authToken = request.getHeader(AUTHORIZATION);
    if (!Strings.nullToEmpty(authToken).trim().isEmpty()) {
      String[] tokenComposite = authToken.split(" ");
      if (BEARER.equals(tokenComposite[0]) && !Strings.nullToEmpty(tokenComposite[1]).isEmpty()) {
        return Optional.of(tokenComposite[1]);
      }
    }

    return Optional.empty();
  }

}
