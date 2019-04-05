package org.eclipse.vorto.repository.core;

public class UserLoginException extends ModelRepositoryException {

  /**
   * 
   */
  private static final long serialVersionUID = -847932562471157259L;

  public UserLoginException(String user, Throwable cause) {
    super(user, cause);
  }

}
