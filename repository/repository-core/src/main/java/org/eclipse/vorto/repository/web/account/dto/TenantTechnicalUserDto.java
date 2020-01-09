/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.web.account.dto;

/**
 * Represents a payload for requests to perform both:
 * <ul>
 *   <li>
 *     Create a new technical user with given name, roles and authentication provider ID
 *   </li>
 *   <li>
 *     Add to the given namespace
 *   </li>
 * </ul>
 * @author mena-bosch
 */
public class TenantTechnicalUserDto extends TenantUserDto {
  private String authenticationProviderId;
  public String getAuthenticationProviderId() {
    return authenticationProviderId;
  }
  public void setAuthenticationProviderId(String value) {
    this.authenticationProviderId = value;
  }
}
