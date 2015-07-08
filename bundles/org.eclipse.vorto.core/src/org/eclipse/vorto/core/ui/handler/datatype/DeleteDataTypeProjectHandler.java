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
package org.eclipse.vorto.core.ui.handler.datatype;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IResource;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.core.ui.handler.ProjectDeletionHandler;

public class DeleteDataTypeProjectHandler extends ProjectDeletionHandler {

	private static final String DEFAULT_MESSAGE_DELETE_TITLE = "Delete Shared Type Project";
	private static final String DEFAULT_MESSAGE_CONFIRM_DELETE = "Confirm delete Type Project %s?";

	@Override
	protected IResource getResource(ExecutionEvent event) {
		return ModelProjectServiceFactory.getDefault()
				.getProjectFromSelection().getProject();
	}

	@Override
	protected String getDialogTitle() {
		return DEFAULT_MESSAGE_DELETE_TITLE;
	}

	@Override
	protected String getConfirmMessage(IResource selectedResource) {
		return String.format(DEFAULT_MESSAGE_CONFIRM_DELETE,
				selectedResource.getName());
	}
}