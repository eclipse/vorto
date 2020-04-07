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

import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides functionalities specific to user manipulation.<br/>
 */
@Service
public class UserService {

  @Autowired
  private UserUtil userUtil;

  @Autowired
  private ServiceValidationUtil validator;

  @Autowired
  private UserRepository userRepository;

  public User createTechnicalUser(User technicalUser) throws InvalidUserException {
    validator.validateNulls(technicalUser);

    // validates technical user
    userUtil.validateTechnicalUser(technicalUser);

    // save the technical user
    return userRepository.save(technicalUser);
  }

}
