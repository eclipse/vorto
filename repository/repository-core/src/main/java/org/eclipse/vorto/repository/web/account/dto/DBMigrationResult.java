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
