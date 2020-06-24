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
package org.eclipse.vorto.repository.search.utils;

import org.eclipse.vorto.repository.domain.NamespaceRole;

public class RoleProvider {

  public static NamespaceRole namespaceAdmin() {
    NamespaceRole namespace_admin = new NamespaceRole();
    namespace_admin.setName("namespace_admin");
    namespace_admin.setPrivileges(7);
    namespace_admin.setRole(32);
    return namespace_admin;
  }

  public static NamespaceRole modelViewer() {
    NamespaceRole model_viewer = new NamespaceRole();
    model_viewer.setName("model_viewer");
    model_viewer.setPrivileges(1);
    model_viewer.setRole(1);
    return model_viewer;
  }

  public static NamespaceRole modelCreator() {
    NamespaceRole model_creator = new NamespaceRole();
    model_creator.setName("model_creator");
    model_creator.setPrivileges(3);
    model_creator.setRole(2);
    return model_creator;
  }

  public static NamespaceRole modelPromoter() {
    NamespaceRole model_promoter = new NamespaceRole();
    model_promoter.setName("model_promoter");
    model_promoter.setPrivileges(3);
    model_promoter.setRole(4);
    return model_promoter;
  }

  public static NamespaceRole modelPublisher() {
    NamespaceRole model_publisher = new NamespaceRole();
    model_publisher.setName("model_publisher");
    model_publisher.setPrivileges(3);
    model_publisher.setRole(4);
    return model_publisher;
  }

  public static NamespaceRole modelReviewer() {
    NamespaceRole model_reviewer = new NamespaceRole();
    model_reviewer.setName("model_reviewer");
    model_reviewer.setPrivileges(3);
    model_reviewer.setRole(8);
    return model_reviewer;
  }
}
