package org.eclipse.vorto.repository.web.tenant.dto;

import java.util.Set;

public class NamespacesRequest {

  private Set<String> namespaces;

  public Set<String> getNamespaces() {
    return namespaces;
  }

  public void setNamespaces(Set<String> namespaces) {
    this.namespaces = namespaces;
  }
}
