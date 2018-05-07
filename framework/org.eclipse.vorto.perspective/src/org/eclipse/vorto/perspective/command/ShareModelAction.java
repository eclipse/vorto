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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
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
		MessageDialog dialog =
			    new MessageDialog(getViewer().getControl().getShell(), "Share Model", null, 
			    		"To upload your model, please follow the following steps:",
			    		MessageDialog.INFORMATION, new String[] { IDialogConstants.OK_LABEL }, 0) {
			protected Control createCustomArea(Composite parent) {
				FillLayout fillLayout = new FillLayout();
				fillLayout.type = SWT.VERTICAL;
				
				Composite composite = new Composite(parent, SWT.NONE);
				composite.setLayout(fillLayout);
				
				Link link = new Link(composite, SWT.WRAP );
				link.setText("1. Go to <a>http://vorto.eclipse.org</a>." );
				link.addSelectionListener(new SelectionAdapter()  {
					public void widgetSelected(SelectionEvent e) {
				        Program.launch("http://vorto.eclipse.org");
				    }  
				});
				
				String[] instructions = new String[] {
					"2. Click on Login",
					"3. Login with your Github ID",
					"4. Click on Share",
					"5. Click on 'Choose File', select your model and click Upload.",
					"6. If there are no validation errors, click on Check-in."
				};
				
				for (String instruction: instructions) {
					Label label = new Label(composite, SWT.NONE);
				    label.setText(instruction);
				}
				
				return composite;
			}
		};
		
		dialog.open();
	}
	
	protected abstract IModelElement getSelectedElement();
	
	protected abstract TreeViewer getViewer();
}
