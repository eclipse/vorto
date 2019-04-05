package org.eclipse.vorto.repository.tenant.repository;

import org.eclipse.vorto.repository.domain.Tenant;
import org.springframework.data.repository.CrudRepository;

public interface ITenantRepository extends CrudRepository<Tenant, Long>  {
  
  Tenant findByTenantId(String tenantId);
  
}
