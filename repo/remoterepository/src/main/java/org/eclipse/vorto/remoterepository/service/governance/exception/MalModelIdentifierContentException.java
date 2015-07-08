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

public class MalModelIdentifierContentException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7394925282229082741L;
	public static final String Mal_ModelIdentifier_Content = "The Identifier does not match with information from Model Content";
	

	public MalModelIdentifierContentException() {
		super(Mal_ModelIdentifier_Content);
	}

	public MalModelIdentifierContentException(Throwable t) {
		super(Mal_ModelIdentifier_Content, t);
	}
}
