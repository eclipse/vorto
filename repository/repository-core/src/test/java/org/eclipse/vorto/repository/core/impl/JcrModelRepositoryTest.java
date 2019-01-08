/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class JcrModelRepositoryTest extends AbstractIntegrationTest {


    @Before public void setUp() throws Exception {

    }

    @Test public void search() throws Exception {
        assertNotNull(modelRepository);
        modelRepository.search(null);
    }

    @Test public void getSession() {
    }


    @Test(expected = ModelNotFoundException.class) public void getModelContentWithNonExistenModelID() {
        modelRepository.getModelContent(ModelId.fromPrettyFormat("asdf:test:1.0.2"));
    }

    @Test public void save() {
    }

    @Test public void getById() {
    }

    @Test public void getMappingModelsForTargetPlatform() {
    }

    @Test public void getEMFResource() {
    }

    @Test public void removeModel() {
    }

    @Test public void updateMeta() {
    }

    @Test public void updateState() {
    }

    @Test public void getModelSearchUtil() {
    }

    @Test public void setModelSearchUtil() {
    }

    @Test public void getUserRepository() {
    }

    @Test public void setUserRepository() {
    }

    @Test public void addFileContent() {
    }

    @Test public void getFileContent() {
    }

    @Test public void attachFile() {
    }

    @Test public void getAttachments() {
    }

    @Test public void getAttachmentsByTag() {
    }

    @Test public void getAttachmentContent() {
    }

    @Test public void deleteAttachment() {
    }

    @Test public void createVersion() {
    }

    @Test public void diagnoseAllModels() {
    }

    @Test public void diagnoseModel() {
    }

    @Test public void setModelParserFactory() {
    }

    @Test public void setRepositoryDiagnostics() {
    }

    @Test public void exists() {
    }

    @Test public void getPolicyEntries() {
    }

    @Test public void addPolicyEntry() {
    }

    @Test public void removePolicyEntry() {
    }

    @Test public void hasPermission() {
    }
}
