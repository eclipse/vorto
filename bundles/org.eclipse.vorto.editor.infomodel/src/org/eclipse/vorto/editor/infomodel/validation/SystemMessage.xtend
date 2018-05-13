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
 
 package org.eclipse.vorto.editor.infomodel.validation


class SystemMessage {
	public static final String ERROR_DUPLICATED_FUNCTIONBLOCK_NAME = 'Function Block is already defined'
	public static final String ERROR_FUNCTIONBLOCK_NOT_IMPORTED = 'This Function Block has not yet been imported.'
	public static final String ERROR_INCOMPATIBLE_TYPE = 'The property type is incompatible to base type.'
	public static final String ERROR_INCOMPATIBLE_PRESENCE = 'The presence is incompatible to presence of the base type.'
	public static final String ERROR_INCOMPATIBLE_MULTIPLICITY = 'The multiplicity is incompatible to multiplicity of the base type.'
	public static final String ERROR_INCOMPATIBLE_BREAKABLE = 'The breakable definition is incompatible to base operation.'
	public static final String ERROR_INCOMPATIBLE_PARMS = 'The parameters are incompatible to base operation parameters.'
	public static final String ERROR_INCOMPATIBLE_RETURN_TYPE = 'The return type is incompatible to base type.'
}