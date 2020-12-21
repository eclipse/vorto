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
package org.eclipse.vorto.repository.comment.impl;

import java.util.List;
import org.eclipse.vorto.repository.domain.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

  List<Comment> findByModelId(String modelId);

  @Query(
      "select c from Comment c join c.author u where u.username = :username and u.authenticationProviderId = :authenticationProviderID"
  )
  List<Comment> findByAuthor(
      @Param("username") String username,
      @Param("authenticationProviderID") String authenticationProviderID
  );

  void delete(Long id);
}
