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
package org.eclipse.vorto.repository.account.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.account.UserRole;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.DeleteAccountMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class DefaultUserAccountService implements IUserAccountService {

  private static final String USER_ANONYMOUS = "anonymous";

  @Value("${server.admin:#{null}}")
  private String admins;

  @Autowired
  private IUserRepository userRepository;

  @Autowired
  private IModelRepository modelRepository;

  @Autowired
  private INotificationService notificationService;

  public User create(String username) {
    if (userRepository.findByUsername(username) != null) {
      throw new IllegalArgumentException("User with given username already exists");
    }
    User user = createUser(username);
    user = userRepository.save(user);
    return user;
  }


  private User createUser(String username) {
    User user = new User();

    user.setUsername(username);
    user.setDateCreated(new Timestamp(System.currentTimeMillis()));
    user.setLastUpdated(new Timestamp(System.currentTimeMillis()));
    user.setAckOfTermsAndCondTimestamp(new Timestamp(System.currentTimeMillis()));
    user.addRoles(Role.USER, Role.MODEL_CREATOR, Role.MODEL_PROMOTER); // newly registered users may
                                                                       // fully interact with the
                                                                       // system
    if (isConfiguredAsAdmin(username)) {
      user.addRoles(Role.ADMIN);
    }
    return user;
  }

  private boolean isConfiguredAsAdmin(String username) {
    return admins != null && Arrays.asList(admins.split(";")).contains(username);
  }

  @Transactional
  public User create(String username, Role... userRoles) throws RoleNotSupportedException {
    User existingUser = userRepository.findByUsername(username);
    if (isUserAlreadyExistAndRoleNotProvided(existingUser, userRoles)) {
      throw new IllegalArgumentException("User with ID already exists");
    } else if (existingUser != null) {
      updateRole(existingUser, userRoles);
      return userRepository.save(existingUser);
    } else {
      User user = createUser(username);
      user.addRoles(userRoles);
      return userRepository.save(user);
    }
  }

  private void updateRole(User existingUser, Role[] userRoles) {
    Set<UserRole> existingRoles = existingUser.getRoles();
    removeDuplicateRoles(userRoles, existingRoles);
    existingUser.addRoles(userRoles);
  }

  private void removeDuplicateRoles(Role[] userRoles, Set<UserRole> existingRoles) {
    Arrays.asList(userRoles).forEach(role -> {
      existingRoles.removeIf(e -> e.getRole() == role);
    });
  }

  private boolean isUserAlreadyExistAndRoleNotProvided(User existingUser, Role[] userRoles) {
    return existingUser != null && Objects.isNull(userRoles);
  }

  @Transactional
  public User removeUserRole(String userName, List<Role> roles) {

    User user = userRepository.findByUsername(userName);
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("User Not Found: " + userName);
    }
    Set<UserRole> userRoles = user.getRoles();

    roles.forEach(role -> {
      userRoles.removeIf(e -> role == e.getRole());
    });

    user.setRoles(userRoles);

    return userRepository.save(user);
  }

  @Override
  public void delete(final String userId) {
    User userToDelete = userRepository.findByUsername(userId);

    if (userToDelete != null) {
      makeModelsAnonymous(UserContext.user(userToDelete.getUsername()).getHashedUsername());
      makeModelsAnonymous(userToDelete.getUsername());
      userRepository.delete(userToDelete);
    }

    if (userToDelete.hasEmailAddress()) {
      notificationService.sendNotification(new DeleteAccountMessage(userToDelete));
    }
  }

  private void makeModelsAnonymous(String username) {
    List<ModelInfo> userModels = this.modelRepository.search("author:" + username);

    for (ModelInfo model : userModels) {
      model.setAuthor(USER_ANONYMOUS);
      this.modelRepository.updateMeta(model);
    }
  }

  public IUserRepository getUserRepository() {
    return userRepository;
  }

  public void setUserRepository(IUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public IModelRepository getModelRepository() {
    return modelRepository;
  }

  public void setModelRepository(IModelRepository modelRepository) {
    this.modelRepository = modelRepository;
  }


  public INotificationService getNotificationService() {
    return notificationService;
  }


  public void setNotificationService(INotificationService notificationService) {
    this.notificationService = notificationService;
  }


  @Override
  public boolean exists(String userId) {
    return userRepository.findByUsername(userId) != null;
  }

  @Override
  public String getAnonymousUserId() {
    return USER_ANONYMOUS;
  }

  @Override
  public User getUser(String username) {
    return this.userRepository.findByUsername(username);
  }

  @Override
  public void saveUser(User user) {
    this.userRepository.save(user);
  }
}
