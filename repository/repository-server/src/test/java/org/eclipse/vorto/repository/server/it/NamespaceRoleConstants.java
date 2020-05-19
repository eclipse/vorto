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
package org.eclipse.vorto.repository.server.it;

import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.NamespaceRole;
import org.eclipse.vorto.repository.domain.RepositoryRole;

public class NamespaceRoleConstants {

    public static final IRole USER = NamespaceRole.DEFAULT_NAMESPACE_ROLES[0];
    public static final IRole MODEL_CREATOR = NamespaceRole.DEFAULT_NAMESPACE_ROLES[1];
    public static final IRole MODEL_PROMOTER = NamespaceRole.DEFAULT_NAMESPACE_ROLES[2];
    public static final IRole MODEL_REVIEWER = NamespaceRole.DEFAULT_NAMESPACE_ROLES[3];
    public static final IRole MODEL_PUBLISHER = NamespaceRole.DEFAULT_NAMESPACE_ROLES[4];
    public static final IRole NAMESPACE_ADMIN = NamespaceRole.DEFAULT_NAMESPACE_ROLES[5];
    public static final IRole SYS_ADMIN = RepositoryRole.SYS_ADMIN;
}
