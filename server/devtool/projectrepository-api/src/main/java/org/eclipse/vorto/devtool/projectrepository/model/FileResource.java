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

/**
 * The class FileResource represents a file specified by a path.
 *
 */
public class FileResource extends Resource {

    private String encoding;

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Encoding of a FileResource.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Returns the type of the resource.
     */
    @Override public ResourceType getType() {
        return ResourceType.FileResource;
    }

	@Override
	public String toString() {
		return "FileResource [encoding=" + encoding + ", toString()=" + super.toString() + "]";
	}
    
    

}

/* EOF */
