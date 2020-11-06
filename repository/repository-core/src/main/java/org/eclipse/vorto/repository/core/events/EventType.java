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
package org.eclipse.vorto.repository.core.events;

public enum EventType {
  USER_DELETED,
  USER_MODIFIED,
  USER_ADDED,
  @Deprecated
  TENANT_ADDED,
  @Deprecated
  TENANT_UPDATED,
  @Deprecated
  TENANT_DELETED,
  MODEL_CREATED,
  MODEL_UPDATED,
  MODEL_DELETED,
  NAMESPACE_ADDED,
  NAMESPACE_UPDATED,
  NAMESPACE_DELETED,
  // when a user loses all roles on a namespace
  USER_REMOVED_FROM_NAMESPACE
}
