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
package org.eclipse.vorto.devtool.projectrepository.query;

import java.util.Date;
import java.util.List;

import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.devtool.projectrepository.model.ResourceType;


/**
 * Builds a search query for resources and executes it.
 *
 */
public interface IResourceQuery {

    /**
     * finds all resources with a name like the given name.
     *
     * @param   name
     *
     * @return  new {@link IResourceQuery}
     */
    IResourceQuery nameLike(String name);

    /**
     * finds the resources with the given name.
     *
     * @param   name
     *
     * @return  new {@link IResourceQuery}
     */
    IResourceQuery name(String name);

    /**
     * finds the resource with the given unique path.
     *
     * @param   path
     *
     * @return  new {@link IResourceQuery}
     */
    IResourceQuery path(String path);

    /**
     * finds the resource with the given path and all its children at all
     * levels.
     *
     * @param   path
     *
     * @return  new {@link IResourceQuery}
     */
    IResourceQuery pathLike(String path);

    /**
     * finds all resources with the given author.
     *
     * @param   author
     *
     * @return  new {@link IResourceQuery}
     */
    IResourceQuery author(String author);

    /**
     * finds all resources with the given version.
     *
     * @param   version
     *
     * @return  new {@link IResourceQuery}
     */
    IResourceQuery version(String version);

    /**
     * finds all resources with the given type. Type is either
     * FileResource, FolderResource or ProjectResource.
     *
     * @param   type
     *
     * @return  new {@link IResourceQuery}
     */
    IResourceQuery type(ResourceType type);

    /**
     * finds all resources that are created at the given date.
     *
     * @param   date
     *
     * @return  new {@link IResourceQuery}
     */
    IResourceQuery creationDate(Date date);

    /**
     * finds all resources which were last modified on the specific date.
     *
     * @param   date
     *
     * @return  {@link IResourceQuery}
     */
    IResourceQuery lastModified(Date date);

    /**
     * Executes the query and fetches all resources which match the criteria
     * @return
     */
    List<Resource> list();
    
    /**
     * Executes the query and fetches the expected single result
     * @return
     */
    Resource singleResult();
    
    /**
     * Executes the query and returns the number of records that match the criteria
     * @return
     */
    int count();
    
}

/* EOF */
