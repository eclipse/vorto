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

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.diagnostics.ModeshapeAclEntry;
import org.eclipse.vorto.repository.diagnostics.ModeshapeProperty;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ModeshapeDoctorTest extends IntegrationTestBase {

    @Autowired
    private NamespaceRepository namespaceRepository;

    private final Function<ModelId, String>
        modelIdToPath =  modelId -> '/' + modelId.getNamespace().replace('.', '/') + '/' + modelId.getName() + '/' + modelId.getVersion();

    @Test
    public void retrieveNodeByModelId() throws Exception {
        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/model/" + testModel.id + "/").with(userSysadmin))
            .andExpect(status().isOk());
    }

    @Test
    public void retrieveNodeByPath() throws Exception {
        String workspace = namespaceRepository.findByName("com.mycompany").getWorkspaceId();
        String path = modelIdToPath.apply(testModel.id);
        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path).with(userSysadmin))
            .andExpect(status().isOk());
    }

    @Test
    public void retrieveNodeByPathAndModelIdAndCompare() throws Exception {
        String workspace = namespaceRepository.findByName("com.mycompany").getWorkspaceId();
        String path = modelIdToPath.apply(testModel.id);
        MvcResult mvcResult = repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path)
            .with(userSysadmin))
            .andExpect(status().isOk())
            .andReturn();

        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/model/" + testModel.id + "/").with(userSysadmin))
            .andExpect(content().string(mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteNode() throws Exception {
        String workspace = namespaceRepository.findByName("com.mycompany").getWorkspaceId();
        String path = modelIdToPath.apply(testModel.id);
        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path).with(userSysadmin))
            .andExpect(status().isOk());

        repositoryServer.perform(delete(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path).with(userSysadmin))
            .andExpect(status().isOk());

        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path).with(userSysadmin))
            .andExpect(status().isNotFound());
    }

    @Test
    public void retrieveContent() throws Exception {
        String modelId = "com.test.Location:1.0.0";
        String fileName = "Location.fbmodel";
        createNamespaceSuccessfully("com.test", userSysadmin);
        createModel(userSysadmin, fileName, modelId);
        String fileContentJson = createContent(fileName);
        String workspace = namespaceRepository.findByName("com.test").getWorkspaceId();
        String path = "/com/test/Location/1.0.0";
        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path).with(userSysadmin))
            .andExpect(status().isOk());

        String contentPath = path + "/Location.fbmodel/jcr:content";

        String dsl = new ObjectMapper().readTree(fileContentJson).get("contentDsl").getTextValue().trim();

        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "/data?path=" + contentPath).with(userSysadmin))
            .andExpect(content().string(dsl))
            .andExpect(status().isOk());
    }

    @Test
    public void setProperty() throws Exception {
        ObjectMapper om = new ObjectMapper();
        String workspace = namespaceRepository.findByName("com.mycompany").getWorkspaceId();
        String path = modelIdToPath.apply(testModel.id);
        ModeshapeProperty property = new ModeshapeProperty();
        property.setName("vorto:name");
        property.setValue("Blah");
        String body = om.writer().writeValueAsString(property);

        MvcResult mvcResult = repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path)
            .with(userSysadmin))
            .andExpect(status().isOk()).andReturn();

        JsonNode jsonNode = om.readTree(mvcResult.getResponse().getContentAsString());
        Iterator<JsonNode> nodeIterator = jsonNode.get("properties").getElements();
        String vortoNameValue = findValueForVortoNameProperty(nodeIterator);
        assertEquals(testModel.modelName, vortoNameValue);

        repositoryServer.perform(put(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "/property?path=" + path).with(userSysadmin)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        MvcResult mvcResult2 = repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path)
            .with(userSysadmin))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode jsonNode2 = om.readTree(mvcResult2.getResponse().getContentAsString());
        Iterator<JsonNode> nodeIterator2 = jsonNode2.get("properties").getElements();
        String vortoNameValue2 = findValueForVortoNameProperty(nodeIterator2);
        assertEquals("Blah", vortoNameValue2);
    }

    @Test(expected = NoVortoNamePropertyException.class)
    public void deleteProperty() throws Exception {
        ObjectMapper om = new ObjectMapper();
        String workspace = namespaceRepository.findByName("com.mycompany").getWorkspaceId();
        String path = modelIdToPath.apply(testModel.id);
        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path).with(userSysadmin))
            .andExpect(status().isOk());

        ModeshapeProperty property = new ModeshapeProperty();
        property.setName("vorto:name");

        String body = om.writer().writeValueAsString(property);

        repositoryServer.perform(delete(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "/property?path=" + path).with(userSysadmin)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        MvcResult mvcResult = repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path)
            .with(userSysadmin))
            .andExpect(status().isOk()).andReturn();

        Iterator<JsonNode> nodeIterator =
            om.readTree(mvcResult.getResponse().getContentAsString()).get("properties").iterator();

        findValueForVortoNameProperty(nodeIterator);
    }

    @Test
    public void setAcl() throws Exception {
        ObjectMapper om = new ObjectMapper();
        String workspace = namespaceRepository.findByName("com.mycompany").getWorkspaceId();
        String path = modelIdToPath.apply(testModel.id);
        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path).with(userSysadmin))
            .andExpect(status().isOk());

        ModeshapeAclEntry aclEntry = new ModeshapeAclEntry();
        aclEntry.setPrincipal("model_master");
        List<String> privileges = new ArrayList<>();
        privileges.add("jcr:all");
        aclEntry.setPrivileges(privileges);

        String body = om.writer().writeValueAsString(aclEntry);

        repositoryServer.perform(put(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "/acl?path=" + path).with(userSysadmin)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        MvcResult mvcResult = repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path)
            .with(userSysadmin))
            .andExpect(status().isOk()).andReturn();

        JsonNode jsonNode = om.reader().readTree(mvcResult.getResponse().getContentAsString());
        Iterator<JsonNode> nodeIterator = jsonNode.get("aclEntryList").getElements();

        assertEquals("jcr:all", findTestAclEntry(nodeIterator));
    }

    @Test
    public void deleteAcl() throws Exception {
        ObjectMapper om = new ObjectMapper();
        String workspace = namespaceRepository.findByName("com.mycompany").getWorkspaceId();
        String path = modelIdToPath.apply(testModel.id);
        repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path).with(userSysadmin))
            .andExpect(status().isOk());

        ModeshapeAclEntry aclEntry = new ModeshapeAclEntry();
        aclEntry.setPrincipal("model_master");
        List<String> privileges = new ArrayList<>();
        privileges.add("jcr:all");
        aclEntry.setPrivileges(privileges);

        String body = om.writer().writeValueAsString(aclEntry);

        repositoryServer.perform(put(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "/acl?path=" + path).with(userSysadmin)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        MvcResult mvcResult = repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path)
            .with(userSysadmin))
            .andExpect(status().isOk()).andReturn();

        JsonNode jsonNode = om.reader().readTree(mvcResult.getResponse().getContentAsString());
        Iterator<JsonNode> nodeIterator = jsonNode.get("aclEntryList").getElements();

        assertEquals("jcr:all", findTestAclEntry(nodeIterator));

        repositoryServer.perform(delete(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "/acl?path=" + path).with(userSysadmin)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        MvcResult mvcResult2 = repositoryServer.perform(get(
            "/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + path)
            .with(userSysadmin))
            .andExpect(status().isOk()).andReturn();

        JsonNode jsonNode2 = om.reader().readTree(mvcResult2.getResponse().getContentAsString());
        Iterator<JsonNode> nodeIterator2 = jsonNode2.get("aclEntryList").getElements();

        try {
            findTestAclEntry(nodeIterator2);
            fail();
        } catch (NoTestAclEntry e) {
            // if exception is caught, the test succeeded.
        }
    }


    private String findValueForVortoNameProperty(Iterator<JsonNode> nodeIterator) throws NoVortoNamePropertyException {
        while (nodeIterator.hasNext()) {
            JsonNode next = nodeIterator.next();
            if ("vorto:name".equals(next.get("name").getTextValue())) {
                return next.get("value").getTextValue();
            }
        }
        throw new NoVortoNamePropertyException();
    }

    private String findTestAclEntry(Iterator<JsonNode> nodeIterator) throws NoTestAclEntry {
        while (nodeIterator.hasNext()) {
            JsonNode next = nodeIterator.next();
            if ("model_master".equals(next.get("principal").getTextValue())) {
                return next.get("privileges").getElements().next().getTextValue();
            }
        }
        throw new NoTestAclEntry();
    }

    static class NoVortoNamePropertyException extends Exception {

    }

    static class NoTestAclEntry extends Exception {

    }

}
