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
package org.eclipse.vorto.repository.web.comment;

import java.util.List;

import javax.validation.Valid;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.comment.Comment;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value="Comment Controller", description="REST API to manage Comments")
@RestController
@RequestMapping(value="/rest")
public class CommentController {

    @Autowired
    private ICommentService commentService;
    
    @ApiOperation(value = "Returns comments for a specific Model Resource")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Not found"), 
            				@ApiResponse(code = 200, message = "OK")})
    @RequestMapping(method = RequestMethod.GET,
    				value = "/comments/model/{namespace}/{name}/{version:.+}",
    				produces = "application/json")
    public List<Comment> getCommentsforModelId(	@ApiParam(value = "namespace", required = true) @PathVariable String namespace,
									    		@ApiParam(value = "name", required = true) @PathVariable String name,
									    		@ApiParam(value = "version", required = true) @PathVariable String version) {
    	
    	final ModelId modelId = new ModelId(name, namespace, version);
    	List<Comment> comments = commentService.getCommentsforModelId(modelId);
    	return comments;
    }
    
    @ApiOperation(value = "Returns comments for a specific Model Resource")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Not found"), 
            				@ApiResponse(code = 200, message = "OK")})
    @RequestMapping(method = RequestMethod.POST,
    				value = "/comments",
    				consumes = "application/json")
    @PreAuthorize("isAuthenticated()")
    public void addCommentforModelResource(@RequestBody @Valid Comment comment) throws Exception {
       	commentService.createComment(comment);
    }

}
