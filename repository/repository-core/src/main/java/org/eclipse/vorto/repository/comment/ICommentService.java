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
package org.eclipse.vorto.repository.comment;

import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.domain.Comment;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface ICommentService {

  void createComment(Comment comment) throws Exception;

  List<Comment> getCommentsforModelId(ModelId modelId);

  List<Comment> getCommentsByAuthor(String author);

  /**
   * Deletes the comment
   *
   * @param id
   * @param username
   */
  void deleteComment(String username, long id)
      throws OperationForbiddenException, DoesNotExistException;

  void saveComment(Comment comment);
}
