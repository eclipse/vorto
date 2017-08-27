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
package org.eclipse.vorto.devtool.projectrepository.modeshape;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService;
import org.eclipse.vorto.devtool.projectrepository.exception.ResourceAlreadyExistsError;
import org.eclipse.vorto.devtool.projectrepository.exception.WrongUploadHandleTypeError;
import org.eclipse.vorto.devtool.projectrepository.model.FileResource;
import org.eclipse.vorto.devtool.projectrepository.model.FileUploadHandle;
import org.eclipse.vorto.devtool.projectrepository.model.FolderResource;
import org.eclipse.vorto.devtool.projectrepository.model.FolderUploadHandle;
import org.eclipse.vorto.devtool.projectrepository.model.ProjectResource;
import org.eclipse.vorto.devtool.projectrepository.model.ProjectUploadHandle;
import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.devtool.projectrepository.model.ResourceContent;
import org.eclipse.vorto.devtool.projectrepository.model.ResourceType;
import org.eclipse.vorto.devtool.projectrepository.model.UploadHandle;
import org.eclipse.vorto.devtool.projectrepository.modeshape.execption.ModeshapeRepositoryException;
import org.eclipse.vorto.devtool.projectrepository.query.IResourceQuery;
import org.eclipse.vorto.devtool.projectrepository.utils.ProjectRepositoryConstants;

/**
 * Modeshape based implementation of {@link IProjectRepositoryService}
 * 
 */
public class ProjectRepositoryServiceModeshape implements IProjectRepositoryService {

	protected static final String VORTO = "vorto";

	protected static final String PREFIX_NODE = "nt:";

	protected static final String BASE_NODE = PREFIX_NODE + "base";

	protected static final String FOLDER_NODE = PREFIX_NODE + "folder";

	protected static final String FILE_NODE = PREFIX_NODE + "file";

	protected static final String RESOURCE_NODE = PREFIX_NODE + "resource";

	protected static final String PREFIX_JCR = "jcr:";

	protected static final String JCR_CONTENT = PREFIX_JCR + "content";

	protected static final String JCR_DATA = PREFIX_JCR + "data";

	protected static final String JCR_LAST_MODIFIED = PREFIX_JCR + "lastModified";

	protected static final String JCR_CREATED_AT = PREFIX_JCR + "createdAt";

	protected static final String JCR_CREATED_BY = PREFIX_JCR + "createdBy";

	protected static final String JCR_PATH = PREFIX_JCR + "path";

	protected static final String PREFIX_VORTO_MIXIN = VORTO + ":";

	protected static final String MIXIN_JCR_LAST_MODIFIED = "mix:lastModified";

	protected static final String MIXIN_PROJECT_RESOURCE = PREFIX_VORTO_MIXIN + "projectResource";

	protected static final String MIXIN_MODEL_RESOURCE = PREFIX_VORTO_MIXIN + "modelResource";

	protected static final String PREFIX_VORTO_META_PROPERTY = VORTO + ":";

	protected static final String META_PROPERTY_RESOURCE_TYPE = PREFIX_VORTO_META_PROPERTY + "resourceType";

	protected static final String META_PROPERTY_DISPLAYNAME = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_DISPLAY_NAME;

	protected static final String META_PROPERTY_NAME = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_NAME;

	protected static final String META_PROPERTY_COLLABORATORS = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_COLLABORATORS;

	protected static final String META_PROPERTY_NAMESPACE = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_NAMESPACE;

	protected static final String META_PROPERTY_VERSION = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_VERSION;

	protected static final String META_PROPERTY_MODEL_TYPE = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_MODEL_TYPE;

	protected static final String META_PROPERTY_MODEL_SUB_TYPE = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_MODEL_SUB_TYPE;

	protected static final String META_PROPERTY_AUTHOR = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_AUTHOR;

	protected static final String META_PROPERTY_REFERENCES = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_REFERENCES;

	protected static final String META_PROPERTY_RESOURCE_ID = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_RESOURCE_ID;

	protected static final String META_PROPERTY_FILE_NAME = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_FILE_NAME;

	protected static final String META_PROPERTY_PROJECT_NAME = PREFIX_VORTO_META_PROPERTY
			+ ProjectRepositoryConstants.META_PROPERTY_PROJECT_NAME;

	protected static final Map<String, String> META_PROPERTIES_MAP;

