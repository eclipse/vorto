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
package org.eclipse.vorto.devtool.projectrepository.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService;
import org.eclipse.vorto.devtool.projectrepository.exception.ProjectRepositoryException;
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
import org.eclipse.vorto.devtool.projectrepository.model.UploadHandle;
import org.eclipse.vorto.devtool.projectrepository.query.NotExistingResourceError;
import org.eclipse.vorto.devtool.projectrepository.utils.ProjectRepositoryConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File system based implementation of {@link IProjectRepositoryService}
 * 
 */
public class ProjectRepositoryServiceFS implements IProjectRepositoryService {

	protected String projectsDirectory = null;

	public static final String META_PROPERTY_FILENAME = ProjectRepositoryConstants.META_PROPERTY_FILE;
	public static final String META_PROPERTY_CREATIONDATE = ProjectRepositoryConstants.META_PROPERTY_CREATIONDATE;

	private static final Logger log = LoggerFactory.getLogger(ProjectRepositoryServiceFS.class);

	public ProjectRepositoryServiceFS(String projectsDirectory) {
		final String normalizedDirectory = FilenameUtils.normalize(new File(projectsDirectory).getAbsolutePath());
		createProjectsDirectory(normalizedDirectory);

		this.projectsDirectory = normalizedDirectory;
	}

	private static void createProjectsDirectory(String projectsDirectory) {
		File projectsFolder = new File(System.getProperty("user.dir") + File.separator + projectsDirectory);

		if (!projectsFolder.exists()) {
			projectsFolder.mkdirs();
			log.info("Projects directory " + projectsDirectory + " created.");
		}
	}

	@Override
	public ProjectResource createProject(String name, String commitMessage) throws ResourceAlreadyExistsError {
		log.info("Create a new project. Name : " + name);
		File projectFolder = new File(projectsDirectory + File.separator + name);
		createProjectDirectory(projectFolder);

		Map<String, String> properties = addProjectResourceProperties(new HashMap<String, String>());
		createMetaFile(addSystemProperties(properties), projectFolder);
		log.info("Directory for Project " + name + " created.");

		return (ProjectResource) createQuery().path(name).singleResult();
	}

	private Map<String, String> addSystemProperties(Map<String, String> source) {
		if (!source.containsKey(META_PROPERTY_CREATIONDATE)) {
			source.put(META_PROPERTY_CREATIONDATE, Long.toString(new Date().getTime()));
		}
		return source;
	}

	@Override
	public ProjectResource createProject(String name, Map<String, String> properties, String commitMessage)
			throws ResourceAlreadyExistsError {
		log.info("Create a new project. Name : " + name);
		File projectFolder = new File(projectsDirectory + File.separator + name);
		createProjectDirectory(projectFolder);

		properties = addProjectResourceProperties(properties);
		createMetaFile(addSystemProperties(properties), projectFolder);
		log.info("Directory for Project " + name + " created.");

		return (ProjectResource) createQuery().path(name).singleResult();
	}

	@Override
	public ResourceQueryFS createQuery() {
		log.info("Creating ResourceQuery");
		return new ResourceQueryFS(projectsDirectory);
	}

	@Override
	public void deleteResource(Resource resource) {
		if (resource == null)
			return;
		log.info("Delete a resource. Name : " + resource.getName() + "; Path: " + resource.getPath());
		resource.setPath(projectsDirectory + File.separator + FilenameUtils.normalize(resource.getPath()));

		String arr[] = resource.getPath().split(Pattern.quote(File.separator));
		String folder = arr[arr.length - 2];
		File file = new File(resource.getPath());
		File metaPropertyFile = new File(projectsDirectory + File.separator
				+ FilenameUtils.normalize(folder + File.separator + "." + resource.getName() + META_PROPERTY_FILENAME));
		FileUtils.deleteQuietly(file);
		FileUtils.deleteQuietly(metaPropertyFile);
		log.info("Resource deleted. (Name : " + resource.getName() + "; Path: " + resource.getPath() + ")");
	}

	@Override
	public ResourceContent getResourceContent(FileResource fileResource) {
		log.info("Getting content for resource with path : " + fileResource.getPath());
		fileResource.setPath(projectsDirectory + File.separator + FilenameUtils.normalize(fileResource.getPath()));

		ResourceContent result = new ResourceContent();
		result.setContent(readContentFromFile(new File(fileResource.getPath())));
		result.setPath(fileResource.getPath());
		log.info("Content for resource with path " + fileResource.getPath() + " found.");

		return result;
	}

