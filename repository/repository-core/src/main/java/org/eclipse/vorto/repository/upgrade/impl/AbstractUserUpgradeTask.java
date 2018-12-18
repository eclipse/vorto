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
package org.eclipse.vorto.repository.upgrade.impl;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.upgrade.IUpgradeTaskCondition;
import org.eclipse.vorto.repository.upgrade.IUserUpgradeTask;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

abstract class AbstractUserUpgradeTask implements IUserUpgradeTask {
  protected Optional<String> getEmailPrefix(OAuth2Authentication oauth2User) {
    UsernamePasswordAuthenticationToken userAuth =
        (UsernamePasswordAuthenticationToken) oauth2User.getUserAuthentication();

    @SuppressWarnings("unchecked")
    Map<String, Object> userDetailsMap = (Map<String, Object>) userAuth.getDetails();

    String email = (String) userDetailsMap.get("email");

    if (email == null) {
      return Optional.empty();
    }

    return Optional.of(email.split("@")[0]);
  }

  @Override
  public Optional<IUpgradeTaskCondition> condition(User user, Supplier<Object> upgradeContext) {
    return Optional.of(() -> upgradeContext.get() instanceof OAuth2Authentication);
  }
}
