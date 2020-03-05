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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class RequestRepositorySessionHelperTest extends AbstractIntegrationTest {

    private static String TEST_TENANT_ID = "TEST_TENANT_ID";

    @Test
    public void testInternalSessionSupplierWired() throws RepositoryException {
        // create a session helper with autowiring (live http request)
        RequestRepositorySessionHelper helper = new RequestRepositorySessionHelper();
        helper.afterPropertiesSet();
        helper.setTenantId(TEST_TENANT_ID);
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
        RequestRepositorySessionHelper helper = new RequestRepositorySessionHelper(false);
        helper.setTenantId(TEST_TENANT_ID);
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
        // when login is called return a new session mock
        when(myRepository.login(anyObject(), anyString())).thenAnswer(new Answer<Session>() {
            private boolean isLive = true;
            public Session answer(InvocationOnMock invocation) {
                // when a new session mock is returned thet the live property to true
                isLive = true;
                Session mySession = Mockito.mock(Session.class);
                doAnswer(new Answer<Void>() {
                    // when logout is called set the isLive property to false;
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        isLive = false;
                        return null;
                    }
                }).when(mySession).logout();
                when(mySession.isLive()).thenAnswer(new Answer<Boolean>() {
                    @Override
                    public Boolean answer(InvocationOnMock invocation) throws Throwable {
                        return isLive;
                    }
                });
                return mySession;
            }
        });
        return myRepository;
    }
}
