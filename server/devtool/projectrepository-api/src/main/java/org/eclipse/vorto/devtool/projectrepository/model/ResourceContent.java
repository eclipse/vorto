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
 * The class ResourceContent provides the content for a resource.
 *
 */
public class ResourceContent {

    public byte[] content;
    public String path;

    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * content of a resource.
     */
    public byte[] getContent() {
        return content;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * path of a resource.
     */
    public String getPath() {
        return path;
    }
}

/* EOF */
