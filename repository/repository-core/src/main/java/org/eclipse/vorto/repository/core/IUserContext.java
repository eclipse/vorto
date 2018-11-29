package org.eclipse.vorto.repository.core;

public interface IUserContext {

  String getUsername();

  // TODO : Checking for hashedUsername is legacy and needs to be removed once full migration has
  // taken place
  String getHashedUsername();

  boolean isAnonymous();
}
