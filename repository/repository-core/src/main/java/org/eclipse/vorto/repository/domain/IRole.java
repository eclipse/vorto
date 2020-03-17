package org.eclipse.vorto.repository.domain;

/**
 * Common interface for roles, i.e. {@link NamespaceRole} and {@link RepositoryRole}.<br/>
 * TODO rename to Role without hungarian notation once homonymous enum removed
 */
public interface IRole {
  String getName();
  long getPrivileges();
}
