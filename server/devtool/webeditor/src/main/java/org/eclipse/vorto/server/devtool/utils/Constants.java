/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.vorto.server.devtool.utils;

import java.util.HashMap;

import org.eclipse.vorto.repository.api.ModelType;

public class Constants {

	public static final String DOT = ".";

	public static final String MESSAGE_RESOURCE_CREATED = "resource created";

	public static final String MESSAGE_RESOURCE_ALREADY_EXISTS = "resource already exists";

	public static final String MESSAGE_RESOURCE_DOES_NOT_EXIST = "resource does not exist";

	public static final String VORTO_REPOSITORY_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	public static final String RESOURCE_VERSION = "1.0.0";

	public static final String SUBTYPE_ENTITY = "entity";

	public static final String DUMMY_RESOURCE_INFOMODEL_NAME = "dummy_infomodel";

	public static final String DUMMY_RESOURCE_INFOMODEL_FILE_NAME = DUMMY_RESOURCE_INFOMODEL_NAME
			+ ModelType.InformationModel.getExtension();

	public static final String DUMMY_RESOURCE_FUNCTIONBLOCK_NAME = "dummy_functionblock";

	public static final String DUMMY_RESOURCE_FUNCTIONBLOCK_FILE_NAME = DUMMY_RESOURCE_FUNCTIONBLOCK_NAME
			+ ModelType.Functionblock.getExtension();

	public static final String DUMMY_RESOURCE_DATATYPE_NAME = "dummy_entity";

	public static final String DUMMY_RESOURCE_DATATYPE_FILE_NAME = DUMMY_RESOURCE_DATATYPE_NAME
			+ ModelType.Datatype.getExtension();

	public static final String DUMMY_RESOURCE_VERSION = RESOURCE_VERSION;

	public static final String DUMMY_RESOURCE_NAMESPACE = "org.dummy";

	public static final String DUMMY_RESOURCE_DESCRIPTION = "dummy description";

	public static final HashMap<ModelType, String> MODELTYPE_SERVLET_MAP = new HashMap<>();

	static {
		MODELTYPE_SERVLET_MAP.put(ModelType.Datatype, "/datatype");
		MODELTYPE_SERVLET_MAP.put(ModelType.Functionblock, "/functionblock");
		MODELTYPE_SERVLET_MAP.put(ModelType.InformationModel, "/infomodel");
	}

}
