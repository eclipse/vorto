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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.eclipse.vorto.model.ModelType;
import org.junit.Test;
import org.springframework.http.MediaType;

public class KeywordNamespaceTest extends IntegrationTestBase {

    @Test
    public void createModelWithKeywordsInNamespace() throws Exception {
        String modelId = "com.status.fault.events.operations.breakable.category.configuration.description:Location2:1.0.0";
        String fileName = "Location2.fbmodel";
        createNamespaceSuccessfully("com.status.fault", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
        repositoryServer.perform(delete("/rest/models/" + modelId).with(userSysadmin));
    }

    @Test
    public void createModelWithUpperCaseKeywordInNamespace() throws Exception {
        String modelId = "com.status.dateTime:Location3:1.0.0";
        String fileName = "Location3.fbmodel";
        createNamespaceSuccessfully("com.status.dateTime", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
        repositoryServer.perform(delete("/rest/models/" + modelId).with(userSysadmin));
    }

    @Test
    public void createModelWithIllegalFunctionblocksInNamespace() throws Exception {
        String modelId = "com.test.functionblocks:InvalidLocation:1.0.0";
        String fileName = "invalid/InvalidLocation.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void createModelWithIllegalInfomodelInNamespace() throws Exception {
        String modelId = "com.test.infomodel:InvalidLocation3:1.0.0";
        String fileName = "invalid/InvalidLocation3.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void createModelWithIllegalMandatoryInNamespace() throws Exception {
        String modelId = "com.test.mandatory:InvalidLocation4:1.0.0";
        String fileName = "invalid/InvalidLocation4.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void createModelWithIllegalNamespaceInNamespace() throws Exception {
        String modelId = "com.test.namespace:InvalidLocation5:1.0.0";
        String fileName = "invalid/InvalidLocation5.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void createModelWithIllegalOptionalInNamespace() throws Exception {
        String modelId = "com.test.optional:InvalidLocation6:1.0.0";
        String fileName = "invalid/InvalidLocation6.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void createModelWithIllegalVersionInNamespace() throws Exception {
        String modelId = "com.test.version:InvalidLocation7:1.0.0";
        String fileName = "invalid/InvalidLocation7.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void createModelWithIllegalDictionaryInNamespace() throws Exception {
        String modelId = "com.test.dictionary:InvalidLocation8:1.0.0";
        String fileName = "invalid/InvalidLocation8.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void createModelWithIllegalExtensionInNamespace() throws Exception {
        String modelId = "com.test.extension:InvalidLocation9:1.0.0";
        String fileName = "invalid/InvalidLocation9.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void createModelWithIllegalFunctionblockInNamespace() throws Exception {
        String modelId = "com.test.functionblock:InvalidLocation10:1.0.0";
        String fileName = "invalid/InvalidLocation10.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        repositoryServer
            .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
                .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

}
