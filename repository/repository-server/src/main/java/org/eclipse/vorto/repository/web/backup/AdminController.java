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
package org.eclipse.vorto.repository.web.backup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.backup.IModelBackupService;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.core.exceptions.UploadTooLargeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/rest/admin")
public class AdminController extends AbstractRepositoryController {
	
	@Autowired
	private IModelBackupService backupService;
	
	@Autowired
	private IModelRepository repositoryService;
	
	@Value("${server.config.maxBackupSize}")
	private long maxBackupSize;
	
	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";

	private static final SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyyMMdd-HH:mm");
	
	@RequestMapping(value = "/{namespace}/{name}/{version:.+}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(new org.eclipse.vorto.repository.api.ModelId(#name,#namespace,#version),'model:delete')")
	public void deleteModelResource(final @PathVariable String namespace,final @PathVariable String name, final @PathVariable String version) {
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");
		repositoryService.removeModel(new ModelId(name, namespace, version));
	}
	
	@RequestMapping(value = "/content", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void backupRepository(final HttpServletResponse response) throws Exception {
		byte[] backup = this.backupService.backup();
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + "vortobackup_"+SIMPLEDATEFORMAT.format(new Date())+".xml");
		response.setContentLengthLong(backup.length);
		response.setContentType(APPLICATION_OCTET_STREAM);
		try {
			IOUtils.copy(new ByteArrayInputStream(backup), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}
	
	@RequestMapping(value = "/content", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void restoreRepository(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.getSize() > maxBackupSize) {
			throw new UploadTooLargeException("backup", maxBackupSize);
		}
		
		this.backupService.restore(file.getBytes());		
	}
	
	@RequestMapping(value = "/content/images", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void removeImages() {
		for (ModelInfo model : repositoryService.search("*")) {
			if (model.isHasImage()) {
				repositoryService.removeModelImage(model.getId());
			}
		}
	}
}
