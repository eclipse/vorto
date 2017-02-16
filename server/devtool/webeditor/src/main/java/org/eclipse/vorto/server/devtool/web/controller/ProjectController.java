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
package org.eclipse.vorto.server.devtool.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.server.devtool.exception.ProjectAlreadyExistsException;
import org.eclipse.vorto.server.devtool.exception.ProjectNotFoundException;
import org.eclipse.vorto.server.devtool.exception.ProjectResourceAlreadyExistsException;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;
import org.eclipse.vorto.server.devtool.service.IEditorProjectService;
import org.eclipse.vorto.server.devtool.web.IEditorSession;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.eclipse.xtext.web.servlet.HttpServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.inject.Injector;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/rest/project")
public class ProjectController {

	@Autowired
	private Injector injector;

	@Autowired
	private IEditorProjectService projectRepositoryService;
	
	@Autowired
	private IEditorSession editorSession;

	@ApiOperation(value = "Checks whether a Vorto project already exists")
	@RequestMapping(value = "/{projectName}/check", method = RequestMethod.GET)
	public void checkProjectExists(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
			throws ProjectAlreadyExistsException {

		Objects.requireNonNull(projectName, "projectName must not be null");

		projectRepositoryService.checkProjectExists(editorSession.getUser(), projectName);
	}

	@ApiOperation(value = "Checks whether a resource exists within the Vorto project")
	@RequestMapping(value = "/{projectName}/resources/check/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public void checkResourceExists(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace,
			@ApiParam(value = "Name", required = true) final @PathVariable String name,
			@ApiParam(value = "Version", required = true) final @PathVariable String version,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
			throws ProjectNotFoundException, ProjectAlreadyExistsException, ProjectResourceAlreadyExistsException {

		Objects.requireNonNull(projectName, "projectName must not be null");
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "namespace must not be null");
		Objects.requireNonNull(version, "namespace must not be null");

		ProjectResource projectResource = new ProjectResource();
		projectResource.setName(name);
		projectResource.setNamespace(namespace);
		projectResource.setVersion(version);

		projectRepositoryService.checkResourceExists(editorSession.getUser(), projectName, projectResource);
	}

	@ApiOperation(value = "Creates a new Vorto project")
	@RequestMapping(method = RequestMethod.POST)
	public void createProject(@RequestBody Project project,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
			throws ProjectAlreadyExistsException {

		Objects.requireNonNull(project.getProjectName(), "projectName must not be null");
		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);

		Project _project = projectRepositoryService.createProject(editorSession.getUser(), project.getProjectName());

		webEditorResourceSetProvider.setSessionResourceSet(httpServiceContext, _project.getResourceSet());
		webEditorResourceSetProvider.setSessionRefencedResourceSet(httpServiceContext,
				_project.getReferencedResourceSet());
	}

	@ApiOperation(value = "Opens an existing Vorto project")
	@RequestMapping(value = "/{projectName}/open", method = RequestMethod.GET)
	public void openProject(@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
			throws ProjectNotFoundException {

		Objects.requireNonNull(projectName, "projectName must not be null");

		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);

		Project project = projectRepositoryService.openProject(editorSession.getUser(), projectName);

		webEditorResourceSetProvider.setSessionResourceSet(httpServiceContext, project.getResourceSet());
		webEditorResourceSetProvider.setSessionRefencedResourceSet(httpServiceContext,
				project.getReferencedResourceSet());
	}

	@ApiOperation(value = "Returns all the project for the user")
	@RequestMapping(method = RequestMethod.GET)
	public List<Project> getProjects(final HttpServletRequest request) {
		return projectRepositoryService.getProjects(editorSession.getUser());
	}

	@ApiOperation(value = "Deletes the resource in the Vorto project")
	@RequestMapping(value = "/{projectName}/resources/delete/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public void deleteProjectResource(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace,
			@ApiParam(value = "Name", required = true) final @PathVariable String name,
			@ApiParam(value = "Version", required = true) final @PathVariable String version,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
			throws ProjectNotFoundException {

		Objects.requireNonNull(projectName, "projectName must not be null");
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "namespace must not be null");
		Objects.requireNonNull(version, "namespace must not be null");

		ProjectResource projectResource = new ProjectResource();
		projectResource.setName(name);
		projectResource.setNamespace(namespace);
		projectResource.setVersion(version);

		projectRepositoryService.deleteResource(editorSession.getUser(), projectName, projectResource);
	}

	@ApiOperation(value = "Creates a new resource in the Vorto project")
	@RequestMapping(value = "/{projectName}/resources", method = RequestMethod.POST)
	public void createProjectResource(@RequestBody ProjectResource projectResoure,
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
			throws ProjectNotFoundException, ProjectResourceAlreadyExistsException {

		Objects.requireNonNull(projectName, "projectName must not be null");
		projectRepositoryService.createResource(editorSession.getUser(), projectName, projectResoure);
	}

	@ApiOperation(value = "Returns a list of resources in the Vorto project")
	@RequestMapping(value = "/{projectName}/resources", method = RequestMethod.GET)
	public List<ProjectResource> getResources(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
			throws ProjectNotFoundException {

		Objects.requireNonNull(projectName, "projectName must not be null");

		return projectRepositoryService.getProjectResources(editorSession.getUser(), projectName);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Project not found")
	@ExceptionHandler(ProjectNotFoundException.class)
	public void handleProjectNotFoundException() {

	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Project already exists")
	@ExceptionHandler(ProjectAlreadyExistsException.class)
	public void handleProjectAlreadyExistsException() {

	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Resource already exists")
	@ExceptionHandler(ProjectResourceAlreadyExistsException.class)
	public void handleProjectResourceAlreadyExistsException() {

	}
}
