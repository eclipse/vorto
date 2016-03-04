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
package org.eclipse.vorto.perspective.command;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.core.api.repository.CheckInModelException;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.api.repository.UploadResult;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.perspective.view.ModelUploadDialog;

import com.google.common.io.ByteStreams;

public class CheckinModelHandler extends AbstractHandler {

	private IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IModelProject project = getProject(event);

		try {
			UploadResult uploadResult = modelRepo.upload(project.getModelFile().getName(),
					ByteStreams.toByteArray(project.getModelFile().getContents()));
			ModelUploadDialog uploadDialog = new ModelUploadDialog(HandlerUtil.getActiveShell(event), uploadResult);
			uploadDialog.create();
			int result = uploadDialog.open();
			if (uploadResult.statusOk() && result == Window.OK) {
				modelRepo.commit(uploadResult.getHandleId());
				MessageDisplayFactory.getMessageDisplay().display(
						"Model " + project.getModelFile().getName() + " saved to repository.");
			}
		} catch (CheckInModelException | IOException | CoreException e) {
			MessageDisplayFactory.getMessageDisplay().displayError("Error uploading file to repository");
			MessageDisplayFactory.getMessageDisplay().displayError(e);
		}

		return project;	
	}

	private IModelProject getProject(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (@SuppressWarnings("unchecked")
			Iterator<Object> iterator = strucSelection.iterator(); iterator.hasNext();) {
				Object element = iterator.next();
				return (IModelProject) element;
			}
		}
		return null;
	}
}
