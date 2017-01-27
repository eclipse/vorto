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
package org.eclipse.vorto.devtool.projectrepository;

import java.util.Map;

import org.eclipse.vorto.devtool.projectrepository.model.FileResource;
import org.eclipse.vorto.devtool.projectrepository.model.ProjectResource;
import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.devtool.projectrepository.model.ResourceContent;
import org.eclipse.vorto.devtool.projectrepository.model.UploadHandle;
import org.eclipse.vorto.devtool.projectrepository.query.IResourceQuery;

/**
 * The interface ProjectRepositoryService provides CRUD functions for projects,
 * folders and files.
 * 
 */
public interface IProjectRepositoryService {

	/**
	 * Create a new project.
	 * 
	 * @pre createQuery().nameLike(name).list().isEmpty()
	 * @post createQuery().nameLike(name).list().size() == 1
	 * 
	 * @param name
	 *            the name of the project to be created
	 * @param commitMessage
	 * 
	 * @return {@link ProjectResource}
	 * 
	 * @throws ResourceAlreadyExistsError
	 */
	public ProjectResource createProject(String projectName, String commitMessage)
			throws ResourceAlreadyExistsError;

    /**
     * Create a new project with properties.
     *
     * @pre createQuery().nameLike(name).list().isEmpty()
     * @post createQuery().nameLike(name).list().size() == 1
     *
     * @param name
     *            the name of the project to be created
     * @param  properties custom properties
     * @param commitMessage
     *
     * @return {@link ProjectResource}
     *
     * @throws ResourceAlreadyExistsError
     */
    public ProjectResource createProject(String name, Map<String,String> properties, String commitMessage);

	/**
	 * Get list of projects. Details: {@link IResourceQuery}
	 * 
	 * @return {@link IResourceQuery}
	 */
	public IResourceQuery createQuery();

	/**
	 * Get content of a resource.
	 * 
	 * @param fileResource
	 *            the file the content should be got from
	 * 
	 * @return {@link ResourceContent}
	 */
	public ResourceContent getResourceContent(FileResource fileResource);

	/**
	 * Deletes a resource and all its children.
	 * 
	 * @param resource
	 *            the resource to be deleted
	 */
	public void deleteResource(Resource resource);

	/**
	 * Upload files and folders.
	 * 
	 * @param commitComment
	 * @param handle
	 *            ... the uploadHandles for each resource to be commited
	 */
	public void uploadResource(String commitComment, UploadHandle... handle);

	/**
	 * Create a version of a project.
	 * 
	 * @param versionName
	 *            The name of the version. If the version does not exist, a new
	 *            version will be created.
	 * @param projectName
	 *            The name of the project to be stored in the version.
	 */
	public void createVersionOfProject(String versionName, String projectName);

	/**
	 * Delete a version.
	 * 
	 * @param versionName
	 *            The name of the version to be deleted.
	 */
	public void deleteVersion(String versionName);

	/**
	 * Tag a resource.
	 * 
	 * @param resource
	 *            The resource to be tagged.
	 * @param tag
	 *            The tag comment.
	 */
	public void tag(Resource resource, String tag);

	/**
	 * Lock a resource.
	 * 
	 * @param resource
	 *            The resource to be locked.
	 */
	public void lock(Resource resource);

	/**
	 * Check if a resource is locked.
	 * 
	 * @param resource
	 *            The resource to be checked.
	 * 
	 * @return true if the resource is locked, false if not.
	 */
	public boolean isLocked(Resource resource);

	/**
	 * Unlock a resource.
	 * 
	 * @param resource
	 *            The resource to be unlocked.
	 */
	public void unlock(Resource resource);
}

/* EOF */