	@Override
	public void uploadResource(String commitComment, UploadHandle... handles) {

		for (UploadHandle handle : handles) {
			handle.setPath(FilenameUtils.normalize(handle.getPath()));
		}

		log.info("Uploading resource. Commit comment: " + commitComment);

		for (UploadHandle handle : handles) {
			handle.setPath(projectsDirectory + File.separator + handle.getPath());
			if (!(handle instanceof ProjectUploadHandle)) {
				checkProjectExists(handle.getPath());
			}
			createResource(handle);
			handle.setPath(handle.getPath().replace(projectsDirectory + File.separatorChar, ""));
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateFolderResourceProperties(FolderResource resource, Map<String, String> properties) {
		String metaFileDirectoryPath = projectsDirectory + File.separator + resource.getPath();
		File metaFileDirectory = new File(metaFileDirectoryPath);
		File metaFile = new File(metaFileDirectoryPath, META_PROPERTY_FILENAME);
		Properties metaProperties = new Properties();
		try {
			metaProperties.loadFromXML(new FileInputStream(metaFile));
		} catch (Exception ex) {
			// Nothing to do here.
		}
		HashMap<String, String> map = new HashMap(metaProperties);
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			if (!ProjectRepositoryConstants.IMMUTABLE_META_PROPERTY_SET.contains(entry.getKey())) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		createMetaFile(map, metaFileDirectory);
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
	public boolean isLocked(Resource resource) {
		return false;
	}

	@Override
	public void lock(Resource resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void tag(Resource resource, String tag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void unlock(Resource resource) {
		throw new UnsupportedOperationException();
	}

	public String getProjectsDirectory() {
		return projectsDirectory;
	}

	private void createProjectDirectory(File file) {

		if (!file.exists()) {
			file.mkdirs();
		} else {
			log.info("Directory " + file.getPath() + " already existed.");
			throw new ResourceAlreadyExistsError(
					"Directory for Project (Path: " + file.getPath() + ") already exists.");
		}
	}

	private byte[] readContentFromFile(File file) {
		byte[] content = new byte[(int) file.length()];

		if (file.exists()) {

			try {
				content = FileUtils.readFileToByteArray(file);
			} catch (IOException e) {
				throw new ProjectRepositoryException("File cannot be read into byte stream");
			}
		} else {
			log.info("Resource not found with path: " + file.getPath());
			return null;
		}

		return content;
	}

	private void checkProjectExists(String path) {

		if (createQuery().name(getProjectName(path)).list().size() == 0) {
			throw new NotExistingResourceError("Project with name " + getProjectName(path) + " does not exist.");
		}

	}

	private String getProjectName(String path) {
		String[] pathArray = StringUtils.split(StringUtils.remove(FilenameUtils.getPath(path),
				FilenameUtils.getPath(projectsDirectory + File.separator)), File.separator);

		return pathArray[0];
	}

	private void createResource(UploadHandle content) {
		addSystemProperties(content.getProperties());
		if (content instanceof FolderUploadHandle) {
			File folder = createFolder(content.getPath());
			createMetaFile(content.getProperties(), folder);
		} else if (content instanceof FileUploadHandle) {
			File createdFile = null;
			FileUploadHandle fileContent = (FileUploadHandle) content;

			if (new File(fileContent.getPath()).exists()) {
				createdFile = updateFile(fileContent);
			} else {
				createdFile = createFile(fileContent);
				createMetaFile(content.getProperties(), createdFile);
			}
		} else if (content instanceof ProjectUploadHandle) {
			File projectFolder = new File(content.getPath());
			createMetaFile(content.getProperties(), projectFolder);
		} else {
			throw new WrongUploadHandleTypeError("UploadHandle ist not of type FileUploadHandle or FolderUploadHandle");
		}
	}

	private void createMetaFile(Map<String, String> properties, File source) {
		Properties projectProps = new Properties();
		projectProps.putAll(properties);
		ByteArrayOutputStream projectPropXml = new ByteArrayOutputStream();
		try {
			File metaFile = new File(source, META_PROPERTY_FILENAME);
			projectProps.storeToXML(projectPropXml, "");
			if (!source.isDirectory()) {
				metaFile = new File(source.getParent(), "." + source.getName() + META_PROPERTY_FILENAME);
			}
			FileUtils.writeByteArrayToFile(metaFile, projectPropXml.toByteArray());
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	private File createFolder(String path) {
		File file = new File(path);

		if (file.exists()) {
			log.info("Folder with path " + path + " already exists.");
			throw new ResourceAlreadyExistsError("Directory (Path: " + file.getPath() + ") already exists.");
		} else {
			file.mkdirs();
			log.info("Folder for resource with path " + path + " created.");
		}

		return file;
	}

	private File updateFile(FileUploadHandle content) {

		try {
			FileUtils.writeByteArrayToFile(new File(content.getPath()), content.getContent());
			log.info("Content for resource with path " + content.getPath() + " updated.");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return new File(content.getPath());
	}

	private File createFile(FileUploadHandle content) {
		File file = new File(content.getPath());
		try {
			// to create all parent directories first
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			file.createNewFile();
			FileUtils.writeByteArrayToFile(file, content.getContent());
			log.info("File for resource with path " + content.getPath() + " created.");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return file;
	}

	public String theProjectsDirectory() {
		return this.projectsDirectory;
	}

	private Map<String, String> addProjectResourceProperties(Map<String, String> source) {
		source.put(ProjectRepositoryConstants.META_PROPERTY_IS_PROJECT, String.valueOf(true));
		return source;
	}
}

/* EOF */
