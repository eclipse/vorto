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
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceAccessRequestDTO;
import org.eclipse.vorto.repository.web.api.v1.dto.OperationResult;

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
 * of {@link OperationResult} that will be empty if the namespace is valid, and
 */
public final class NamespaceValidator {
  public static final String NAMESPACE_PREFIX = "vorto.private.";
  public static final String VALID_NAMESPACE = "(\\p{Alnum}|_)+(\\.(\\p{Alnum}|_)+)*";
  public static Optional<OperationResult> validate(String namespace, IUserContext context) {
    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return Optional.of(OperationResult.failure("Empty namespace"));
    }
    if (!namespace.matches(VALID_NAMESPACE)) {
      return Optional.of(OperationResult.failure("Invalid namespace notation."));
    }
    if (!context.isSysAdmin()) {
      if (!namespace.startsWith(NAMESPACE_PREFIX)) {
        return Optional.of(OperationResult.failure("User can only register a private namespace."));
      }
    }
    return Optional.empty();
  }

  /**
   * Validates a request to add a user to a namespace.
   * @param request
   * @return
   */
  public static Optional<OperationResult> validateAccessRequest(NamespaceAccessRequestDTO request) {
    if (request == null) {
      return Optional.of(OperationResult.failure("Cannot process request"));
    }
    /*
     This is a dumb representation of the "conditions acknowledged" checkbox in the UI. The idea
     here is that if a user "maliciously" enables the "send" button through scripting and thus
     bypasses the validation of the checkbox, the value sent to the back-end can still be
     evaluated.
     Of course, one could also spoof the request and enable the flag, but in that case, the
     conditions should be considered as acknowledged, since the clever user obviously knows what
     they are doing.
     */
    if (!request.isConditionsAcknowledged()) {
      return Optional.of(OperationResult.failure("Conditions not acknowledged"));
    }
    if (Strings.nullToEmpty(request.getNamespaceName()).trim().isEmpty()) {
      return Optional.of(OperationResult.failure("No namespace specified"));
    }
    if (Strings.nullToEmpty(request.getRequestingUsername()).trim().isEmpty()) {
      return Optional.of(OperationResult.failure("No requesting user specified"));
    }
    if (Strings.nullToEmpty(request.getTargetUsername()).trim().isEmpty()) {
      return Optional.of(OperationResult.failure("No target user specified"));
    }
    return Optional.empty();
  }
}
