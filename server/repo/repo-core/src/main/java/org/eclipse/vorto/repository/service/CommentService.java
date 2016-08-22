/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.service;

import java.util.Date;
import java.util.List;

import javax.jcr.PathNotFoundException;

import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.repository.internal.service.CommentRepository;
import org.eclipse.vorto.repository.model.Comment;
import org.eclipse.vorto.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class CommentService implements ICommentService{

	@Autowired
	private IModelRepository modelRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public void createComment(Comment comment) {
		
		final ModelId id = ModelId.fromPath(comment.getModelId());
		
		if (modelRepository.getById(id) != null){	
			comment.setModelId(id.getFullPath());
			comment.setDate(new Date());
			commentRepository.save(comment);
		} else {
			throw new ModelNotFoundException("Model not found", new PathNotFoundException());
		}		
	}
	
	public List<Comment> getCommentsforModelId(ModelId modelId){
		
		for (Comment comment : commentRepository.findByModelId(modelId.getFullPath())){
			final User user = userRepository.findByUsername(comment.getAuthor());
			if (user.getFirstName() != null && user.getLastName() != null){
				comment.setFirstname(user.getFirstName());
				comment.setLastname(user.getLastName());
			}
		}
		
		return commentRepository.findByModelId(modelId.getFullPath());		
	}
}
