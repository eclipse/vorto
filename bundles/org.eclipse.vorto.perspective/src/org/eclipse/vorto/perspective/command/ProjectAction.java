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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.perspective.view.ILocalModelWorkspace;

public abstract class ProjectAction extends Action {

	protected ILocalModelWorkspace localWorkspace;
	
	public ProjectAction(String text, Image img, ILocalModelWorkspace localWorkspace) {
		super(text,ImageDescriptor.createFromImage(img));
		this.localWorkspace = localWorkspace;
	}
	
	@Override
	public void run() {
		if (localWorkspace.getProjectBrowser().getSelectedProject() == null) {
			MessageDialog.open(MessageDialog.ERROR,
					localWorkspace.getShell(), "No Model Project", "Please create a model project first!", SWT.NONE);
		} else {
			doAction();
		}
	}
	
	protected abstract void doAction();
}
