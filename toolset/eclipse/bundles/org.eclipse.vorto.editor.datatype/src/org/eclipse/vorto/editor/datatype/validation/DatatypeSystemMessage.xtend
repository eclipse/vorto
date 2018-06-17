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
 
 package org.eclipse.vorto.editor.datatype.validation

class DatatypeSystemMessage {
	public static final String ERROR_SUPERTYPE_CIRCULAR_REF = 'Super type has circular reference'
	
	public static final String ERROR_PROPERTY_TYPE_NOT_IMPORTED = "This property type has not been imported"
	
	public static final String ERROR_DUPLICATED_ENTITY_NAME = 'Entity name has been defined'
	public static final String ERROR_DUPLICATED_PROPERTY_NAME = 'Property name has been defined'

	public static final String ERROR_ENTITYNAME_INVALID_CAMELCASE = 'Entity name must begin with a capital letter'
	public static final String ERROR_PROPNAME_SUFFIX_TS = 'Invalid property name with suffix "TS"'
	public static final String ERROR_ENTITYNAME_SUFFIX_REPLY = 'Invalid property name with suffix "Reply"'
	public static final String ERROR_DUPLICATED_CONSTRAINT = 'Constraint Type has been defined'
	public static final String ERROR_CONSTRAINT_ENTITY = 'Constraint cannot be imposed on Entity or Enum'

	public static final String ERROR_CONSTRAINT_VALUE_INT = 'This constraint value must be of an Integer'
	public static final String ERROR_CONSTRAINT_VALUE_FLOAT = 'This constraint value must be of a Float'
	public static final String ERROR_CONSTRAINT_VALUE_LONG = 'This constraint value must be of a Long'
	public static final String ERROR_CONSTRAINT_VALUE_BOOLEAN = 'This constraint value must be of a Boolean'
	public static final String ERROR_CONSTRAINT_VALUE_SHORT = 'This constraint value must be of a Short'
	public static final String ERROR_CONSTRAINT_VALUE_DOUBLE= 'This constraint value must be of a Double'
	public static final String ERROR_CONSTRAINT_VALUE_BYTE= 'This constraint value must be of a Byte'
	public static final String ERROR_CONSTRAINT_VALUE= 'This constraint value must be same type of property'
	public static final String ERROR_CONSTRAINT_VALUE_DATETIME = 'Value expected should be in ISO 8196 Date Format e.g: 2002-05-30T09:30:10+06:00'
	public static final String ERROR_CONSTRAINTTYPE_INVALID = "Constraint cannot apply on this property's datatype"
	
	public static final String ERROR_DUPLICATED_ENUM_LITERAL = 'Enumeration literal has been defined'
	public static final String ERROR_DUPLICATED_ENUM_NAME = 'Enumeration has been defined'
	public static final String ERROR_ENUMNAME_INVALID_CAMELCASE = 'Enum name must begin with a capital letter'

	public static final String ERROR_ENTITY_SAME_ENUMNAME = 'Enumeration name cannot be same as entity name'
	public static final String ERROR_ENUM_CANNOT_BE_EMPTY = 'Enumeration cannot have zero literals'
	
	public static final String ERROR_MIMETYPE_FOR_BYTE = "MIMEType only applies to byte array, have you forgotten to add 'multiple' ?"
}