/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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

import org.eclipse.vorto.repository.model.ModelId;

/**
 * Model class to hold file handle id and associated model details.
 * @author Nagavijay Sivakumar - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class ModelHandle {
	
	private String handleId;
	private ModelId id;
	
	public ModelHandle() {
	}
	public String getHandleId() {
		return handleId;
	}
	public void setHandleId(String handleId) {
		this.handleId = handleId;
	}
	public ModelId getId() {
		return id;
	}
	public void setId(ModelId id) {
		this.id = id;
	}
}
