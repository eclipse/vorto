/*******************************************************************************
/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/


package org.eclipse.vorto.remoterepository.service.governance.exception;

public class DuplicatedModelIDException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2024213028513839757L;
	/**
	 * 
	 */
	
	public static final String Duplicated_ModelIdentifier_Content = "Based on the Identifier, the Model already exists in the repository.";
	

	public DuplicatedModelIDException() {
		super(Duplicated_ModelIdentifier_Content);
	}

	public DuplicatedModelIDException(Throwable t) {
		super(Duplicated_ModelIdentifier_Content, t);
	}
}
