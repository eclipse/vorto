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
package org.eclipse.vorto.repository.web.account.dto;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Expresses the outcome of a database migration operation performed through the
 * {@link org.eclipse.vorto.repository.web.admin.DBMigrationController}.
 */
public class DBMigrationResult {

  public enum Status {
    SUCCESS, FAILURE, WARNING
  }

  private Status status;
  private Set<String> messages = new LinkedHashSet<>();

  public static DBMigrationResult success() {
    return new DBMigrationResult().withStatus(Status.SUCCESS).withMessage("");
  }

  public static DBMigrationResult success(String message) {
    return new DBMigrationResult().withStatus(Status.SUCCESS).withMessage(message);
  }

  public static DBMigrationResult failure(String message) {
    return new DBMigrationResult().withStatus(Status.FAILURE).withMessage(message);
  }

  public static DBMigrationResult warning(String message) {
    return new DBMigrationResult().withStatus(Status.WARNING).withMessage(message);
  }

  public Status getStatus() {
    return status;
  }

  public DBMigrationResult withStatus(Status status) {
    this.status = status;
    return this;
  }

  public Set<String> getMessages() {
    return messages;
  }

  public DBMigrationResult withMessage(String message) {
    this.messages.add(message);
    return this;
  }
}
