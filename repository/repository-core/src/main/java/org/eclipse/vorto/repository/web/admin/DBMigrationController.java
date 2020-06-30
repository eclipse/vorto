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
package org.eclipse.vorto.repository.web.admin;

import static org.eclipse.vorto.repository.web.account.dto.DBMigrationResult.Status.FAILURE;
import static org.eclipse.vorto.repository.web.account.dto.DBMigrationResult.Status.SUCCESS;
import static org.eclipse.vorto.repository.web.account.dto.DBMigrationResult.Status.WARNING;

import com.google.common.base.Strings;
import org.eclipse.vorto.repository.web.account.dto.DBMigrationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/migration")
public class DBMigrationController {

  @Autowired
  private JdbcTemplate template;

  public static final String DROP_USER_ROLE = "drop table if exists user_role;";
  public static final String DROP_TENANT_USER_TABLE = "drop table if exists tenant_user;";
  public static final String DROP_TENANT_TABLE = "drop table if exists tenant;";

  /**
   * This format {@link String} queries for existing constraints on the {@literal tenant} table. <br/>
   *
   * @see DBMigrationController#NAMESPACE_TABLE_NAME
   */
  public static final String QUERY_TENANT_ID_CONSTRAINT_FORMAT =
      "select constraint_name from information_schema.key_column_usage where "
          + "referenced_table_name = 'tenant' and referenced_column_name = 'id' and "
          + "table_name = '%s' and column_name = 'tenant_id'";

  public static final String NAMESPACE_TABLE_NAME = "namespace";

  /**
   * Using {@link String#format(String, Object...)} instead of a {@link java.sql.PreparedStatement}
   * here, because the foreign key cannot be wrapped in single quotes.<br/>
   * This approach is safe as there is no data whatsoever coming from the requesting user - the
   * data is retrieved internally by querying the database.
   */
  public static final String UPDATE_REMOVE_TENANT_ID_COLUMN_FROM_NAMESPACE_FORMAT =
      "alter table namespace drop foreign key %s;";

  /**
   * This endpoint performs the following operations, in this order:
   * <ol>
   *   <li>
   *     Drops the {@code user_role} table if it exists. Failure unrecoverable - will abort.
   *   </li>
   *   <li>
   *     Searches for the constraint name of the foreign key binding the {@code tenant.ID} to the
   *     {@code namespace} table. Failure recoverable - will skip with a warning.
   *   </li>
   *   <li>
   *     If found, attempts to drop the foreign key. Failure unrecoverable - will abort.
   *   </li>
   *   <li>
   *     Drops the {@code tenant_id} field from the {@code namespace} table if it exists.
   *     Failure unrecoverable - will abort.
   *   </li>
   *   <li>
   *     Drops the {@code tenant_user} table if it exists. Failure unrecoverable - will abort.
   *   </li>
   *   <li>
   *     Drops the {@code tenant} table if it exists. Failure unrecoverable - will abort.
   *   </li>
   * </ol>
   * <br/>
   * Only available to {@literal sysadmin} users.<br/>
   * Should only be called after running the migration's initialization script, with an up-to-date
   * Vorto source.<br/>
   * The syntax here is tailored for MySQL (more specifically MariaDB) databases. <br/>
   * As documented elsewhere, the data migration is not suitable for e.g. H2 databases persisting in
   * the local file system, although in fairness, there is no expectation of any field usage for the
   * latter.
   * <br/>
   * Final note: for ease of use, this endpoint requires {@code GET}, although the operation
   * performed would imply something in between {@code POST} and {@code PUT}, in theory.
   *
   * @return the {@link DBMigrationResult} representing the result of this operation, with any relevant messages.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/cleanup")
  @PreAuthorize("hasAuthority('sysadmin')")
  public DBMigrationResult cleanup() {

    // initializing result
    DBMigrationResult result = new DBMigrationResult();

    // Step 1: drops the user_role table - no FK constraints here to be taken care of prior
    try {
      template.execute(DROP_USER_ROLE);
    } catch (DataAccessException dae) {
      return result.failure(dae.getMessage());
    }

    // Step 2: drops the foreign key binding the namespace table with the tenant table

    // fetches foreign key to tenant ID in namespace table
    String tenantIdConstraintInNamespace = null;
    try {
      tenantIdConstraintInNamespace = template
          .queryForObject(
              String.format(
                  QUERY_TENANT_ID_CONSTRAINT_FORMAT, NAMESPACE_TABLE_NAME
              ),
              String.class
          );

      // removes foreign key to tenant ID in namespace table
      if (!Strings.isNullOrEmpty(tenantIdConstraintInNamespace)) {
        try {
          template.execute(
              String.format(
                  UPDATE_REMOVE_TENANT_ID_COLUMN_FROM_NAMESPACE_FORMAT,
                  tenantIdConstraintInNamespace
              )
          );
          result.withMessage("namespace - tenant foreign key dropped.");
        }
        // unknown failure condition - should never occur at this point
        catch (DataAccessException dae) {
          return result
              .withStatus(FAILURE)
              .withMessage(dae.getMessage());
        }
      }
    }
    // constraint not found: most likely case here, the constraint has already been removed
    catch (IncorrectResultSizeDataAccessException irsdae) {
      result
          .withStatus(WARNING)
          .withMessage(
              "Constraint for tenant on namespace table could not be found. Skipping."
          );
    }

    // Step 3: drops the foreign key binding the tenant_user table with the tenant table
    try {
      template.execute(DROP_TENANT_USER_TABLE);
      result.withMessage("tenant_user table dropped (if existed).");
    } catch (DataAccessException dae) {
      return result.failure(dae.getMessage());
    }

    // Step 4: drops the tenant table
    try {
      template.execute(DROP_TENANT_TABLE);
      result.withMessage("tenant table dropped (if existed).");
    } catch (DataAccessException dae) {
      return result.failure(dae.getMessage());
    }

    if (result.getStatus() != WARNING) {
      result.withStatus(SUCCESS);
    }

    return result;
  }

}
