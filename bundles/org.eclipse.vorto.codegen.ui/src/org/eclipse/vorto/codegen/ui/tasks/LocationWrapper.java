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
package org.eclipse.vorto.codegen.ui.tasks;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;

/**
 * Workspace aware location for a project
 * 
 */
public class LocationWrapper {

	private static final String ERROR_MESSAGE = "Invalid workspace location provided";
	private String path;
	private String projectName;
	private URI uri;

	public LocationWrapper(String path, String projectName) {

		this.path = path;
		this.projectName = projectName;

		try {
			uri = URIUtil.fromString(path + "/" + projectName);

		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(ERROR_MESSAGE, e);
		}
	}

	public Path getValidPath() {
		return new Path(uri.toString());
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

}
