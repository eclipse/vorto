/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 
 package org.eclipse.vorto.editor.functionblock.validation


class SystemMessage {
	public static final String ERROR_DUPLICATED_PARAMETER_NAME = 'Parameter name has been defined'
	public static final String ERROR_DUPLICATED_METHOD_NAME = 'Method name has been defined'
	public static final String ERROR_FBNAME_INVALID = 'Function block name must begin with a capital letter'

	public static final String ERROR_OPERATION_SAME_NAME_AS_TYPE = 'Operation name cannot be same as custom type (Enum / Entity) name'
	
	public static final String ERROR_VERSION_PATTERN = "Version is not matching the following pattern: <MAJOR>.<MINOR>.<PATCH>-<QUALIFIER>"
	
	public static final String ERROR_OPERATION_SAME_ENUMNAME = 'Operation name cannot be same as enumeration name'
	
	public static final String ERROR_TYPE_NAME_DUPLICATED = 'Enum or Entity name has been defined.'

	public static final String ERROR_NAMESPACE_PATTERN = 'Namespace should have following pattern: <[a-z0-9]+(.[a-z0-9])*> E.g com.bosch, com.bosch.lightsystem'
	
}