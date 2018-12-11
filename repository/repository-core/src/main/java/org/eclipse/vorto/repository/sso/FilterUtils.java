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
