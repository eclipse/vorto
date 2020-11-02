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
package org.eclipse.vorto.repository.diagnostics;

import java.util.ArrayList;
import java.util.List;

public class ModeshapeAclEntry {

    private String principal;

    private List<String> privileges = new ArrayList<>();

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public List<String> getPrivileges() {
        return privileges;
    }

    public void addPrivilege(String privilege) {
        this.privileges.add(privilege);
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }
}
