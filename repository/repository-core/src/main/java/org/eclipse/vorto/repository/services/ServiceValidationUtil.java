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
package org.eclipse.vorto.repository.services;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.springframework.stereotype.Service;

/**
 * Utility class with boilerplate validation code for services.
 */
@Service
public class ServiceValidationUtil {

  /**
   * Simple wrapper for boilerplate null validation of given arguments.<br/>
   * Traverses collections and checks elements for null too if so configured.
   *
   * @param arguments
   * @throws IllegalArgumentException
   */
  public void validateNulls(boolean traverseCollections, Object... arguments)
      throws IllegalArgumentException {
    // args
    if (Stream.of(arguments).anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("At least one argument is null");
    }
    if (traverseCollections) {
      // non-null args that are collections
      Stream.of(arguments).filter(a -> a instanceof Collection).forEach(c -> {
        if (((Collection) c).stream().anyMatch(Objects::isNull)) {
          throw new IllegalArgumentException("At least one element is null");
        }
      });
    }
  }

  /**
   * Traverses collections.
   *
   * @param arguments
   * @throws IllegalArgumentException
   * @see ServiceValidationUtil#validateNulls(boolean, Object...)
   */
  public void validateNulls(Object... arguments) throws IllegalArgumentException {
    validateNulls(true, arguments);
  }

  /**
   * First validates arguments for {@code null}s, then validates that their {@link String}
   * representation trimmed of whitespace is not empty.
   *
   * @param traverseCollections
   * @param arguments
   * @throws IllegalArgumentException
   */
  public void validateEmpties(boolean traverseCollections, Object... arguments)
      throws IllegalArgumentException {
    validateNulls(traverseCollections, arguments);
    if (Stream.of(arguments).anyMatch(o -> o.toString().trim().isEmpty())) {
      throw new IllegalArgumentException("At least one value is empty.");
    }
    if (traverseCollections) {
      // non-null args that are collections
      Stream.of(arguments).filter(a -> a instanceof Collection).forEach(c -> {
        if (((Collection) c).stream().anyMatch(o -> o.toString().trim().isEmpty())) {
          throw new IllegalArgumentException("At least one value is empty.");
        }
      });
    }
  }

  /**
   * Traverses collections.
   *
   * @param arguments
   * @throws IllegalArgumentException
   * @see ServiceValidationUtil#validateEmpties(boolean, Object...)
   */
  public void validateEmpties(Object... arguments) throws IllegalArgumentException {
    validateEmpties(true, arguments);
  }

  /**
   * Convenience utility to throw {@link DoesNotExistException} with a specific message if a
   * user does not exist. <br/>
   * The usage implication is that the user has been retrieved by name through a repository
   * call.
   *
   * @param user
   * @throws DoesNotExistException
   */
  public void validateUser(User user) throws DoesNotExistException {
    if (Objects.isNull(user)) {
      throw new DoesNotExistException("User does not exist");
    }
  }

  /**
   * Convenience utility to throw {@link DoesNotExistException} with a specific message if a
   * namespace does not exist. <br/>
   * The usage implication is that the namespace has been retrieved by name through a repository
   * call.
   *
   * @param namespace
   * @throws DoesNotExistException
   */
  public void validateNamespace(Namespace namespace) throws DoesNotExistException {
    if (Objects.isNull(namespace)) {
      throw new DoesNotExistException("Namespace does not exist.");
    }
  }

  /**
   * Convenience utility to throw {@link DoesNotExistException} with a specific message if a
   * role does not exist. <br/>
   * The usage implication is that the role has been retrieved by name through a repository
   * call.
   *
   * @param role
   * @throws DoesNotExistException
   */
  public void validateRole(IRole role) throws DoesNotExistException {
    if (Objects.isNull(role)) {
      throw new DoesNotExistException("Role does not exist.");
    }
  }

  /**
   * @param user
   * @param namespace
   * @throws DoesNotExistException
   * @see ServiceValidationUtil#validateUser(User)
   * @see ServiceValidationUtil#validateNamespace(Namespace)
   */
  public void validate(User user, Namespace namespace) throws DoesNotExistException {
    validateUser(user);
    validateNamespace(namespace);
  }

  /**
   * @param actor
   * @param target
   * @param namespace
   * @throws DoesNotExistException
   * @see ServiceValidationUtil#validateUser(User)
   * @see ServiceValidationUtil#validateNamespace(Namespace)
   */
  public void validate(User actor, User target, Namespace namespace) throws DoesNotExistException {
    validateUser(actor);
    validateUser(target);
    validateNamespace(namespace);
  }

  /**
   * @param user
   * @param namespace
   * @param role
   * @throws DoesNotExistException
   * @see ServiceValidationUtil#validateUser(User)
   * @see ServiceValidationUtil#validateNamespace(Namespace)
   * @see ServiceValidationUtil#validateRole(IRole)
   */
  public void validate(User user, Namespace namespace, IRole role) throws DoesNotExistException {
    validateUser(user);
    validateNamespace(namespace);
    validateRole(role);
  }

  /**
   * @param actor
   * @param target
   * @param namespace
   * @param role
   * @throws DoesNotExistException
   * @see ServiceValidationUtil#validateUser(User)
   * @see ServiceValidationUtil#validateNamespace(Namespace)
   * @see ServiceValidationUtil#validateRole(IRole)
   */
  public void validate(User actor, User target, Namespace namespace, IRole role)
      throws DoesNotExistException {
    validate(actor, target);
    validateNamespace(namespace);
    validateRole(role);
  }

  /**
   * @param users
   * @throws DoesNotExistException
   * @see ServiceValidationUtil#validateUser(User)
   */
  public void validate(User... users) throws DoesNotExistException {
    if (users != null && users.length > 0) {
      for (User user : users) {
        validateUser(user);
      }
    }
  }
}
