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
package org.eclipse.vorto.repository.core.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.springframework.security.core.Authentication;

public class UserContext implements IUserContext {

  private String username;
  private String workspaceId;
  private Authentication authentication;

  private static final List<String> ANONYMOUS_USERS =
      Arrays.asList("anonymous", "anonymousUser", getHash("anonymous"), getHash("anonymousUser"));

  public static UserContext user(String username, String workspaceId) {
    return new UserContext(username, workspaceId);
  }

  public static UserContext user(Authentication authentication, String workspaceId) {
    return new UserContext(authentication, workspaceId);
  }
  
  public static UserContext user(Authentication authentication) {
    return new UserContext(authentication, null);
  }

  private UserContext() {}

  private UserContext(Authentication authentication, String workspaceId) {
    this(authentication.getName(), workspaceId);
    this.authentication = authentication;
  }

  private UserContext(String username, String workspaceId) {
    this.username = username;
    this.workspaceId = workspaceId;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getHashedUsername() {
    return getHash(username);
  }

  @Override
  public String getWorkspaceId() {
    return workspaceId;
  }

  @Override
  public Authentication getAuthentication() {
    return authentication;
  }

  public static String getHash(String username) {
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA-256");
      return bytesToHex(digest.digest(username.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Exception in getting has of username: " + username, e);
    }
  }

  private static String bytesToHex(byte[] hash) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if (hex.length() == 1)
        hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString();
  }

  @Override
  public boolean isAnonymous() {
    return ANONYMOUS_USERS.contains(this.username);
  }

  public static boolean isAnonymous(String username) {
    return ANONYMOUS_USERS.contains(username);
  }

  @Override
  public boolean isSysAdmin() {
    return getAuthentication() != null && isSysAdmin(getAuthentication());
  }
  
  public static boolean isSysAdmin(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals(RepositoryRole.SYS_ADMIN.getName()));
  }
}
