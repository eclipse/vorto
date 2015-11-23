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
package org.eclipse.vorto.core.ui.handler.infomodel;

import org.eclipse.core.resources.IResource;
import org.eclipse.vorto.core.ui.handler.ProjectDeletionHandler;

public class DeleteInfomodelHandler extends ProjectDeletionHandler {

	private static final String MESSAGE_DELETE_TITLE = "Delete Infomation Model";
	private static final String MESSAGE_CONFIRM_DELETE = "Confirm delete information model %s?";

	@Override
	protected String getDialogTitle() {
		return MESSAGE_DELETE_TITLE;
	}

	@Override
	protected String getConfirmMessage(IResource selectedProject) {
		return String.format(MESSAGE_CONFIRM_DELETE, selectedProject.getName());
	}
}
