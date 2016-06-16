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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.vorto.core.api.repository.CheckInModelException;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.api.repository.UploadResult;
import org.eclipse.vorto.core.ui.MessageDisplayFactory;
import org.eclipse.vorto.core.ui.exception.ExceptionHandlerFactory;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.perspective.util.ImageUtil;
import org.eclipse.vorto.perspective.view.ModelUploadDialog;

import com.google.common.io.ByteStreams;

public abstract class ShareModelAction extends Action {

	private IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();
	
	public ShareModelAction() {
		super("Share",ImageDescriptor.createFromImage(ImageUtil.getImage("share.gif")));
	}
	
	@Override
	public void run() {
		IModelElement modelElement = getSelectedElement();

		try {
			UploadResult uploadResult = modelRepo.upload(modelElement.getModelFile().getName(),
					ByteStreams.toByteArray(modelElement.getModelFile().getContents()));
			ModelUploadDialog uploadDialog = new ModelUploadDialog(getViewer().getControl().getShell(), uploadResult);
			uploadDialog.create();
			int result = uploadDialog.open();
			if (uploadResult.statusOk() && result == Window.OK) {
				modelRepo.commit(uploadResult.getHandleId());
				MessageDisplayFactory.getMessageDisplay().display(
						"Model " + modelElement.getModelFile().getName() + " saved to repository.");
			}
		} catch (Exception e) {
			ExceptionHandlerFactory.getHandler().handle(e);
		}
	}
	
	protected abstract IModelElement getSelectedElement();
	
	protected abstract TreeViewer getViewer();
}
