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
package org.eclipse.vorto.devtool.projectrepository.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * The abstract class resource can either be a {@link ProjectResource},
 * {@link FolderResource} or a {@link FileResource}.
 *
 */
public abstract class Resource {

    private String name;
    private String path;
    private Date creationDate;
    private Date lastModified;
    private String version;
    private String[] tags;
    private Map<String,String> properties = new HashMap<String, String>();
    
    public static final String META_PROPERTY_AUTHOR = "author";

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Name of a resource.
     */
    public String getName() {
        return name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Path of a resource.
     */
    public String getPath() {
        return path;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Date a resource was created at.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Author of a resource.
     */
    public String getAuthor() {
        return properties.get(META_PROPERTY_AUTHOR);
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Date a resource was last modified at.
     */
    public Date getLastModified() {
        return lastModified;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Version of a resource.
     */
    public String getVersion() {
        return version;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    /**
     * Tags of a resource.
     */
    public String[] getTags() {
        return tags;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * Returns the type of the resource.
     */
    public abstract ResourceType getType();

	@Override
	public String toString() {
		return "Resource [name=" + name + ", path=" + path + ", creationDate=" + creationDate + ", lastModified="
				+ lastModified + ", author=" + getAuthor() + ", version=" + version + ", tags=" + Arrays.toString(tags)
				+ "]";
	}
    
    
}

/* EOF */
