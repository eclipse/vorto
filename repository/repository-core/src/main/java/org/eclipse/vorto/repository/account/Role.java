/**
 * \ * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
