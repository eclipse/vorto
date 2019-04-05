package org.eclipse.vorto.repository.tenant.repository;

import java.util.List;
import org.eclipse.vorto.repository.domain.Namespace;
import org.springframework.data.repository.CrudRepository;

public interface INamespaceRepository extends CrudRepository<Namespace, Long> {
  
  Namespace findByName(String name);
  
  List<Namespace> findByNameIgnoreCaseContaining(String name);
  
}
