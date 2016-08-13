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
package org.eclipse.vorto.perspective.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.vorto.core.ui.model.IModelElement;

public class TreeViewerTemplate {

	private TreeViewer treeViewer;

	public TreeViewerTemplate(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
		init();
	}

	public void update(final TreeViewerCallback callback) {
		if (!Display.getDefault().isDisposed()) {
			Display.getDefault().syncExec(new Runnable() {

				public void run() {
					try {
						callback.doUpdate(treeViewer);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}

	private void init() {
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
	
	protected void openFileInEditor(IFile fileToOpen) {
		if (fileToOpen.exists()) {
			org.eclipse.ui.IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
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
