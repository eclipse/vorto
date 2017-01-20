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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * The class UploadHandle can either be {@link FolderUploadHandle} or a
 * {@link FileUploadHandle}.
 *
 */
public abstract class UploadHandle {

    /** The serial version UID. */
    private String path;
    private Map<String, String> properties = new HashMap<String, String>();

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

	/**
     * @param  path  the path of the resource.
     */
    public UploadHandle(String path) {
        this.path = path;
    }

    /**
     * @return  path of the resource.
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
	public void setProperty(String key, String value) {
		this.properties.put(key, value);
	}

	@Override
	public String toString() {
		return "UploadHandle [path=" + path + "]";
	}
	
	
}

/* EOF */
