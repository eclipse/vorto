/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.server.devtool.web.controller.publisher;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.repository.api.upload.ServerResponse;
import org.eclipse.vorto.repository.model.ModelHandle;
import org.eclipse.vorto.server.devtool.models.ProjectResource;
import org.eclipse.vorto.server.devtool.models.ProjectResourceListWrapper;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.eclipse.xtext.web.servlet.HttpServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.inject.Injector;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/rest/publish")
public class PublisherController {

	@Value("${vorto.repository.base.path:http://vorto.eclipse.org}")
	private String repositoryBasePath;

	@Autowired
	Injector injector;

	@Autowired
	DevtoolRestClient devtoolRestClient;

	@ApiOperation(value = "Uploads models to the vorto repository for validation")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<ServerResponse> uploadModels(
			@RequestBody ProjectResourceListWrapper projectResourceListWrapper,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);
		ResourceSet resourceSet = webEditorResourceSetProvider.getResourceSetFromSession(httpServiceContext);
		ArrayList<Resource> resourceList = new ArrayList<Resource>();
		for (ProjectResource projectResource : projectResourceListWrapper.getProjectResourceList()) {
			Resource resource = resourceSet.getResource(URI.createURI(projectResource.getResourceId()), true);
			resourceList.add(resource);
		}
		byte[] zipFileContent = createZipFileContent(projectResourceListWrapper.getProjectResourceList(), resourceList);
		
		final String fileName = Long.toString(System.currentTimeMillis()) + ".zip";
		return devtoolRestClient.uploadMultipleFiles(fileName,zipFileContent);
	}

	@ApiOperation(value = "Checks in single model to the vorto repository")
	@RequestMapping(value = "/{handleId:.+}", method = RequestMethod.PUT)
	public ResponseEntity<ServerResponse> checkin(
			@ApiParam(value = "The file name of uploaded vorto model", required = true) final @PathVariable String handleId) {
			return devtoolRestClient.checkInSingleFile(handleId);
	}

	@ApiOperation(value = "Checks in multiple models to the vorto repository")
	@RequestMapping(value = "/checkInMultiple", method = RequestMethod.PUT)
	public ResponseEntity<ServerResponse> checkInMultiple(
			@ApiParam(value = "The file name of uploaded vorto model", required = true) final @RequestBody ModelHandle[] modelHandles) {
				return devtoolRestClient.checkInMultipleFiles(modelHandles);
	}

	private byte[] createZipFileContent(ArrayList<ProjectResource> projectResourceList, ArrayList<Resource> resourceList) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zipOutputStream = new ZipOutputStream(baos);
			for (int index = 0; index < projectResourceList.size(); index++) {
				zipOutputStream.putNextEntry(new ZipEntry(projectResourceList.get(index).getName()));
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				Resource resource = resourceList.get(index);
				resource.save(byteArrayOutputStream, null);
				zipOutputStream.write(byteArrayOutputStream.toByteArray());
				zipOutputStream.closeEntry();
			}
			zipOutputStream.close();
			return baos.toByteArray();
		} catch (Exception ex) {
			return null;
		}
	}
}
