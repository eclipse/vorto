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
package org.eclipse.vorto.repository.server.it;

import static org.eclipse.vorto.repository.domain.Role.MODEL_CREATOR;
import static org.eclipse.vorto.repository.domain.Role.MODEL_PROMOTER;
import static org.eclipse.vorto.repository.domain.Role.MODEL_REVIEWER;
import static org.eclipse.vorto.repository.domain.Role.SYS_ADMIN;
import static org.eclipse.vorto.repository.domain.Role.TENANT_ADMIN;
import static org.eclipse.vorto.repository.domain.Role.USER;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.google.common.collect.Sets;

@Configuration
public class ApplicationConfig {

  private static final String GITHUB = "GITHUB";

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private ITenantService tenantService;

  public static final String USER_ADMIN = "user1";
  public static final String USER_STANDARD = "user2";
  public static final String USER_STANDARD2 = "userstandard2";
  public static final String USER_STANDARD3 = "userstandard3";
  public static final String USER_STANDARD4 = "userstandard4";
  public static final String USER_STANDARD5 = "userstandard5";
  public static final String USER_STANDARD6 = "userstandard6";
  public static final String USER_STANDARD7 = "userstandard7";
  public static final String USER_STANDARD8 = "userstandard8";
  public static final String USER_CREATOR = "user3";
  public static final String USER_CREATOR2 = "usercreator2";
  public static final String USER_CREATOR3 = "usercreator3";
  public static final String NON_TENANT_USER = "user4";


  @PostConstruct
  public void createUsers() {
    accountService.create(USER_ADMIN, GITHUB, null);
    
    tenantService.createOrUpdateTenant("playground", "com.mycompany", Sets.newHashSet(USER_ADMIN), 
        Optional.of(Sets.newHashSet("com.mycompany", "com.ipso", "examples.mappings", "com.test")), 
        Optional.of(GITHUB), Optional.of("DB"), createUserContext(USER_ADMIN, "playground"));
    
    accountService.createOrUpdate(USER_ADMIN, GITHUB, null, 
        "playground", USER, SYS_ADMIN, TENANT_ADMIN, MODEL_CREATOR, MODEL_PROMOTER, MODEL_REVIEWER);
    accountService.createOrUpdate(USER_STANDARD, GITHUB, null,
        "playground", USER);
    accountService.createOrUpdate(USER_STANDARD2, GITHUB, null,
        "playground", USER);
    accountService.createOrUpdate(USER_STANDARD3, GITHUB, null,
        "playground", USER);
    accountService.createOrUpdate(USER_STANDARD4, GITHUB, null,
        "playground", USER);
    accountService.createOrUpdate(USER_STANDARD5, GITHUB, null,
        "playground", USER);
    accountService.createOrUpdate(USER_STANDARD6, GITHUB, null,
        "playground", USER);
    accountService.createOrUpdate(USER_STANDARD7, GITHUB, null,
        "playground", USER);
    accountService.createOrUpdate(USER_STANDARD8, GITHUB, null,
        "playground", USER);
    accountService.createOrUpdate(USER_CREATOR, GITHUB, null,
        "playground", USER, MODEL_CREATOR);
    accountService.createOrUpdate(USER_CREATOR2, GITHUB, null,
        "playground", USER, MODEL_CREATOR);
    accountService.createOrUpdate(USER_CREATOR3, GITHUB, null,
        "playground", USER, MODEL_CREATOR);
    
  }
  
  protected IUserContext createUserContext(String username, String tenantId) {
    Authentication auth = new TestingAuthenticationToken(username, username, 
        new String[] { Role.rolePrefix + SYS_ADMIN.name() });

    return UserContext.user(auth, tenantId);
  }
}
