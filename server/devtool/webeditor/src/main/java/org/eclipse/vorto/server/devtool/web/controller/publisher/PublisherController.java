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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.devtool.projectrepository.model.ModelResource;
import org.eclipse.vorto.devtool.projectrepository.utils.ProjectRepositoryConstants;
import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.repository.api.upload.ModelHandle;
import org.eclipse.vorto.repository.api.upload.UploadModelResponse;
import org.eclipse.vorto.server.devtool.service.IProjectService;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.eclipse.vorto.server.devtool.utils.DevtoolUtils;
import org.eclipse.vorto.server.devtool.utils.WebUtils;
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

	@Value("${vorto.repository.base.path}")
	private String repositoryBasePath;

	@Autowired
	private Injector injector;

	@Autowired
	private DevtoolRestClient devtoolRestClient;

	@Autowired
	private IProjectService projectService;

	@Autowired
	private DevtoolUtils devtoolUtils;

	@Autowired
	private WebUtils webUtils;

	@ApiOperation(value = "Uploads models to the vorto repository for validation")
	@RequestMapping(value = "/{projectName}/validate", method = RequestMethod.GET)
	public ResponseEntity<UploadModelResponse> uploadModels(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);
		ResourceSet resourceSet = webEditorResourceSetProvider.getResourceSetFromSession(httpServiceContext);

		Objects.requireNonNull(projectName, "projectName must not be null");
		String userProjectName = webUtils.getUserProjectName(projectName);

		List<ModelResource> projectResources = projectService.getProjectResources(userProjectName);

		HashMap<String, Resource> resourceMap = new HashMap<>();

		for (org.eclipse.vorto.devtool.projectrepository.model.Resource resource : projectResources) {
			String resourceId = resource.getProperties().get(ProjectRepositoryConstants.META_PROPERTY_RESOURCE_ID);
			Resource xtextResource = resourceSet.getResource(devtoolUtils.getResourceURI(resourceId), true);
			resourceMap.put(resourceId, xtextResource);
		}
		byte[] zipFileContent = createZipFileContent(resourceMap);
		final String fileName = Long.toString(System.currentTimeMillis()) + ".zip";
		ResponseEntity<UploadModelResponse> responseEntity = devtoolRestClient.uploadMultipleFiles(fileName, zipFileContent);
		return responseEntity;
	}

	@ApiOperation(value = "Checks in single model to the vorto repository")
	@RequestMapping(value = "/{handleId:.+}", method = RequestMethod.PUT)
	public ResponseEntity<UploadModelResponse> checkin(
			@ApiParam(value = "The file name of uploaded vorto model", required = true) final @PathVariable String handleId) {
		return devtoolRestClient.checkInSingleFile(handleId);
	}

	@ApiOperation(value = "Checks in multiple models to the vorto repository")
	@RequestMapping(value = "/checkInMultiple", method = RequestMethod.PUT)
	public ResponseEntity<UploadModelResponse> checkInMultiple(
			@ApiParam(value = "The file name of uploaded vorto model", required = true) final @RequestBody ModelHandle[] modelHandles) {
		return devtoolRestClient.checkInMultipleFiles(modelHandles);
	}

	private byte[] createZipFileContent(HashMap<String, Resource> resourceMap) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zipOutputStream = new ZipOutputStream(baos);
			Iterator<String> iterator = resourceMap.keySet().iterator();
			while (iterator.hasNext()) {
				String resourceName = iterator.next();
				zipOutputStream.putNextEntry(new ZipEntry(resourceName));
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				Resource resource = resourceMap.get(resourceName);
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
