package org.eclipse.vorto.repository.core.impl;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractModelService {
 
  private ModelRepositoryFactory repoFactory;
  
  private ITenantService tenantService;
  
  private Supplier<Authentication> authenticationSupplier =
      () -> SecurityContextHolder.getContext().getAuthentication();
  
  public AbstractModelService(ITenantService tenantService,
      ModelRepositoryFactory repoFactory) {
    this.tenantService = tenantService;
    this.repoFactory = repoFactory;
  }

  protected Collection<String> getTenants() {
    return tenantService.getTenants().stream().map(Tenant::getTenantId)
        .collect(Collectors.toList());
  }
  
  protected IModelRepository getRepository(String tenantId) {
    return repoFactory.getRepository(tenantId, authenticationSupplier.get());
  }

  public void setRepoFactory(ModelRepositoryFactory repoFactory) {
    this.repoFactory = repoFactory;
  }

  public void setTenantService(ITenantService tenantService) {
    this.tenantService = tenantService;
  }

  public void setAuthenticationSupplier(Supplier<Authentication> authenticationSupplier) {
    this.authenticationSupplier = authenticationSupplier;
  }
  
}
