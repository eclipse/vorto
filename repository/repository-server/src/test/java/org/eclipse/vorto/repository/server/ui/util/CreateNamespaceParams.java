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
package org.eclipse.vorto.repository.server.ui.util;

import org.eclipse.vorto.repository.server.ui.SeleniumVortoHelper;

/**
 * Utility builder with namespace creation parametrization
 */
public class CreateNamespaceParams {

  private String username;
  private String password;
  private String namespaceName;

  public static final String DEFAULT_USERNAME = "user1";
  public static final String DEFAULT_PASSWORD = "pass";
  public static final String DEFAULT_NAMESPACE_NAME =
      SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE;

  public static final CreateNamespaceParams DEFAULT = new CreateNamespaceParams(
      DEFAULT_USERNAME,
      DEFAULT_PASSWORD,
      DEFAULT_NAMESPACE_NAME
  );


  public CreateNamespaceParams() {
  }

  public CreateNamespaceParams(String username, String password, String namespaceName) {
    this.username = username;
    this.password = password;
    this.namespaceName = namespaceName;
  }

  public static CreateNamespaceParams defaults() {
    return DEFAULT;
  }

  public CreateNamespaceParams withUsername(String username) {
    this.username = username;
    return this;
  }

  public CreateNamespaceParams withPassword(String password) {
    this.password = password;
    return this;
  }

  public CreateNamespaceParams withNamespaceName(String namespaceName) {
    this.namespaceName = namespaceName;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNamespaceName() {
    return namespaceName;
  }

  public void setNamespaceName(String namespaceName) {
    this.namespaceName = namespaceName;
  }
}
