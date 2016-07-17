package org.eclipse.vorto.server.devtool.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.service.IProjectRepositoryService;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.eclipse.xtext.web.servlet.HttpServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@ApiOperation(value = "Creates a new Vorto project")
	@RequestMapping(value = "/new/{projectName}", method = RequestMethod.GET)
	public void createProjec(@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		Objects.requireNonNull(projectName, "projectName must not be null");

		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);

		String sessionId = request.getSession().getId();
		ResourceSet resourceSet = webEditorResourceSetProvider.getNewResourceSet();

		Project project = projectRepositoryService.createProject(sessionId, projectName, resourceSet);

		webEditorResourceSetProvider.setSessionResourceSet(httpServiceContext, project.getResourceSet());
		webEditorResourceSetProvider.setSessionRefencedResourceSet(httpServiceContext,
				project.getReferencedResourceSet());
	}

	@ApiOperation(value = "Opens an existing Vorto project")
	@RequestMapping(value = "/open/{projectName}", method = RequestMethod.GET)
	public void openProject(@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

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
	
	@ApiOperation(value = "Opens an existing Vorto project")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ArrayList<String> getProjects(final HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		return projectRepositoryService.getProjects(sessionId);		
	}

	@ApiOperation(value = "Returns a list of resources in the Vorto project")
	@RequestMapping(value = "/resources/{projectName}", method = RequestMethod.GET)
	public void getResources(@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		Objects.requireNonNull(projectName, "projectName must not be null");

		String sessionId = request.getSession().getId();
		projectRepositoryService.getProjectResources(sessionId, projectName);
	}

	@ApiOperation(value = "Returns the contents of a resource in the existing Vorto project")
	@RequestMapping(value = "/resource/{projectName}/{resourceId}", method = RequestMethod.GET)
	public void getProjectResourceContents(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "ResourceId", required = true) final @PathVariable String resourceId,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request,
			@ApiParam(value = "Response", required = true) final HttpServletResponse response) {

		Objects.requireNonNull(projectName, "projectName must not be null");
		Objects.requireNonNull(resourceId, "resourceId must not be null");

		String sessionId = request.getSession().getId();
		String content = projectRepositoryService.getProjectResourceContents(sessionId, projectName, resourceId);
		
		try {
			IOUtils.copy(new ByteArrayInputStream(content.getBytes()), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}
}
