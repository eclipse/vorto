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
package org.eclipse.vorto.repository.web.api.v1.util;

import com.google.common.base.Strings;
import java.util.Optional;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceOperationResult;

/**
 * As the name indicates, validates a namespace name based on whether:
 * <ul>
 *   <li>
 *     the user is a sysadmin and does not need to prepend {@link NamespaceValidator#NAMESPACE_PREFIX}
 *     to the namespace's name, and
 *   </li>
 *   <li>
 *     the actual namespace name is valid according to the {@link NamespaceValidator#VALID_NAMESPACE}
 *     pattern.
 *   </li>
 * </ul>
 * The {@link NamespaceValidator#validate(String, IUserContext)} method returns an {@link Optional}
 * of {@link NamespaceOperationResult} that will be empty if the namespace is valid, and
 */
public final class NamespaceValidator {
  public static final String NAMESPACE_PREFIX = "vorto.private.";
  public static final String VALID_NAMESPACE = "(\\p{Alnum}|_)+(\\.(\\p{Alnum}|_)+)*";
  public static Optional<NamespaceOperationResult> validate(String namespace, IUserContext context) {
    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return Optional.of(NamespaceOperationResult.failure("Empty namespace"));
    }
    if (!namespace.matches(VALID_NAMESPACE)) {
      return Optional.of(NamespaceOperationResult.failure("Invalid namespace notation."));
    }
    if (!context.isSysAdmin()) {
      if (!namespace.startsWith(NAMESPACE_PREFIX)) {
        return Optional.of(NamespaceOperationResult.failure("User can only register a private namespace."));
      }
    }
    return Optional.empty();
  }
}
