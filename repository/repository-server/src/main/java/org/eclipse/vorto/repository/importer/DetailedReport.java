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
package org.eclipse.vorto.repository.importer;

public class DetailedReport {
	public enum REPORT_MESSAGE_TYPE {
		ERROR,
		WARNING;
	}
	
	private boolean modelExists;
	private String message;
	private REPORT_MESSAGE_TYPE messageType;
	private boolean isAdmin;
	private boolean isOwner;
	private boolean isReleased;

	public DetailedReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean modelExists() {
		return modelExists;
	}
	public void setModelExists(boolean isModelExists) {
		this.modelExists = isModelExists;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public REPORT_MESSAGE_TYPE getMessageType() {
		return messageType;
	}
	public void setMessageType(REPORT_MESSAGE_TYPE messageType) {
		this.messageType = messageType;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public boolean isOwner() {
		return isOwner;
	}
	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}
	public boolean isReleased() {
		return isReleased;
	}
	public void setReleased(boolean isReleased) {
		this.isReleased = isReleased;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isAdmin ? 1231 : 1237);
		result = prime * result + (isOwner ? 1231 : 1237);
		result = prime * result + (isReleased ? 1231 : 1237);
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result + (modelExists ? 1231 : 1237);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetailedReport other = (DetailedReport) obj;
		if (isAdmin != other.isAdmin)
			return false;
		if (isOwner != other.isOwner)
			return false;
		if (isReleased != other.isReleased)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (messageType != other.messageType)
			return false;
		if (modelExists != other.modelExists)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DetailedReport [modelExists=" + modelExists + ", message=" + message + ", messageType=" + messageType
				+ ", isAdmin=" + isAdmin + ", isOwner=" + isOwner + ", isReleased=" + isReleased + "]";
	}

}
