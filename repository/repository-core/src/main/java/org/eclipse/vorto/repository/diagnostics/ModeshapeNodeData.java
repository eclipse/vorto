package org.eclipse.vorto.repository.diagnostics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModeshapeNodeData {

    private Map<String, String> properties = new HashMap<>();

    private String name;

    private List<String> childNodeNames = new ArrayList<>();

    private List<ModeshapeAclEntry> aclEntryList = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }

    public void addChildNode(String nodeName) {
        childNodeNames.add(nodeName);
    }

    public Map<String, String> getProperties() {
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
}
