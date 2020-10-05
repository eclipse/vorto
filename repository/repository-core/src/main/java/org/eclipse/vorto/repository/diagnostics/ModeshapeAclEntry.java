package org.eclipse.vorto.repository.diagnostics;

import java.util.ArrayList;
import java.util.List;

public class ModeshapeAclEntry {

    private String principal;

    private List<String> privileges = new ArrayList<>();

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public List<String> getPrivileges() {
        return privileges;
    }

    public void addPrivilege(String privilege) {
        this.privileges.add(privilege);
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }
}
