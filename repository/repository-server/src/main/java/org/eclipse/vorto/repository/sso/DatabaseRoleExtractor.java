/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.sso;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DatabaseRoleExtractor implements RoleExtractor {

    @Autowired
    public IUserAccountService userService;

    public DatabaseRoleExtractor( IUserAccountService userService) {
        this.userService = userService;
    }

    public String extractAuthorities(Map<String, Object> map) {
        String username = (String) map.get("sub");
        User user = userService.getUser(username);
        if (user == null) {
            return "USER";
        }

        return user.getUserRolesAsCommaSeparatedString();
    }
}
