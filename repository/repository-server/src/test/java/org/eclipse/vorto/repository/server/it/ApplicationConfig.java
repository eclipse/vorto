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
package org.eclipse.vorto.repository.server.it;

import static org.eclipse.vorto.repository.domain.Role.MODEL_CREATOR;
import static org.eclipse.vorto.repository.domain.Role.MODEL_PROMOTER;
import static org.eclipse.vorto.repository.domain.Role.MODEL_REVIEWER;
import static org.eclipse.vorto.repository.domain.Role.SYS_ADMIN;
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

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private ITenantService tenantService;

  public static final String USER_ADMIN = "user1";
  public static final String USER_STANDARD = "user2";
  public static final String USER_CREATOR = "user3";
  public static final String NON_TENANT_USER = "user4";


  @PostConstruct
  public void createUsers() {
    accountService.create(USER_ADMIN);
    
    tenantService.createOrUpdateTenant("playground", "com.mycompany", Sets.newHashSet(USER_ADMIN), 
        Optional.of(Sets.newHashSet("com.mycompany", "com.ipso", "examples.mappings", "com.test")), 
        Optional.of("GITHUB"), Optional.of("DB"), createUserContext(USER_ADMIN, "playground"));
    
    accountService.create(USER_ADMIN, "playground", USER, SYS_ADMIN, MODEL_CREATOR, MODEL_PROMOTER, MODEL_REVIEWER);
    accountService.create(USER_STANDARD, "playground", USER);
    accountService.create(USER_CREATOR, "playground", USER, MODEL_CREATOR);
  }
  
  protected IUserContext createUserContext(String username, String tenantId) {
    Authentication auth = new TestingAuthenticationToken(username, username, 
        new String[] { Role.rolePrefix + SYS_ADMIN.name() });

    return UserContext.user(auth, tenantId);
  }
}
