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
import java.util.List;

public class ModeshapeNodeData {

    private List<ModeshapeProperty> properties = new ArrayList<>();

    private String name;

    private String path;

    private List<String> childNodeNames = new ArrayList<>();

    private List<ModeshapeAclEntry> aclEntryList = new ArrayList<>();

    private boolean contentOnNode = false;

    public void setName(String name) {
        this.name = name;
    }

    public void addProperty(String name, String value) {
        ModeshapeProperty property = new ModeshapeProperty(name, value);
        properties.add(property);
    }

    public void addChildNode(String nodeName) {
        childNodeNames.add(nodeName);
    }

    public List<ModeshapeProperty> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public List<String> getChildNodeNames() {
        return childNodeNames;
    }

    public List<ModeshapeAclEntry> getAclEntryList() {
        return aclEntryList;
    }

    public void addAclEntry(ModeshapeAclEntry aclEntry) {
        this.aclEntryList.add(aclEntry);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean isContentOnNode() {
        return contentOnNode;
    }

    public void setContentOnNode(boolean contentOnNode) {
        this.contentOnNode = contentOnNode;
    }
}
