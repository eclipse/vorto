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
package org.eclipse.vorto.perspective.view;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.perspective.contentprovider.DefaultTreeModelContentProvider;
import org.eclipse.vorto.perspective.labelprovider.DefaultTreeModelLabelProvider;

public abstract class ModelTreeViewer {

	protected ModelProjectTreeViewer treeViewer;

	protected ILocalModelWorkspace localModelWorkspace;

	public ModelTreeViewer(Composite parent, ILocalModelWorkspace localModelWorkspace) {
		this.treeViewer = createTreeViewer(parent, localModelWorkspace);
		this.localModelWorkspace = localModelWorkspace;
		init();
		initContextMenu();
	}

	protected abstract String getLabel();

	protected abstract void initContextMenu();

	private ModelProjectTreeViewer createTreeViewer(Composite parent, ILocalModelWorkspace localModelBrowser) {
		Composite composite = new Composite(parent, SWT.BORDER);

		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;

		composite.setLayout(layout);

		Label label = new Label(composite, SWT.NONE);
		label.setBackground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), 58, 90, 130));
		label.setForeground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), 255, 255, 255));
		label.setText(getLabel());

		FormData lblFormData = new FormData();
		lblFormData.top = new FormAttachment(0, 0);
		lblFormData.left = new FormAttachment(0, 0);
		lblFormData.right = new FormAttachment(100, 0);

		label.setLayoutData(lblFormData);

		ModelProjectTreeViewer treeViewer = new ModelProjectTreeViewer(composite,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL, localModelBrowser);
		FormData viewerFormData = new FormData();
		viewerFormData.top = new FormAttachment(label, 10);
		viewerFormData.left = new FormAttachment(0, 0);
		viewerFormData.right = new FormAttachment(100, 0);
		viewerFormData.bottom = new FormAttachment(100, 0);

		treeViewer.getTree().setLayoutData(viewerFormData);

		treeViewer.setContentProvider(new DefaultTreeModelContentProvider());
		treeViewer.setLabelProvider(new DefaultTreeModelLabelProvider());

		treeViewer.setUseHashlookup(true);

		ColumnViewerToolTipSupport.enableFor(treeViewer);

		return treeViewer;
	}

	public void populate(Collection<IModelElement> modelElements) {
		if (!Display.getDefault().isDisposed()) {
			 Display.getDefault().syncExec(newViewUpdateRunnable(treeViewer, modelElements));
		}
	}

	private Runnable newViewUpdateRunnable(final ModelProjectTreeViewer treeViewer,
			final Collection<IModelElement> modelElements) {
		return new Runnable() {
			public void run() {
				try {
					treeViewer.setInput(modelElements);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	protected void init() {
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection().isEmpty()) {
					return;
				}
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					if (selection.size() > 0) {
						if (selection.getFirstElement() instanceof IModelElement) {
							IModelElement modelElement = (IModelElement) selection.getFirstElement();
							if (modelElement.getModelFile() != null) {
								openFileInEditor(modelElement.getModelFile());
							}
						}
					}
				}
			}
		});
	}

	private void openFileInEditor(IFile fileToOpen) {
		if (fileToOpen.exists()) {
			org.eclipse.ui.IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IDE.openEditor(page, fileToOpen);
			} catch (org.eclipse.ui.PartInitException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File not found for: " + fileToOpen);
		}
	}
}
