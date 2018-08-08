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
package org.eclipse.vorto.repository.comment.impl;

import java.util.Date;
import java.util.List;

import javax.jcr.PathNotFoundException;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.comment.Comment;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class DefaultCommentService implements ICommentService{

	@Autowired
	private IModelRepository modelRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	public void createComment(Comment comment) {
		
		final ModelId id = ModelId.fromPrettyFormat(comment.getModelId());
		
		if (modelRepository.getById(id) != null){
			comment.setAuthor(comment.getAuthor());
			comment.setModelId(id.getPrettyFormat());
			comment.setDate(new Date());
			commentRepository.save(comment);
		} else {
			throw new ModelNotFoundException("Model not found", new PathNotFoundException());
		}		
	}
	
	public List<Comment> getCommentsforModelId(ModelId modelId){
		return commentRepository.findByModelId(modelId.getPrettyFormat());		
	}
	
	public List<Comment> getCommentsByAuthor(String author) {
		return commentRepository.findByAuthor(author);
	}

	@Override
	public void saveComment(Comment comment) {
		this.commentRepository.save(comment);	
	}

	public IModelRepository getModelRepository() {
		return modelRepository;
	}

	public void setModelRepository(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	public CommentRepository getCommentRepository() {
		return commentRepository;
	}

	public void setCommentRepository(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	
}
