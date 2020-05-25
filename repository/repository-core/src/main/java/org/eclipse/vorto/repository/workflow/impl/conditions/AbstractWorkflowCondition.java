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
package org.eclipse.vorto.repository.workflow.impl.conditions;

import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWorkflowCondition implements IWorkflowCondition {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWorkflowCondition.class);

    private NamespaceService namespaceService;

    private UserNamespaceRoleService userNamespaceRoleService;

    public AbstractWorkflowCondition(NamespaceService namespaceService, UserNamespaceRoleService userNamespaceRoleService) {
        this.namespaceService = namespaceService;
        this.userNamespaceRoleService = userNamespaceRoleService;
    }

    boolean hasRole(IUserContext userContext, User user, IRole role) {
        Namespace namespace = namespaceService.findNamespaceByWorkspaceId(userContext.getTenant());
        try {
            return userNamespaceRoleService.hasRole(user, namespace, role);
        } catch (DoesNotExistException e) {
            LOGGER.warn("Error trying to verify role " + role.getName() + " on user " + user.getUsername(), e);
            return false;
        }
    }

}
