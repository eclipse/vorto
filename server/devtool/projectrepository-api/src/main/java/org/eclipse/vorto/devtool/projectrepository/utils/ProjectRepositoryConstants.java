/**
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.devtool.projectrepository.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProjectRepositoryConstants {

	public static final String DOT = ".";
	
	public static final String UNDERSCORE = ".";
	
	public static final String DOT_REGEX = "//.";
	
	public static final String DUMMY = "dummy:/";
	
	public static final String META_PROPERTY_AUTHOR = "author";

	public static final String META_PROPERTY_RESOURCE_ID = "resourceId";

	public static final String META_PROPERTY_NAME = "name";

	public static final String META_PROPERTY_NAMESPACE = "namespace";

	public static final String META_PROPERTY_VERSION = "version";

	public static final String META_PROPERTY_MODEL_TYPE = "modelType";

	public static final String META_PROPERTY_MODEL_SUB_TYPE = "modelSubType";

	public static final String META_PROPERTY_IS_PROJECT = "isProject";

	public static final String META_PROPERTY_REFERENCES = "references";
	
	public static final String META_PROPERTY_DISPLAY_NAME = "displayName";
	
	public static final String META_PROPERTY_COLLABORATORS = "collaborators";
	
	public static final String META_PROPERTY_CREATIONDATE = "meta.createdOn";
	
	public static final String META_PROPERTY_FILE = ".meta.props";
	
	public static final String META_PROPERTY_FILE_NAME = "fileName";
	
	public static final String META_PROPERTY_PROJECT_NAME = "projectName";


	public static Set<String> IMMUTABLE_META_PROPERTY_SET = new HashSet<String>(Arrays.asList(
		META_PROPERTY_AUTHOR,
		META_PROPERTY_IS_PROJECT,
		META_PROPERTY_RESOURCE_ID,
		META_PROPERTY_NAME,
		META_PROPERTY_NAMESPACE,
		META_PROPERTY_VERSION,
		META_PROPERTY_MODEL_SUB_TYPE,
		META_PROPERTY_MODEL_TYPE
	));

}
