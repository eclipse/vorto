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
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.retention.RetentionManager;
import javax.jcr.security.AccessControlManager;
import javax.jcr.version.VersionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessControlException;

public class RequestRepositorySessionHelperTest extends AbstractIntegrationTest {

    private static String TEST_TENANT_ID = "TEST_TENANT_ID";

    @Test
    public void testInternalSessionSupplierWired() {
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
    public void testInternalSessionSupplierNonWired() {
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

    private Repository createMockRepository() {
        return new Repository() {
            @Override
            public String[] getDescriptorKeys() {
                return new String[0];
            }

            @Override
            public boolean isStandardDescriptor(String s) {
                return false;
            }

            @Override
            public boolean isSingleValueDescriptor(String s) {
                return false;
            }

            @Override
            public Value getDescriptorValue(String s) {
                return null;
            }

            @Override
            public Value[] getDescriptorValues(String s) {
                return new Value[0];
            }

            @Override
            public String getDescriptor(String s) {
                return null;
            }

            @Override
            public Session login(Credentials credentials, String s) throws LoginException, NoSuchWorkspaceException, RepositoryException {
                return new MockRepositorySession();
            }

            @Override
            public Session login(Credentials credentials) throws LoginException, RepositoryException {
                return null;
            }

            @Override
            public Session login(String s) throws LoginException, NoSuchWorkspaceException, RepositoryException {
                return null;
            }

            @Override
            public Session login() throws LoginException, RepositoryException {
                return null;
            }
        };
    }

    private class MockRepositorySession implements Session {

        private boolean loggedOut = false;

        public boolean isLoggedOut() {
            return loggedOut;
        }

        @Override
        public Repository getRepository() {
            return null;
        }

        @Override
        public String getUserID() {
            return null;
        }

        @Override
        public String[] getAttributeNames() {
            return new String[0];
        }

        @Override
        public Object getAttribute(String s) {
            return null;
        }

        @Override
        public Workspace getWorkspace() {
            return null;
        }

        @Override
        public Node getRootNode() throws RepositoryException {
            return null;
        }

        @Override
        public Session impersonate(Credentials credentials) throws LoginException, RepositoryException {
            return null;
        }

        @Override
        public Node getNodeByUUID(String s) throws ItemNotFoundException, RepositoryException {
            return null;
        }

        @Override
        public Node getNodeByIdentifier(String s) throws ItemNotFoundException, RepositoryException {
            return null;
        }

        @Override
        public Item getItem(String s) throws PathNotFoundException, RepositoryException {
            return null;
        }

        @Override
        public Node getNode(String s) throws PathNotFoundException, RepositoryException {
            return null;
        }

        @Override
        public Property getProperty(String s) throws PathNotFoundException, RepositoryException {
            return null;
        }

        @Override
        public boolean itemExists(String s) throws RepositoryException {
            return false;
        }

        @Override
        public boolean nodeExists(String s) throws RepositoryException {
            return false;
        }

        @Override
        public boolean propertyExists(String s) throws RepositoryException {
            return false;
        }

        @Override
        public void move(String s, String s1) throws ItemExistsException, PathNotFoundException, VersionException, ConstraintViolationException, LockException, RepositoryException {

        }

        @Override
        public void removeItem(String s) throws VersionException, LockException, ConstraintViolationException, AccessDeniedException, RepositoryException {

        }

        @Override
        public void save() throws AccessDeniedException, ItemExistsException, ReferentialIntegrityException, ConstraintViolationException, InvalidItemStateException, VersionException, LockException, NoSuchNodeTypeException, RepositoryException {

        }

        @Override
        public void refresh(boolean b) throws RepositoryException {

        }

        @Override
        public boolean hasPendingChanges() throws RepositoryException {
            return false;
        }

        @Override
        public ValueFactory getValueFactory() throws UnsupportedRepositoryOperationException, RepositoryException {
            return null;
        }

        @Override
        public boolean hasPermission(String s, String s1) throws RepositoryException {
            return false;
        }

        @Override
        public void checkPermission(String s, String s1) throws AccessControlException, RepositoryException {

        }

        @Override
        public boolean hasCapability(String s, Object o, Object[] objects) throws RepositoryException {
            return false;
        }

        @Override
        public ContentHandler getImportContentHandler(String s, int i) throws PathNotFoundException, ConstraintViolationException, VersionException, LockException, RepositoryException {
            return null;
        }

        @Override
        public void importXML(String s, InputStream inputStream, int i) throws IOException, PathNotFoundException, ItemExistsException, ConstraintViolationException, VersionException, InvalidSerializedDataException, LockException, RepositoryException {

        }

        @Override
        public void exportSystemView(String s, ContentHandler contentHandler, boolean b, boolean b1) throws PathNotFoundException, SAXException, RepositoryException {

        }

        @Override
        public void exportSystemView(String s, OutputStream outputStream, boolean b, boolean b1) throws IOException, PathNotFoundException, RepositoryException {

        }

        @Override
        public void exportDocumentView(String s, ContentHandler contentHandler, boolean b, boolean b1) throws PathNotFoundException, SAXException, RepositoryException {

        }

        @Override
        public void exportDocumentView(String s, OutputStream outputStream, boolean b, boolean b1) throws IOException, PathNotFoundException, RepositoryException {

        }

        @Override
        public void setNamespacePrefix(String s, String s1) throws NamespaceException, RepositoryException {

        }

        @Override
        public String[] getNamespacePrefixes() throws RepositoryException {
            return new String[0];
        }

        @Override
        public String getNamespaceURI(String s) throws NamespaceException, RepositoryException {
            return null;
        }

        @Override
        public String getNamespacePrefix(String s) throws NamespaceException, RepositoryException {
            return null;
        }

        @Override
        public void logout() {
            loggedOut = true;
        }

        @Override
        public boolean isLive() {
            return !loggedOut;
        }

        @Override
        public void addLockToken(String s) {

        }

        @Override
        public String[] getLockTokens() {
            return new String[0];
        }

        @Override
        public void removeLockToken(String s) {

        }

        @Override
        public AccessControlManager getAccessControlManager() throws UnsupportedRepositoryOperationException, RepositoryException {
            return null;
        }

        @Override
        public RetentionManager getRetentionManager() throws UnsupportedRepositoryOperationException, RepositoryException {
            return null;
        }
    }
}
