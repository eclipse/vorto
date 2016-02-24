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
package org.eclipse.vorto.repository.governance;

import org.eclipse.vorto.repository.model.ModelUploadHandle;
/**
 * 
 * @author Alexander Edelmann
 *
 */
public class GovernanceResult {
	
	private ModelUploadHandle handle;
	
	private boolean approved = false;
	
	public GovernanceResult(ModelUploadHandle handle, boolean approved) {
		this.handle = handle;
		this.approved = approved;
	}

	public ModelUploadHandle getHandle() {
		return handle;
	}

	public boolean isApproved() {
		return approved;
	}
	
 }
