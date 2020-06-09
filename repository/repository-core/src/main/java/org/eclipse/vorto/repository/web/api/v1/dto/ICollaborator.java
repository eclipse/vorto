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
package org.eclipse.vorto.repository.web.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;

/**
 * This interface is meant as a glue between {@link Collaborator} and {@link org.eclipse.vorto.repository.web.account.dto.TenantUserDto}
 * POJOs, since they have the exact same properties but are used in different contexts. <br/>
 * This will be obsolete when the back-end side of the "tenant  to namespace" refactory is completed. <br/>
 * At this time, the interface will allow passing {@link Collaborator} instances to the
 * {@link DefaultUserAccountService}, when e.g. creating a technical user with the new
 * {@link org.eclipse.vorto.repository.web.api.v1.NamespaceController}.
 */
public interface ICollaborator {
  String getUserId();
  void setUserId(String userId);
  String getAuthenticationProviderId();
  void setAuthenticationProviderId(String providerId);
  Collection<String> getRoles();
  void setRoles(Collection<String> roles);
  String getSubject();
  void setSubject(String subject);
  @JsonProperty("isTechnicalUser")
  boolean isTechnicalUser();
  @JsonProperty("isTechnicalUser")
  void setTechnicalUser(boolean isTechnicalUser);

}