	static {
		Map<String, String> tempMap = new HashMap<>();
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_AUTHOR, META_PROPERTY_AUTHOR);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_COLLABORATORS, META_PROPERTY_COLLABORATORS);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_NAME, META_PROPERTY_NAME);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_NAMESPACE, META_PROPERTY_NAMESPACE);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_VERSION, META_PROPERTY_VERSION);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_RESOURCE_ID, META_PROPERTY_RESOURCE_ID);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_REFERENCES, META_PROPERTY_REFERENCES);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_MODEL_SUB_TYPE, META_PROPERTY_MODEL_TYPE);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_MODEL_TYPE, META_PROPERTY_MODEL_TYPE);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_CREATIONDATE, JCR_CREATED_AT);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_DISPLAY_NAME, META_PROPERTY_DISPLAYNAME);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_FILE_NAME, META_PROPERTY_FILE_NAME);
		tempMap.put(ProjectRepositoryConstants.META_PROPERTY_PROJECT_NAME, META_PROPERTY_PROJECT_NAME);
		META_PROPERTIES_MAP = Collections.unmodifiableMap(tempMap);
	}

	
	private Session session;

	public ProjectRepositoryServiceModeshape(Session session) {
		this.session = session;
	}

	@Override
	public ProjectResource createProject(String projectName, String commitMessage) throws ResourceAlreadyExistsError {
		return createProject(projectName, new HashMap<String, String>(), commitMessage);
	}

	@Override
	public ProjectResource createProject(String projectName, Map<String, String> properties, String commitMessage) {
		try {
			checkProjectExists(projectName);
			Node projectNode = createFolderNode(projectName);
			projectNode.addMixin(MIXIN_PROJECT_RESOURCE);
			projectNode.addMixin(MIXIN_JCR_LAST_MODIFIED);
			Iterator<String> iterator = properties.keySet().iterator();
			while (iterator.hasNext()) {
				String property = iterator.next();
				projectNode.setProperty(PREFIX_VORTO_META_PROPERTY + property, properties.get(property));
			}
			projectNode.setProperty(META_PROPERTY_NAME, projectName);
			projectNode.setProperty(META_PROPERTY_RESOURCE_TYPE, ResourceType.ProjectResource.toString());
			session.save();
			return (ProjectResource) createQuery().name(projectName).type(ResourceType.ProjectResource).singleResult();
		} catch (RepositoryException e) {
			throw new ModeshapeRepositoryException("Error creating project " + projectName, e);
		}
	}

	@Override
	public IResourceQuery createQuery() {
		return new ResourceQueryModeshape(session);
	}

	@Override
	public void deleteResource(Resource resource) {
		if (resource == null)
			return;
		String path = resource.getPath();
		if(path.startsWith(File.separator)){
			path = path.replaceFirst(File.separator, "");
		}
		try {
			Node rootNode = session.getRootNode();
			Node removeNode = rootNode.getNode(path);
			removeNode.remove();
			session.save();
		} catch (RepositoryException ex) {
			throw new ModeshapeRepositoryException("Error deleting resource " + resource.getPath(), ex);
		}
	}

	@Override
	public void uploadResource(String commitComment, UploadHandle... handles) {
		for (UploadHandle handle : handles) {
			handle.setPath(FilenameUtils.normalize(handle.getPath()));
		}

		for (UploadHandle handle : handles) {
			try {
				if (handle instanceof ProjectUploadHandle) {
					checkProjectExists(handle.getPath());
				}
				createResourceNode(handle);
			} catch (RepositoryException e) {
				throw new ModeshapeRepositoryException("Error uploading resource " + handle.getPath(), e);
			}
		}
	}

	@Override
	public void updateFolderResourceProperties(FolderResource folderResource, Map<String, String> properties) {
		String path = folderResource.getPath();
		if(path.startsWith(File.separator)){
			path = path.replaceFirst(File.separator, "");
		}
		try {
			Node rootNode = session.getRootNode();
			Node folderNode = rootNode.getNode(path);
			for (Map.Entry<String, String> entry : properties.entrySet()) {
				if (!ProjectRepositoryConstants.IMMUTABLE_META_PROPERTY_SET.contains(entry.getKey())) {
					folderNode.setProperty(PREFIX_VORTO_MIXIN + entry.getKey(), entry.getValue());
				}
			}
			session.save();
		}catch(RepositoryException e){
			
		}		
	}

	@Override
	public ResourceContent getResourceContent(FileResource fileResource) {
		StringBuilder pathBuilder = new StringBuilder();
		String[] subDirectories = fileResource.getPath().split(File.pathSeparator);
		String fileName = subDirectories[subDirectories.length - 1];
		ResourceContent resourceContent = new ResourceContent();
		resourceContent.setPath(fileResource.getPath());
		try {
			Node rootNode = session.getRootNode();
			Node folderNode = rootNode.getNode(pathBuilder.toString());
			Node fileNode = folderNode.getNode(fileName);
			if (folderNode.hasNode(fileName)) {
				Node contentNode = (Node) fileNode.getPrimaryItem();
				InputStream inputStream = contentNode.getProperty(JCR_DATA).getBinary().getStream();
				resourceContent.setContent(IOUtils.toByteArray(inputStream));
			}
			return resourceContent;
		} catch (RepositoryException e) {
			return resourceContent;
		} catch (IOException e) {
			return resourceContent;
		}
	}
	
	@Override
	public void createVersionOfProject(String versionName, String projectName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteVersion(String versionName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void tag(Resource resource, String tag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void lock(Resource resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isLocked(Resource resource) {
		return false;
	}

	@Override
	public void unlock(Resource resource) {
		throw new UnsupportedOperationException();
	}

	private void checkProjectExists(String projectName) throws RepositoryException {
		Node rootNode = session.getRootNode();
		boolean projectExists = true;
		try {
			rootNode.getNode(projectName);
		} catch (PathNotFoundException ex) {
			projectExists = false;
		}
		if (projectExists) {
			throw new ResourceAlreadyExistsError("Directory for folder (" + projectName + ") already exists.");
		}
	}

	private Node createFolderNode(String folderPath) throws RepositoryException {
		Node rootNode = session.getRootNode();
		StringBuilder pathBuilder = new StringBuilder();
		String[] subDirectories = folderPath.split(File.separator);
		Node subNode = null;
		for (String subDirectory : subDirectories) {
			pathBuilder.append(subDirectory).append(File.separator);
			try {
				rootNode.getNode(pathBuilder.toString());
			} catch (PathNotFoundException e) {
				subNode = rootNode.addNode(pathBuilder.toString(), FOLDER_NODE);
				subNode.setPrimaryType(FOLDER_NODE);
			}
		}
		return subNode;
	}

	private Node upsertFileNode(FileUploadHandle fileUploadHandle) throws RepositoryException {
		StringBuilder pathBuilder = new StringBuilder();
		Map<String, String> properties = fileUploadHandle.getProperties();
		String filePath = FilenameUtils.getFullPathNoEndSeparator(fileUploadHandle.getPath());
		if(filePath.startsWith(File.separator)){
			filePath = filePath.replaceFirst(File.separator, "");
		}
		String[] subDirectories = filePath.split(File.separator);
		String fileName = fileUploadHandle.getFileName();
		Node rootNode = session.getRootNode();
		for (String string : subDirectories) {
			pathBuilder.append(string).append(File.separator);
		}
		Node folderNode = rootNode.getNode(pathBuilder.toString());
		Node fileNode; 
		Node contentNode;
		if (folderNode.hasNode(fileName)) {
			fileNode = folderNode.getNode(fileName);
			contentNode = (Node) fileNode.getPrimaryItem();
		} else {
			fileNode = folderNode.addNode(fileName, FILE_NODE);
			fileNode.addMixin(MIXIN_MODEL_RESOURCE);
			fileNode.setProperty(META_PROPERTY_FILE_NAME, fileName);
			fileNode.setProperty(META_PROPERTY_RESOURCE_TYPE, ResourceType.FileResource.toString());
			fileNode.setProperty(META_PROPERTY_DISPLAYNAME, properties.get(META_PROPERTY_NAME) + "." + properties.get(META_PROPERTY_MODEL_TYPE));
			contentNode = fileNode.addNode(JCR_CONTENT, RESOURCE_NODE);
		}
		Iterator<String> iterator = properties.keySet().iterator();
		while (iterator.hasNext()) {
			String property = iterator.next();
			fileNode.setProperty(PREFIX_VORTO_META_PROPERTY + property, properties.get(property));
		}
		Binary binary = session.getValueFactory().createBinary(new ByteArrayInputStream(fileUploadHandle.getContent()));
		contentNode.setProperty(JCR_DATA, binary);
		session.save();
		return fileNode;
	}

	private Node createResourceNode(UploadHandle handle) throws RepositoryException {
		Node node;
		if (handle instanceof ProjectUploadHandle) {
			node = createFolderNode(handle.getPath());
			node.addMixin(MIXIN_PROJECT_RESOURCE);
		} else if (handle instanceof FolderUploadHandle) {
			node = createFolderNode(handle.getPath());
		} else if (handle instanceof FileUploadHandle) {
			FileUploadHandle fileUploadHandle = (FileUploadHandle) handle;
			node = upsertFileNode(fileUploadHandle);
		} else {
			throw new WrongUploadHandleTypeError("UploadHandle is not of type FileUploadHandle or FolderUploadHandle");
		}
		return node;
	}
}