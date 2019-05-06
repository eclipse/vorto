/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.notification.message;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.vorto.repository.domain.User;

public class OfficialNamespaceRequest extends AbstractMessage {

  private String tenantId;
  private String namespace;
  private String requestingUser;
  private Date requestDate;

  public OfficialNamespaceRequest(User recipient, String tenantId, String namespace,
      String username, Date dateRequested) {
    super(recipient);
    this.tenantId = tenantId;
    this.namespace = namespace;
    this.requestingUser = username;
    this.requestDate = dateRequested;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getRequestingUser() {
    return requestingUser;
  }

  public void setRequestingUser(String requestingUser) {
    this.requestingUser = requestingUser;
  }

  public Date getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(Date requestDate) {
    this.requestDate = requestDate;
  }

  @Override
  public String getSubject() {
    return String.format("Official Namespace Request from '%s'", requestingUser);
  }

  @Override
  public String getContent() {
    return String.format(
        "User '%s' has requested the official namespace '%s' for tenant '%d' on '%s'",
        requestingUser, namespace, tenantId,
        new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(requestDate));
  }

  @Override
  public String toString() {
    return "OfficialNamespaceRequest [tenantId=" + tenantId + ", namespace=" + namespace
        + ", requestingUser=" + requestingUser + ", requestDate=" + requestDate + "]";
  }
}
