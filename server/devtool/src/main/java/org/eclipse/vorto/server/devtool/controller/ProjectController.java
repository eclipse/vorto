package org.eclipse.vorto.server.devtool.controller;

import java.util.ArrayList;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.server.devtool.exception.ProjectAlreadyExistsException;
import org.eclipse.vorto.server.devtool.exception.ProjectNotFoundException;
import org.eclipse.vorto.server.devtool.exception.ProjectResourceAlreadyExistsException;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;
import org.eclipse.vorto.server.devtool.service.IProjectRepositoryService;
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
@RequestMapping(value = "/project")
public class ProjectController {

	@Autowired
	Injector injector;

	@Autowired
	IProjectRepositoryService projectRepositoryService;

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

	@ApiOperation(value = "Checks whether a Vorto project already exists")
	@RequestMapping(value = "/{projectName}/check", method = RequestMethod.GET)
	public void checkProjectExists(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
					throws ProjectAlreadyExistsException {

		Objects.requireNonNull(projectName, "projectName must not be null");

		String sessionId = request.getSession().getId();

		projectRepositoryService.checkProjectExists(sessionId, projectName);
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
		String sessionId = request.getSession().getId();
		
		ProjectResource projectResource = new ProjectResource();
		projectResource.setName(name);
		projectResource.setNamespace(namespace);
		projectResource.setVersion(version);
		
		projectRepositoryService.checkResourceExists(sessionId, projectName, projectResource);
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

		String sessionId = request.getSession().getId();

		project = projectRepositoryService.createProject(sessionId, project.getProjectName());

		webEditorResourceSetProvider.setSessionResourceSet(httpServiceContext, project.getResourceSet());
		webEditorResourceSetProvider.setSessionRefencedResourceSet(httpServiceContext,
				project.getReferencedResourceSet());
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

		String sessionId = request.getSession().getId();
		Project project = projectRepositoryService.openProject(sessionId, projectName);

		webEditorResourceSetProvider.setSessionResourceSet(httpServiceContext, project.getResourceSet());
		webEditorResourceSetProvider.setSessionRefencedResourceSet(httpServiceContext,
				project.getReferencedResourceSet());
	}

	@ApiOperation(value = "Returns all the project for the user")
	@RequestMapping(method = RequestMethod.GET)
	public ArrayList<Project> getProjects(final HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		return projectRepositoryService.getProjects(sessionId);
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
		String sessionId = request.getSession().getId();
		
		ProjectResource projectResource = new ProjectResource();
		projectResource.setName(name);
		projectResource.setNamespace(namespace);
		projectResource.setVersion(version);
		
		projectRepositoryService.deleteResource(sessionId, projectName, projectResource);
	}

	@ApiOperation(value = "Creates a new resource in the Vorto project")
	@RequestMapping(value = "/{projectName}/resources/create", method = RequestMethod.POST)
	public void createProjectResource(
			@RequestBody ProjectResource projectResoure,
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
					throws ProjectNotFoundException, ProjectResourceAlreadyExistsException {

		Objects.requireNonNull(projectName, "projectName must not be null");
		String sessionId = request.getSession().getId();
		projectRepositoryService.createResource(sessionId, projectName, projectResoure);
	}

	@ApiOperation(value = "Returns a list of resources in the Vorto project")
	@RequestMapping(value = "/{projectName}/resources", method = RequestMethod.GET)
	public ArrayList<ProjectResource> getResources(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request)
					throws ProjectNotFoundException {

		Objects.requireNonNull(projectName, "projectName must not be null");

		String sessionId = request.getSession().getId();
		return projectRepositoryService.getProjectResources(sessionId, projectName);
	}
}
