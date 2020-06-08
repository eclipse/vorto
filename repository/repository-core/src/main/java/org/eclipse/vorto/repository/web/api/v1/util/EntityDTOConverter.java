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

import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.UserBuilder;
import org.eclipse.vorto.repository.services.UserUtil;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Utility service providing all entity to DTO and vice-versa conversions the controllers require.
 */
public class EntityDTOConverter {

    private EntityDTOConverter() {
        // this class contains only static methods.
    }

  /**
   * Conveniently builds a {@link NamespaceDto} from a given {@link Namespace} entity, and a
   * given {@link Map} of {@link User}s and {@link IRole}s, as retrieved from e.g.
   * {@link org.eclipse.vorto.repository.services.UserNamespaceRoleService#getNamespacesCollaboratorsAndRoles(User, User, Long)}.<br/>
   * The {@link Collaborator} and roles are sorted lexicographically.
   *
   * @param namespace
   * @param usersAndRoles
   * @return
   */
  public static NamespaceDto createNamespaceDTO(Namespace namespace,
      Map<User, Collection<IRole>> usersAndRoles) {
    Collection<Collaborator> collaborators = createCollaborators(usersAndRoles);
    Collection<Collaborator> admins = collaborators.stream()
        .filter(c -> c.getRoles().contains("namespace_admin")).collect(Collectors
            .toCollection(() -> new TreeSet<>(Comparator.comparing(Collaborator::getUserId))));
    return new NamespaceDto(namespace.getName(), collaborators, admins);
  }

  public static Collection<Collaborator> createCollaborators(Map<User, Collection<IRole>> usersAndRoles) {
    Collection<Collaborator> result = new TreeSet<>(Comparator.comparing(Collaborator::getUserId));
    usersAndRoles.forEach(
        (u, c) -> {
          Collaborator collaborator = new Collaborator();
          collaborator.setAuthenticationProviderId(u.getAuthenticationProviderId());
          collaborator.setRoles(c.stream().map(IRole::getName).collect(Collectors.toSet()));
          collaborator.setSubject(u.getSubject());
          collaborator.setTechnicalUser(u.isTechnicalUser());
          collaborator.setUserId(u.getUsername());
          result.add(collaborator);
        }
    );
    return result;
  }

  public static User createUser(UserUtil userUtil, Collaborator collaborator) throws InvalidUserException {
    return new UserBuilder(userUtil)
        .withAuthenticationProviderID(collaborator.getAuthenticationProviderId())
        .withAuthenticationSubject(collaborator.getSubject())
        .withName(collaborator.getUserId())
        .setTechnicalUser(collaborator.isTechnicalUser())
        .build();
  }

}
