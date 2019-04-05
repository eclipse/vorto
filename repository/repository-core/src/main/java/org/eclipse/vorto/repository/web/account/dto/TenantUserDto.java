package org.eclipse.vorto.repository.web.account.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.TenantUser;

public class TenantUserDto {
  
  public static TenantUserDto fromTenantUser(TenantUser user) {
    TenantUserDto tenantUser = new TenantUserDto();
    tenantUser.setUsername(user.getUser().getUsername());
    tenantUser.setRoles(user.getRoles().stream()
        .map(userRole -> "ROLE_" + userRole.getRole().toString())
        .collect(Collectors.toList()));
    return tenantUser;
  }
  
  private String username;
  private Collection<String> roles;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Collection<String> getRoles() {
    return roles;
  }

  public void setRoles(Collection<String> roles) {
    this.roles = roles;
  }
}
