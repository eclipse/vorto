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
package org.eclipse.vorto.core.ui;

/**
 * Display message in customized format to user
 * 
 * 
 */
public interface IMessageDisplay {
	/**
	 * Display a general message
	 * 
	 * @param message
	 */
	void display(String message);

	/**
	 * Display a user friendly success message
	 * 
	 * @param successMessge
	 */
	void displaySuccess(String successMessge);

	/**
	 * Display a message that gives user warning
	 * 
	 * @param warningMessage
	 */
	void displayWarning(String warningMessage);

	/**
	 * Display a error message to catch user attention
	 * 
	 * @param errorMessge
	 */
	void displayError(String errorMessge);

	void displayError(Throwable cause);

	/**
	 * Insert a line break in the display
	 */
	void displayNewLine();
	
	/**
	 * Clear current display
	 */
	void clear();
}