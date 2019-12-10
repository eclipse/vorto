package org.eclipse.vorto.repository.web.api.v1.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.TenantHasNoNamespaceException;

public class NamespaceDto {
  private String name;
  private Collection<Collaborator> collaborators;

  public static NamespaceDto fromTenant(Tenant tenant) {
    return new NamespaceDto(getNamespaceName(tenant), getCollaborators(tenant));
  }
  
  private static Collection<Collaborator> getCollaborators(Tenant tenant) {
    return tenant.getUsers().stream().map(Collaborator::fromUser).collect(Collectors.toList());
  }

  private static String getNamespaceName(Tenant tenant) {
    if (tenant.getNamespaces().size() <= 0) {
      throw new TenantHasNoNamespaceException(tenant.getTenantId());
    }
    
    // we are expected to only have 1 namespace per tenant
    return tenant.getNamespaces().iterator().next().getName();
  }
  
  public NamespaceDto(String name, Collection<Collaborator> collaborators) {
    this.name = name;
    this.collaborators = collaborators;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<Collaborator> getCollaborators() {
    return collaborators;
  }

  public void setCollaborators(Collection<Collaborator> collaborators) {
    this.collaborators = collaborators;
  }
}
