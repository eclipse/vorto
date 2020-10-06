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
package org.eclipse.vorto.repository.diagnostics;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModeshapeDoctor;
import org.eclipse.vorto.repository.core.impl.AbstractRepositoryOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModeshapeDoctor extends AbstractRepositoryOperation implements IModeshapeDoctor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModeshapeDoctor.class);

    @Override
    public ModeshapeNodeData readModeshapeNodeData(ModelId modelId) {
        String path = '/' + modelId.getNamespace().replace('.', '/') + '/' + modelId.getName() + '/' + modelId.getVersion();
        return readModeshapeNodeData(path);
    }

    @Override
    public ModeshapeNodeData readModeshapeNodeData(String path) {
        return doInSession(session -> {
            Node node = session.getNode(path);

            LOGGER.info("Node name: {}", node.getName());

            ModeshapeNodeData data = new ModeshapeNodeData();
            data.setName(node.getName());
            data.setPath(path);
            printChildren(node, data);
            printProperties(node, data);
            printACL(session, node, data);

            return data;
        });
    }

    @Override
    public ModeshapeContentData readModeshapeNodeContent(String path) {
        return doInSession(session -> {
            Node node = session.getNode(path);
            LOGGER.info("Node name: {}", node.getName());
            Property property = node.getProperty("jcr:data");
            String parentName = property.getParent().getParent().getName();
            Binary binary = property.getValue().getBinary();
            ModeshapeContentData contentData = new ModeshapeContentData();
            contentData.setFilename(parentName);
            contentData.setData(IOUtils.toByteArray(binary.getStream()));
            return contentData;
        });
    }

    private void printACL(Session session, Node node, ModeshapeNodeData data)
        throws RepositoryException {
        AccessControlManager accessControlManager = session.getAccessControlManager();
        AccessControlPolicyIterator it = accessControlManager.getApplicablePolicies(node.getPath());
        AccessControlList acl;
        if (it.hasNext()) {
            acl = (AccessControlList) it.nextAccessControlPolicy();
        } else {
            acl = (AccessControlList) accessControlManager.getPolicies(node.getPath())[0];
        }

        for (int i = 0; i < acl.getAccessControlEntries().length; i++) {
            LOGGER.info("ACL Entry {} principal: {}", i, acl.getAccessControlEntries()[i].getPrincipal().getName());
            LOGGER.info("ACL Entry {} principal: {}", i, concatPrivileges(acl.getAccessControlEntries()[i].getPrivileges()));
            ModeshapeAclEntry aclEntry = new ModeshapeAclEntry();
            aclEntry.setPrincipal(acl.getAccessControlEntries()[i].getPrincipal().getName());
            aclEntry.setPrivileges(transformToPrivilegeList(acl.getAccessControlEntries()[i].getPrivileges()));
            data.addAclEntry(aclEntry);
        }
    }

    private List<String> transformToPrivilegeList(Privilege[] privileges) {
        return Arrays.stream(privileges).map(Privilege::getName).collect(Collectors.toList());
    }

    private String concatPrivileges(Privilege[] privileges) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(privileges).forEach(privilege -> sb.append(privilege.getName()));
        return sb.toString();
    }

    private  void printChildren(Node node, ModeshapeNodeData data) throws RepositoryException {
        NodeIterator nodeIterator = node.getNodes();
        while (nodeIterator.hasNext()) {
            Node childNode = nodeIterator.nextNode();
            LOGGER.info("Child node name: {}", childNode.getName());
            data.addChildNode(childNode.getName());
        }
    }

    private void printProperties(Node node, ModeshapeNodeData data) throws RepositoryException {
        PropertyIterator propertyIterator = node.getProperties();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.nextProperty();
            LOGGER.info("Property name: {}", property.getName());
            if ("jcr:data".equals(property.getName())) {
                data.setContentOnNode(true);
            }
            printPropertyValue(property, data);
        }
    }

    private void printPropertyValue(Property property, ModeshapeNodeData data) throws RepositoryException {
        if (property.isMultiple()) {
            for (Value value : property.getValues()) {
                LOGGER.info("Property multi-value: {}", value);
                data.addProperty(property.getName(), value.toString());
            }
        } else {
            LOGGER.info("Property value: {}", property.getValue());
            data.addProperty(property.getName(), property.getValue().toString());
        }
    }

}
