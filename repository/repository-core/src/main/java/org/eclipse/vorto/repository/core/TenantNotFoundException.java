package org.eclipse.vorto.repository.core;

public class TenantNotFoundException extends ModelRepositoryException {

  /**
   * 
   */
  private static final long serialVersionUID = 6204999779117905073L;

  public TenantNotFoundException(String tenantId, Throwable cause) {
    super("No tenant '" + tenantId + "'", cause);
  }

}
