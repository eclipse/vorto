package org.eclipse.vorto.repository.web.api.v1.dto;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.tenant.TenantHasNoNamespaceException;

public class NamespaceDto {
  private String name;
  private String tenantId;
  private Map<String, String> tenantAdmins;

  public static NamespaceDto fromTenant(Tenant tenant) {
    return new NamespaceDto(getNamespaceName(tenant), tenant.getTenantId(), getAdmins(tenant));
  }
  
  private static Map<String, String> getAdmins(Tenant tenant) {
    Map<String, String> admins = new HashMap<>();
    if (tenant.getTenantAdmins() != null) {
      for (User user : tenant.getTenantAdmins()) {
        admins.put(user.getUsername(), user.getAuthenticationProvider());
      }
    }
    return admins;
  }

  private static String getNamespaceName(Tenant tenant) {
    if (tenant.getNamespaces().size() <= 0) {
      throw new TenantHasNoNamespaceException(tenant.getTenantId());
    }
    
    // we are expected to only have 1 namespace per tenant
    return tenant.getNamespaces().iterator().next().getName();
  }
  
  public NamespaceDto(String name, String tenantId, Map<String, String> tenantAdmins) {
    this.name = name;
    this.tenantId = tenantId;
    this.tenantAdmins = tenantAdmins;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Map<String, String> getTenantAdmins() {
    return tenantAdmins;
  }

  public void setTenantAdmins(Map<String, String> tenantAdmins) {
    this.tenantAdmins = tenantAdmins;
  }
}
