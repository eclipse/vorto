/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.repository.web;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.vorto.repository.model.UploadModelResult;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value="Model Controller", description="REST API to upload Models")
@RestController
@RequestMapping(value = "/rest/secure")
public class ShareModelController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private IModelRepository modelRepository;
	
	private UploadModelResult uploadModelResult;
	
	@ApiOperation(value = "Upload and validate a Model")
	@RequestMapping(method = RequestMethod.POST)
	public UploadModelResult uploadModel(@ApiParam(value = "File", required = true) @RequestParam("file") MultipartFile file) {
		LOGGER.info("uploadModel: [" + file.getOriginalFilename() + "]");
		try {
			uploadModelResult = modelRepository.upload(file.getBytes(), file.getOriginalFilename());
			return uploadModelResult;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@ApiOperation(value = "Checkin a Model into the Repository")
	@RequestMapping(value = "/{handleId:.+}", method = RequestMethod.PUT)
	public void checkin(@ApiParam(value = "Handle Id", required = true) final @PathVariable String handleId) {
		modelRepository.checkin(handleId, SecurityContextHolder.getContext().getAuthentication().getName());
	}
}
