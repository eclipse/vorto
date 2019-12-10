package org.eclipse.vorto.repository.oauth;

import java.util.HashSet;
import java.util.Set;

public class OAuthUser {

  private String userId;
  private String displayName;
  private String email;
  private Set<String> roles = new HashSet<String>();
  
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public Set<String> getRoles() {
    return roles;
  }
  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }
  
}
