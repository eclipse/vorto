/*******************************************************************************
 * Copyright (c) 2014-2018 Bosch Software Innovations GmbH and others.
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
	
	public static final String ERROR_VERSION_PATTERN = "Version is not matching the following pattern: <MAJOR>.<MINOR>.<PATCH>-<QUALIFIER>"
	
	public static final String ERROR_TYPE_NAME_DUPLICATED = 'Enum or Entity name has been defined.'

	public static final String ERROR_NAMESPACE_PATTERN = 'Namespace should have following pattern: <[a-z0-9]+(.[a-z0-9])*> E.g com.bosch, com.bosch.lightsystem'
	
	public static final String ERROR_REF_PARAM_NOT_IMPORTED = "Reference parameter has not yet been imported.";
	
	public static final String ERROR_OBJECT_RETURN_TYPE_NOT_IMPORTED = "Return type has not yet been imported.";

	public static final String ERROR_INCOMPATIBLE_TYPE = 'The property type is incompatible to base type.'
	public static final String ERROR_INCOMPATIBLE_PRESENCE = 'The presence is incompatible to presence of the base type.'
	public static final String ERROR_INCOMPATIBLE_MULTIPLICITY = 'The multiplicity is incompatible to multiplicity of the base type.'
	public static final String ERROR_INCOMPATIBLE_BREAKABLE = 'The breakable definition is incompatible to base operation.'
	public static final String ERROR_INCOMPATIBLE_PARMS = 'The parameters are incompatible to base operation parameters.'
	public static final String ERROR_INCOMPATIBLE_RETURN_TYPE = 'The return type is incompatible to base type.'
	
	public static final String ERROR_OVERWRITTEN_CONSTRAINT_MIN_TOO_SMALL = 'The given MIN constraint needs to be bigger or equal as the base MIN value.'
	public static final String ERROR_OVERWRITTEN_CONSTRAINT_MAX_TOO_BIG = 'The given MAX constraint needs to be smaller or equal as the base MAX value.'
	public static final String ERROR_OVERWRITTEN_CONSTRAINT_NULLABLE = 'If the constraint NULLABLE of the base type is false than it can not changed.'
	public static final String ERROR_OVERWRITTEN_CONSTRAINT_STRLEN = 'The given STRLEN constraint needs to be smaller or equal as the base STRLEN value.'
	public static final String ERROR_OVERWRITTEN_CONSTRAINT_ALREADY_DEFINED = 'The constraint is already defined in the base property.'
	public static final String ERROR_OVERWRITTEN_PROPERTY_ATTRIBUTE_TYPE = 'Property attributes can not be overwritten.'
	
}