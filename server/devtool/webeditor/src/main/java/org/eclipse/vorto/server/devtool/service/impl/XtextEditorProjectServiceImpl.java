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
package org.eclipse.vorto.server.devtool.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService;
import org.eclipse.vorto.devtool.projectrepository.ResourceAlreadyExistsError;
import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryFileConstants;
import org.eclipse.vorto.devtool.projectrepository.model.FileUploadHandle;
import org.eclipse.vorto.devtool.projectrepository.model.FolderResource;
import org.eclipse.vorto.devtool.projectrepository.model.ProjectResource;
import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.devtool.projectrepository.model.ResourceType;
import org.eclipse.vorto.devtool.projectrepository.query.IResourceQuery;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.server.devtool.models.ModelResource;
import org.eclipse.vorto.server.devtool.service.IEditorSession;
import org.eclipse.vorto.server.devtool.service.IProjectService;
import org.eclipse.vorto.server.devtool.utils.Constants;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.eclipse.vorto.server.devtool.utils.DevtoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class XtextEditorProjectServiceImpl implements IProjectService {

	@Autowired
	private IEditorSession editorSession;

	@Autowired
	private IProjectRepositoryService projectRepositoryService;

	@Autowired
	private DevtoolUtils devtoolUtils;

	@Autowired
	private DevtoolRestClient devtoolRestClient;

	@Value("${reference.repository}")
	private String referenceRepository;

	@Override
	public ProjectResource createProject(String projectName, String author) {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put(ProjectRepositoryFileConstants.META_PROPERTY_AUTHOR, author);
		ProjectResource resource = projectRepositoryService.createProject(projectName, properties, null);
		return resource;
	}

	@Override
	public ProjectResource getProject(String projectName) {
		return (ProjectResource) projectRepositoryService.createQuery()
				.path(projectName)
				.type(ResourceType.ProjectResource).singleResult();
	}
	
	@Override
	public List<Resource> getProjects(String author) {
		return projectRepositoryService.createQuery()
				.property(ProjectRepositoryFileConstants.META_PROPERTY_AUTHOR, author)
				.type(ResourceType.ProjectResource).list();
	}

	@Override
	public List<Resource> getProjectResources(String projectName) {
		return projectRepositoryService.createQuery().pathLike(projectName).type(ResourceType.FileResource).list();
	}

	@Override
	public Resource createProjectResource(String projectName, String author, ModelResource modelResource) {
		IResourceQuery resourceQuery = getModelResourceQuery(projectName, modelResource);
		List<Resource> list = resourceQuery.list();

		if (list.isEmpty()) {
			String fileContent = devtoolUtils.generateFileContent(modelResource);
			FileUploadHandle fileUploadHandle = getFileUploadHandle(projectName, fileContent, author, modelResource);
			projectRepositoryService.uploadResource(null, fileUploadHandle);
			return resourceQuery.list().get(0);
		} else {
			throw new ResourceAlreadyExistsError(Constants.MESSAGE_RESOURCE_ALREADY_EXISTS);
		}
	}

	@Override
	public Resource createProjectResource(String projectName, ModelResource modelResource) {
		return createProjectResource(projectName, editorSession.getUser(), modelResource);
	}

	@Override
	public Resource checkResourceExists(Resource resource) {
		if (resource.getType() == ResourceType.ProjectResource) {
			return projectRepositoryService.createQuery().path(resource.getPath()).name(resource.getName())
					.type(resource.getType()).singleResult();
		} else if (resource.getType() == ResourceType.FileResource) {
			return projectRepositoryService.createQuery().pathLike(resource.getPath()).name(resource.getName())
					.property(ProjectRepositoryFileConstants.META_PROPERTY_VERSION,
							resource.getProperties().get(ProjectRepositoryFileConstants.META_PROPERTY_VERSION))
					.property(ProjectRepositoryFileConstants.META_PROPERTY_NAMESPACE,
							resource.getProperties().get(ProjectRepositoryFileConstants.META_PROPERTY_NAMESPACE))
					.property(ProjectRepositoryFileConstants.META_PROPERTY_NAME,
							resource.getProperties().get(ProjectRepositoryFileConstants.META_PROPERTY_NAME))
					.singleResult();
		} else {
			return projectRepositoryService.createQuery().path(resource.getPath()).name(resource.getName())
					.type(resource.getType()).singleResult();
		}
	}

	@Override
	public Resource getReferencedResource(ModelInfo modelInfo) {
		ModelResource modelResource = devtoolUtils.getModelResource(modelInfo);
		IResourceQuery resourceQuery = getModelResourceQuery(referenceRepository, modelResource);
		List<Resource> list = resourceQuery.list();

		if (list.isEmpty()) {
			String fileContent = devtoolRestClient.getModelFile(modelInfo.getId());
			FileUploadHandle fileUploadHandle = getFileUploadHandle(referenceRepository, fileContent,
					modelInfo.getAuthor(), modelResource);
			projectRepositoryService.uploadResource(null, fileUploadHandle);
			return resourceQuery.list().get(0);
		} else {
			return list.get(0);
		}
	}

	@Override
	public void deleteResource(String projectName, String resourceId) {
		deleteResource(projectName, editorSession.getUser(), resourceId);
	}

	@Override
	public void deleteResource(String projectName, String author, String resourceId) {
		IResourceQuery resourceQuery = projectRepositoryService.createQuery().pathLike(projectName)
				.property(ProjectRepositoryFileConstants.META_PROPERTY_RESOURCE_ID, resourceId)
				.property(ProjectRepositoryFileConstants.META_PROPERTY_AUTHOR, author);
		Resource resource = resourceQuery.singleResult();
		projectRepositoryService.deleteResource(resource);
	}

	@Override
	public void addReferenceToProject(String projectName, String referenceResourceId) {
		ProjectResource projectResource = (ProjectResource) projectRepositoryService.createQuery().pathLike(projectName)
				.type(ResourceType.ProjectResource).singleResult();
		Map<String, String> properties = projectResource.getProperties();
		String[] resourceIdArray = {};
		if (properties.containsKey(ProjectRepositoryFileConstants.META_PROPERTY_REFERENCES)) {
			resourceIdArray = properties.get(ProjectRepositoryFileConstants.META_PROPERTY_REFERENCES)
					.split(Constants.COMMA);
		}
		ArrayList<String> resourceIdList = new ArrayList<>(Arrays.asList(resourceIdArray));
		Set<String> resourceIdSet = new HashSet<String>(resourceIdList);
		if (resourceIdSet.contains(referenceResourceId)) {
			return;
		}else{
			resourceIdList.add(referenceResourceId);
			Map<String, String> updatedProperties = new HashMap<String, String>();
			updatedProperties.put(ProjectRepositoryFileConstants.META_PROPERTY_REFERENCES,
					String.join(Constants.COMMA, resourceIdList));
			projectRepositoryService.updateFolderResourceProperties(projectResource, updatedProperties);			
		}
	}

	private IResourceQuery getModelResourceQuery(String projectName, ModelResource modelResource) {
		return projectRepositoryService.createQuery().pathLike(projectName).name(modelResource.getFilename())
				.property(ProjectRepositoryFileConstants.META_PROPERTY_VERSION, modelResource.getVersion())
				.property(ProjectRepositoryFileConstants.META_PROPERTY_NAMESPACE, modelResource.getNamespace())
				.property(ProjectRepositoryFileConstants.META_PROPERTY_NAME, modelResource.getName());
	}

	private FileUploadHandle getFileUploadHandle(String projectName, String fileContent, String author,
			ModelResource modelResource) {
		Resource projectResource = projectRepositoryService.createQuery().path(projectName.replace("\\", "/"))
				.singleResult();
		FileUploadHandle fileUploadHandle = new FileUploadHandle((FolderResource) projectResource,
				modelResource.getFilename(), fileContent.getBytes());
		String resourceId = devtoolUtils.generateResourceId(modelResource, projectName, author);
		HashMap<String, String> properties = (HashMap<String, String>) modelResource.getProperties();
		properties.put(ProjectRepositoryFileConstants.META_PROPERTY_AUTHOR, author);
		properties.put(ProjectRepositoryFileConstants.META_PROPERTY_RESOURCE_ID, resourceId);
		fileUploadHandle.setProperties(properties);
		return fileUploadHandle;
	}
}