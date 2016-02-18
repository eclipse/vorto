/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.model;

public class StatusMessage {

	public static enum Level {
		WARNING, ERROR, OK
	}
	
	private Level level;
	private String message;
	
	private StatusMessage(Level level,String message) {
		this.level = level;
		this.message = message;
	}
	
	public static StatusMessage ok(String message) {
		return new StatusMessage(Level.OK,message);
	}
	
	public static StatusMessage warning(String message) {
		return new StatusMessage(Level.WARNING,message);
	}
	
	public static StatusMessage error(String message) {
		return new StatusMessage(Level.ERROR,message);
	}

	public Level getLevel() {
		return level;
	}

	public String getMessage() {
		return message;
	}
	
	
}
