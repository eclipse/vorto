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
package org.eclipse.vorto.repository.utils;

import com.google.common.collect.Sets;
import org.eclipse.vorto.repository.domain.*;

import java.util.Set;

@Deprecated
public class TenantProvider {

    /**
     * This method can be removed after all references to Tenants are removed
     */
    @Deprecated
    public static Tenant playgroundTenant() {
        UserRole roleUser = new UserRole(Role.USER);
        UserRole roleCreator = new UserRole(Role.MODEL_CREATOR);
        UserRole rolePromoter = new UserRole(Role.MODEL_PROMOTER);
        UserRole roleReviewer = new UserRole(Role.MODEL_REVIEWER);
        UserRole rolePublisher = new UserRole(Role.MODEL_PUBLISHER);
        UserRole roleTenantAdmin = new UserRole(Role.TENANT_ADMIN);
        UserRole roleSysAdmin = new UserRole(Role.SYS_ADMIN);

        Tenant playground = Tenant.newTenant("playground", "org.eclipse",
            Sets.newHashSet("org.eclipse", "com.mycompany", "com.ipso", "examples.mappings", "vorto.private.playground"));

        playground.addUser(createTenantUser("alex",
            Sets.newHashSet(roleUser, roleCreator, rolePromoter, roleReviewer)));
        playground.addUser(createTenantUser("erle",
            Sets.newHashSet(roleUser, roleCreator, rolePromoter, roleReviewer, roleTenantAdmin)));
        playground.addUser(createTenantUser("admin",
            Sets.newHashSet(roleUser, roleCreator, rolePromoter, roleReviewer, roleSysAdmin)));
        playground.addUser(createTenantUser("creator", Sets.newHashSet(roleUser, roleCreator)));
        playground.addUser(createTenantUser("promoter", Sets.newHashSet(roleUser, rolePromoter)));
        playground.addUser(createTenantUser("reviewer", Sets.newHashSet(roleUser, roleReviewer)));
        playground.addUser(createTenantUser("publisher", Sets.newHashSet(roleUser, rolePublisher)));

        return playground;
    }

    @Deprecated
    public static TenantUser createTenantUser(String name, Set<UserRole> roles) {
        User _user = User.create(name, "GITHUB", null);
        TenantUser user = new TenantUser();
        user.setRoles(roles);
        _user.addTenantUser(user);
        return user;
    }
}
