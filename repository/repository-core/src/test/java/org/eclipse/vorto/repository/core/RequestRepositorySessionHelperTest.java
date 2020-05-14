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

import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.impl.RequestRepositorySessionHelper;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class RequestRepositorySessionHelperTest extends AbstractIntegrationTest {

    private static String TEST_TENANT_ID = "TEST_TENANT_ID";

    @Test
    public void testInternalSessionSupplierWired() throws RepositoryException {
        // create a session helper with autowiring (live http request)
        RequestRepositorySessionHelper helper = new RequestRepositorySessionHelper();
        helper.afterPropertiesSet();
        helper.setWorkspaceId(TEST_TENANT_ID);
        helper.setRepository(createMockRepository());
        Session mySession = helper.getSession();
        Assert.assertNotNull(mySession);
        Assert.assertTrue(mySession.isLive());
        // session should be kept alive for potential reuse
        helper.logoutSessionIfNotReusable(mySession);
        Assert.assertTrue(mySession.isLive());
        // same session should be returned when requested again
        Session sessionForReuse = helper.getSession();
        Assert.assertTrue(mySession.equals(sessionForReuse));
        // after destroy session should be logged out
        helper.destroy();
        Assert.assertFalse(sessionForReuse.isLive());
    }

    @Test
    public void testInternalSessionSupplierNonWired() throws RepositoryException {
        // create a session helper without autowiring (no http request)
        RequestRepositorySessionHelper helper = new RequestRepositorySessionHelper(false, null);
        helper.setWorkspaceId(TEST_TENANT_ID);
        helper.setRepository(createMockRepository());
        Session mySession = helper.getSession();
        Assert.assertNotNull(mySession);
        Assert.assertTrue(mySession.isLive());
        // session should be logged out - no reuse
        helper.logoutSessionIfNotReusable(mySession);
        Assert.assertFalse(mySession.isLive());
        // new session should be returned
        Session sessionForReuse = helper.getSession();
        Assert.assertFalse(mySession.equals(sessionForReuse));
        // after destroy session should still be alive
        helper.destroy();
        Assert.assertTrue(sessionForReuse.isLive());

    }

    private Repository createMockRepository() throws RepositoryException {
        Repository myRepository = Mockito.mock(Repository.class);
        when(myRepository.login(anyObject(), anyString())).thenAnswer(inv -> createNewMockSession());
        return myRepository;
    }

    private Session createNewMockSession() {
        Session liveSession = Mockito.mock(Session.class);

        when(liveSession.isLive()).thenAnswer(inv -> Mockito.mockingDetails(liveSession)
            .getInvocations().stream().noneMatch(i -> "logout".equals(i.getMethod().getName())));
        return liveSession;
    }

}
