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
package org.eclipse.vorto.repository.core.impl;

import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.TenantNotFoundException;
import org.eclipse.vorto.repository.core.UserLoginException;
import org.eclipse.vorto.repository.core.security.SpringSecurityCredentials;
import org.eclipse.vorto.repository.domain.Role;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.jcr.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Component
@RequestScope
public class RequestRepositorySessionHelper implements DisposableBean, InitializingBean {

    private static final Logger myLogger = Logger.getLogger(RequestRepositorySessionHelper.class);

    private Map<String, Session> repositorySessionMap;
    private String tenantId;
    private Authentication user;
    private Repository repository;
    private Set<Role> roleSet;
    private Supplier<Session> internalSessionSupplier;

    public RequestRepositorySessionHelper() {
        this(true);
    }


    public RequestRepositorySessionHelper(boolean isAutowired) {
        if(isAutowired) {
            internalSessionSupplier = () -> {
                try {
                    return getSessionInternal(tenantId, user);
                } catch (LoginException e) {
                    throw new UserLoginException(user.getName(), e);
                } catch (NoSuchWorkspaceException e) {
                    throw new TenantNotFoundException(tenantId, e);
                } catch (RepositoryException e) {
                    throw new FatalModelRepositoryException("Error while getting repository given tenant ["
                            + tenantId + "] and user [" + user.getName() + "]", e);
                }
            };
        } else {
            internalSessionSupplier = () -> {
                try {
                    return login(tenantId, user);
                } catch (RepositoryException e) {
                    throw new FatalModelRepositoryException("Error while getting repository given tenant ["
                            + tenantId + "] and user [" + user.getName() + "]", e);
                }
            };
        }
    }

    @Override
    public void destroy() {
        myLogger.debug("destroy");
        if (this.repositorySessionMap == null)
            return;
        logoutAssociatedRepositorySessions();
        this.repositorySessionMap = null;
    }

    private void logoutAssociatedRepositorySessions() {
        for (Map.Entry<String, Session> entry : this.repositorySessionMap.entrySet()) {
            myLogger.debug("logging out session: " + entry.getValue().getUserID() +
                    " session live: " + entry.getValue().isLive());
            entry.getValue().logout();
        }
    }

    @Override
    public void afterPropertiesSet() {
        myLogger.debug("afterPropertiesSet: reinit session map");
        this.repositorySessionMap = new HashMap<>();
    }

    public Session getSession() {
        return internalSessionSupplier.get();
    }

    private synchronized Session getSessionInternal(String tenant, Authentication user) throws RepositoryException {
        Session mySession;
        mySession = this.repositorySessionMap.get(tenant);
        if (mySession == null || !mySession.isLive()) {
            mySession = login(tenant, user);
            this.repositorySessionMap.put(tenant, mySession);
        }
        return mySession;
    }

    private Session login(String tenant, Authentication user) throws RepositoryException {
        return repository.login(
                new SpringSecurityCredentials(user, roleSet),
                tenant);
    }

    public void logoutSessionIfNotReusable(Session session) {
        // if the session is in the session map - do not logout the session. It will be logged out
        //  after the request is finished.
        if (this.repositorySessionMap != null && this.repositorySessionMap.get(tenantId) != null)
            return;
        session.logout();
    }

    public void setTenantId(String tenant) {
        this.tenantId = tenant;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepository() {
        return repository;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setUser(Authentication user) {
        this.user = user;
    }

    public void setRolesInTenant(Set<Role> userRolesInTenant) {
        this.roleSet = userRolesInTenant;
    }
}
