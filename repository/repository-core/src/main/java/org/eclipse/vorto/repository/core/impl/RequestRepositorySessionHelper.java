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
import org.eclipse.vorto.repository.domain.IRole;
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
    private String workspaceId;
    private Authentication user;
    private Repository repository;
    private Set<IRole> roleSet;
    private Supplier<Session> internalSessionSupplier;

    public RequestRepositorySessionHelper() {
        this(true);
    }


    public RequestRepositorySessionHelper(boolean isAutowired) {
        if(isAutowired) {
            internalSessionSupplier = () -> {
                try {
                    return getSessionInternal(workspaceId, user);
                } catch (LoginException e) {
                    throw new UserLoginException(user.getName(), e);
                } catch (NoSuchWorkspaceException e) {
                    throw new TenantNotFoundException(workspaceId, e);
                } catch (RepositoryException e) {
                    throw new FatalModelRepositoryException("Error while getting repository given workspace ID ["
                            + workspaceId + "] and user [" + user.getName() + "]", e);
                }
            };
        } else {
            internalSessionSupplier = () -> {
                try {
                    return login(workspaceId, user);
                } catch (RepositoryException e) {
                    throw new FatalModelRepositoryException("Error while getting repository given workspace ID ["
                            + workspaceId + "] and user [" + user.getName() + "]", e);
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

    private synchronized Session getSessionInternal(String workspaceId, Authentication user) throws RepositoryException {
        Session mySession;
        mySession = this.repositorySessionMap.get(workspaceId);
        if (mySession == null || !mySession.isLive()) {
            mySession = login(workspaceId, user);
            this.repositorySessionMap.put(workspaceId, mySession);
        }
        return mySession;
    }

    private Session login(String workspaceId, Authentication user) throws RepositoryException {
        return repository.login(new SpringSecurityCredentials(user, roleSet), workspaceId);
    }

    public void logoutSessionIfNotReusable(Session session) {
        // if the session is in the session map - do not logout the session. It will be logged out
        //  after the request is finished.
        if (this.repositorySessionMap != null && this.repositorySessionMap.get(workspaceId) != null)
            return;
        session.logout();
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepository() {
        return repository;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setUser(Authentication user) {
        this.user = user;
    }

    public void setRolesInNamespace(Set<IRole> userRolesInNamespace) {
        this.roleSet = userRolesInNamespace;
    }
}
