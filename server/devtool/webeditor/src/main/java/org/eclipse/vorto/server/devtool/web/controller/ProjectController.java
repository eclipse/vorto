/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.4
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
package org.eclipse.vorto.server.devtool.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.devtool.projectrepository.ResourceAlreadyExistsError;
import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryFileConstants;
import org.eclipse.vorto.devtool.projectrepository.model.ProjectResource;
import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.server.devtool.http.request.DeleteResourceRequest;
import org.eclipse.vorto.server.devtool.http.response.Response;
import org.eclipse.vorto.server.devtool.models.ModelResource;
import org.eclipse.vorto.server.devtool.service.IEditorSession;
import org.eclipse.vorto.server.devtool.service.IProjectService;
import org.eclipse.vorto.server.devtool.utils.Constants;
import org.eclipse.vorto.server.devtool.utils.DevtoolReferenceLinker;
import org.eclipse.vorto.server.devtool.utils.ProjectResourceComparator;
import org.eclipse.vorto.server.devtool.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/rest/project")
public class ProjectController {

	@Autowired
	private IProjectService projectService;

	@Autowired
	private IEditorSession editorSession;

	@Autowired
	private DevtoolReferenceLinker devtoolReferenceLinker;

	@Autowired
	private WebUtils webUtils;

	@Value("${reference.repository}")
	private String referenceRepository;

	private ProjectResourceComparator projectResourceComparator = new ProjectResourceComparator();

	@ApiOperation(value = "Checks whether a Vorto project already exists")
	@RequestMapping(value = "/{projectName}/check", method = RequestMethod.GET)
	public Response checkProjectExists(
			@ApiParam(value = "ProjectName", required = true) @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		Objects.requireNonNull(projectName, "projectName must not be null");

		ProjectResource projectResource = new ProjectResource();
		projectResource.setName(projectName);
		projectResource.setPath(projectName);

		Resource resource = projectService.checkResourceExists(projectResource);
		if (resource == null) {
			return new Response(Constants.MESSAGE_RESOURCE_DOES_NOT_EXIST, null);
		} else {
			return new Response(Constants.MESSAGE_RESOURCE_ALREADY_EXISTS, null);
		}
	}

	@ApiOperation(value = "Creates a new Vorto project")
	@RequestMapping(method = RequestMethod.POST)
	public Response createProject(@RequestBody ProjectResource projectResource,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		Objects.requireNonNull(projectResource.getName(), "projectName must not be null");
		Response response;
		try {
			String projectName = webUtils.getUserProjectName(projectResource.getName());
			ProjectResource resource = projectService.createProject(projectName, editorSession.getUser());
			response = new Response(Constants.MESSAGE_RESOURCE_CREATED, resource);
		} catch (ResourceAlreadyExistsError resourceAlreadyExistsError) {
			response = new Response(Constants.MESSAGE_RESOURCE_ALREADY_EXISTS, null);
		}
		return response;
	}

	@ApiOperation(value = "Returns all the projects of the user")
	@RequestMapping(method = RequestMethod.GET)
	public List<ProjectResource> getProjects(final HttpServletRequest request) {
		List<ProjectResource> projectList = projectService.getProjects(editorSession.getUser());
		Collections.sort(projectList, projectResourceComparator.reversed());
		return projectList;
	}

	@ApiOperation(value = "Creates a new resource in the Vorto project")
	@RequestMapping(value = "/{projectName}/resources", method = RequestMethod.POST)
	public Response createProjectResource(@RequestBody ModelResource modelResource,
			@ApiParam(value = "ProjectName", required = true) @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		Objects.requireNonNull(projectName, "projectName must not be null");
		projectName = webUtils.getUserProjectName(projectName);

		Response response;

		try {
			Resource resource = projectService.createProjectResource(projectName, modelResource);
			response = new Response(Constants.MESSAGE_RESOURCE_CREATED, resource);
		} catch (ResourceAlreadyExistsError resourceAlreadyExistsError) {
			response = new Response(Constants.MESSAGE_RESOURCE_ALREADY_EXISTS, null);
		}
		return response;
	}

	@ApiOperation(value = "Returns a list of resources in the Vorto project")
	@RequestMapping(value = "/{projectName}/resources", method = RequestMethod.GET)
	public List<Resource> getResources(
			@ApiParam(value = "ProjectName", required = true) @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		Objects.requireNonNull(projectName, "projectName must not be null");
		projectName = webUtils.getUserProjectName(projectName);

		return projectService.getProjectResources(projectName);
	}

	@ApiOperation(value = "Opens an existing Vorto project")
	@RequestMapping(value = "/{projectName}/open", method = RequestMethod.GET)
	public Resource openProject(
			@ApiParam(value = "ProjectName", required = true) @PathVariable final String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) throws IOException {

		Objects.requireNonNull(projectName, "projectName must not be null");

		ResourceSet resourceSet = webUtils.getResourceSet(request);
		ProjectResource projectResource = projectService.getProject(webUtils.getUserProjectName(projectName));
		List<String> resourceIdList = new ArrayList<String>();

		if (projectResource.getProperties().containsKey(ProjectRepositoryFileConstants.META_PROPERTY_REFERENCES)) {
			resourceIdList = new ArrayList<String>(Arrays.asList(projectResource.getProperties()
					.get(ProjectRepositoryFileConstants.META_PROPERTY_REFERENCES).split(Constants.COMMA)));
		}

		List<Resource> resourceList = projectService.getProjectResources(projectName);
		for (Resource resource : resourceList) {
			resourceIdList.add(resource.getProperties().get(ProjectRepositoryFileConstants.META_PROPERTY_RESOURCE_ID));
		}
				
		devtoolReferenceLinker.resetResourceSet(resourceSet);
		webUtils.removePreviousProjectModelsFromSessionAttributes(request);
		devtoolReferenceLinker.loadResources(resourceIdList, resourceSet);
		webUtils.setSessionResourceSet(request, resourceSet);

		return projectResource;
	}

	@ApiOperation(value = "Deletes a resource from the project")
	@RequestMapping(value = "/{projectName}/delete", method = RequestMethod.POST)
	public void deleteResource(@RequestBody DeleteResourceRequest deleteResourceRequest,
			@ApiParam(value = "ProjectName", required = true) @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		Objects.requireNonNull(projectName, "projectName must not be null");

		projectName = webUtils.getUserProjectName(projectName);
		ResourceSet resourceSet = webUtils.getResourceSet(request);
		devtoolReferenceLinker.removeResourceFromResourceSet(deleteResourceRequest.getResourceId(), resourceSet);		
		projectService.deleteResource(projectName, deleteResourceRequest.getResourceId());
		webUtils.removeDeletedModelFromSessionAttributes(request, deleteResourceRequest.getResourceId());
	}

	@ApiOperation(value = "Deletes a resource from the project")
	@RequestMapping(value = "/{projectName}", method = RequestMethod.DELETE)
	public void deleteProject(@ApiParam(value = "ProjectName", required = true) @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		Objects.requireNonNull(projectName, "projectName must not be null");

		projectService.deleteProject(webUtils.getUserProjectName(projectName));
	}
}
