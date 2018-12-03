package org.eclipse.vorto.repository.core.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.eclipse.vorto.repository.core.IUserContext;

public class UserContext implements IUserContext {
  private String username;

  public static UserContext user(String username) {
    return new UserContext(username);
  }

  private UserContext() {}

  private UserContext(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  // TODO : Checking for hashedUsername is legacy and needs to be removed once full migration has
  // taken place
  public String getHashedUsername() {
    return getHash(username);
  }

  private String getHash(String username) {
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA-256");
      return bytesToHex(digest.digest(username.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Exception in getting has of username: " + username, e);
    }
  }

  private String bytesToHex(byte[] hash) {
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
    return this.username.equalsIgnoreCase("anonymous");
  }
}
