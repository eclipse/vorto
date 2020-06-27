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
package org.eclipse.vorto.repository.core;

import org.springframework.security.core.Authentication;

public interface IUserContext {

  Authentication getAuthentication();
  
  String getUsername();
  
  String getTenant();

  String getHashedUsername();

  boolean isAnonymous();

  boolean isSysAdmin();
}
