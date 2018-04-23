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
package org.eclipse.vorto.perspective.command;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.perspective.util.ImageUtil;

public abstract class ShareModelAction extends Action {

	public ShareModelAction() {
		super("Share",ImageDescriptor.createFromImage(ImageUtil.getImage("share.gif")));
	}
	
	public static Action newInstance(final TreeViewer viewer, final IModelElement model) {
		return new ShareModelAction() {
			@Override
			protected TreeViewer getViewer() {
				return viewer;
			}

			@Override
			protected IModelElement getSelectedElement() {
				return model;
			}
		};
	}
	
	@Override
	public void run() {
		MessageDialog.open(MessageDialog.INFORMATION, getViewer().getControl().getShell(),
				"Share Model", "To upload your model, please go to http://vorto.eclipse.org", SWT.NONE);
	}
	
	protected abstract IModelElement getSelectedElement();
	
	protected abstract TreeViewer getViewer();
}
