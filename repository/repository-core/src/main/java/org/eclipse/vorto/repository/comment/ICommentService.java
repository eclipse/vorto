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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.domain.Comment;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.web.api.v1.dto.CommentDTO;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface ICommentService {

  DateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm a dd-MM-yyyy");

  void createComment(String username, CommentDTO comment) throws DoesNotExistException, OperationForbiddenException;

  List<Comment> getCommentsforModelId(ModelId modelId);

  List<Comment> getCommentsByAuthor(String author);

  /**
   * Deletes the comment
   *
   * @param id
   * @param username
   * @return true if successful
   */
  boolean deleteComment(String username, long id) throws DoesNotExistException;

  /**
   * Authorizes the user to delete a given comment. <br/>
   * A user can delete a comment if:
   * <ul>
   *   <li>
   *     They are the author (and implicitly have the {@literal model_viewer} role on the namespace)
   *     as long as the comment has not been anonymized
   *   </li>
   *   <li>
   *     They have the {@literal namespace_admin} role on the namespace
   *   </li>
   *   <li>
   *     They have the {@literal sysadmin} role on the repository
   *   </li>
   * </ul>
   * @param username
   * @param comment
   * @return
   */
  boolean canDelete(String username, Comment comment);

  /**
   * Authorizes a user to create a comment if either of the following conditions applies:
   * <ul>
   *   <li>
   *     The user is {@literal sysadmin}
   *   </li>
   *   <li>
   *     The user has any role on the target namespace
   *   </li>
   *   <li>
   *     The model is public
   *   </li>
   * </ul>
   *
   * @param username
   * @param comment
   * @return
   */
  boolean canCreate(String username, Comment comment);

  void saveComment(Comment comment);


}
