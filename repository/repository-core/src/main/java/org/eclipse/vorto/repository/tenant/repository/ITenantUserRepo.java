package org.eclipse.vorto.repository.tenant.repository;

import org.eclipse.vorto.repository.domain.TenantUser;
import org.springframework.data.repository.CrudRepository;

public interface ITenantUserRepo extends CrudRepository<TenantUser, Long> {

}
