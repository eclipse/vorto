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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModeshapeDoctor;
import org.eclipse.vorto.repository.core.impl.AbstractRepositoryOperation;
import org.modeshape.jcr.security.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModeshapeDoctor extends AbstractRepositoryOperation implements IModeshapeDoctor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModeshapeDoctor.class);

    @Override
    public ModeshapeNodeData readModeshapeNodeData(ModelId modelId) {
        String path = String.format("/%s/%s/%s", modelId.getNamespace().replace('.', '/'), modelId.getName(), modelId.getVersion());
        return readModeshapeNodeData(path);
    }

    @Override
    public ModeshapeNodeData readModeshapeNodeData(String path) {
        return doInSession(session -> {
            Node node = session.getNode(path);
            LOGGER.debug("Reading Modeshape node data: {}", path);
            ModeshapeNodeData data = new ModeshapeNodeData();
            data.setName(node.getName());
            data.setPath(path);
            getChildNodes(node, data);
            getProperties(node, data);
            getACL(session, node, data);
            return data;
        });
    }

    @Override
    public ModeshapeContentData readModeshapeNodeContent(String path) {
        return doInSession(session -> {
            Node node = session.getNode(path);
            LOGGER.debug("Getting Modeshape content: {}", path);
            Property property = node.getProperty("jcr:data");
            String parentName = property.getParent().getParent().getName();
            Binary binary = property.getValue().getBinary();
            return new ModeshapeContentData(parentName, IOUtils.toByteArray(binary.getStream()));
        });
    }

    @Override
    public void deleteModeshapeNode(String path) {
        doInSession(session -> {
            Node node = session.getNode(path);
            LOGGER.debug("Deleting Modeshape node: {}", path);
            node.remove();
            session.save();
            return null;
        });
    }

    @Override
    public void setPropertyOnNode(String path, ModeshapeProperty property) {
        doInSession(session -> {
            Node node = session.getNode(path);
            LOGGER.debug("Setting property {} with value {} on Modeshape node: {}", property.getName(), property.getValue(), path);
            node.setProperty(property.getName(), property.getValue());
            session.save();
            return null;
        });
    }

    @Override
    public void setAclEntryOnNode(String path, ModeshapeAclEntry aclEntry) {
        doInSession(session -> {
            Node node = session.getNode(path);
            LOGGER.debug("Setting ACL entry for principal {} with permissions {} on Modeshape node: {}", aclEntry.getPrincipal(), aclEntry.getPrivileges(), path);
            setACL(session, node, aclEntry);
            session.save();
            return null;
        });
    }

    @Override
    public void deletePropertyOnNode(String path, ModeshapeProperty property) {
        doInSession(session -> {
            LOGGER.debug("Deleting property {} on Modeshape node: {}", property.getName(), path);
            session.getNode(path)
                .getProperty(property.getName())
                .remove();
            session.save();
            return null;
        });
    }

    @Override
    public void deleteAclEntryOnNode(String path, ModeshapeAclEntry aclEntry) {
        doInSession(session -> {
            Node node = session.getNode(path);
            LOGGER.debug("Deleting ACL entry for principal {} on Modeshape node: {}", aclEntry.getPrincipal(), path);
            AccessControlManager accessControlManager = session.getAccessControlManager();
            AccessControlList acl = getAccessControlList(node, accessControlManager);
            Optional<AccessControlEntry> existingEntry = getAccessControlEntry(aclEntry, acl);
            if (existingEntry.isPresent()) {
                acl.removeAccessControlEntry(existingEntry.get());
            }
            accessControlManager.setPolicy(path, acl);
            session.save();
            return null;
        });
    }

    private void getACL(Session session, Node node, ModeshapeNodeData data)
        throws RepositoryException {

        AccessControlManager accessControlManager = session.getAccessControlManager();
        AccessControlList acl = getAccessControlList(node, accessControlManager);
        for (int i = 0; i < acl.getAccessControlEntries().length; i++) {
            ModeshapeAclEntry aclEntry = new ModeshapeAclEntry();
            aclEntry.setPrincipal(acl.getAccessControlEntries()[i].getPrincipal().getName());
            aclEntry.setPrivileges(transformToPrivilegeList(acl.getAccessControlEntries()[i]
                .getPrivileges()));
            data.addAclEntry(aclEntry);
        }
    }

    private AccessControlList getAccessControlList(Node node, AccessControlManager accessControlManager)
        throws RepositoryException {

        AccessControlPolicyIterator it = accessControlManager.getApplicablePolicies(node.getPath());
        AccessControlList acl;
        if (it.hasNext()) {
            acl = (AccessControlList) it.nextAccessControlPolicy();
        } else {
            acl = (AccessControlList) accessControlManager.getPolicies(node.getPath())[0];
        }
        return acl;
    }

    private void setACL(Session session, Node node, ModeshapeAclEntry aclEntry)
        throws RepositoryException {

        AccessControlManager accessControlManager = session.getAccessControlManager();
        AccessControlList acl = getAccessControlList(node, accessControlManager);
        Optional<AccessControlEntry> existingEntry = getAccessControlEntry(aclEntry, acl);
        if (existingEntry.isPresent()) {
            acl.removeAccessControlEntry(existingEntry.get());
        }
        List<Privilege> privileges = new ArrayList<>();
        for (String privilege : aclEntry.getPrivileges()) {
            privileges.add(accessControlManager.privilegeFromName(privilege));
        }
        acl.addAccessControlEntry(SimplePrincipal.newInstance(aclEntry.getPrincipal()), privileges.toArray(new Privilege[]{}));
        accessControlManager.setPolicy(node.getPath(), acl);
        session.save();
    }

    private Optional<AccessControlEntry> getAccessControlEntry(ModeshapeAclEntry aclEntry,
        AccessControlList acl) throws RepositoryException {

        return Arrays.stream(acl.getAccessControlEntries())
            .filter(entry -> entry.getPrincipal().getName().equals(aclEntry.getPrincipal()))
            .findAny();
    }

    private List<String> transformToPrivilegeList(Privilege[] privileges) {
        return Arrays.stream(privileges).map(Privilege::getName).collect(Collectors.toList());
    }

    private void getChildNodes(Node node, ModeshapeNodeData data) throws RepositoryException {
        NodeIterator nodeIterator = node.getNodes();
        while (nodeIterator.hasNext()) {
            Node childNode = nodeIterator.nextNode();
            LOGGER.debug("Child node name: {}", childNode.getName());
            data.addChildNode(childNode.getName());
        }
    }

    private void getProperties(Node node, ModeshapeNodeData data) throws RepositoryException {
        PropertyIterator propertyIterator = node.getProperties();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.nextProperty();
            LOGGER.debug("Property name: {}", property.getName());
            if ("jcr:data".equals(property.getName())) {
                data.setContentOnNode(true);
            }
            getPropertyValue(property, data);
        }
    }

    private void getPropertyValue(Property property, ModeshapeNodeData data)
        throws RepositoryException {

        if (property.isMultiple()) {
            for (Value value : property.getValues()) {
                LOGGER.debug("Property multi-value: {}", value);
                data.addProperty(property.getName(), value.toString());
            }
        } else {
            LOGGER.debug("Property value: {}", property.getValue());
            data.addProperty(property.getName(), property.getValue().toString());
        }
    }

}
