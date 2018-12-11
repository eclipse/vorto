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
package org.eclipse.vorto.repository.account;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public enum Role {
  USER, // minimal role for using the repository. It is restricted to only read access, but can
        // generate things and write comments
  ADMIN, // may execute admin functionality
  MODEL_CREATOR, // users with this role may create models in the system, import models, edit his
                 // own models and delete own models as long as they are not released
  MODEL_PROMOTER, // users with this role may start a release process for a model and deprecate a
                  // model
  MODEL_REVIEWER // user with this role may review models and either approve or reject them
}
