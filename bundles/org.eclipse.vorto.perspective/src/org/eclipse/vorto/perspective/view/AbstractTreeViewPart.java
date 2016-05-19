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
package org.eclipse.vorto.perspective.view;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.perspective.IModelContentProvider;
import org.eclipse.vorto.perspective.contentprovider.DefaultTreeModelContentProvider;
import org.eclipse.vorto.perspective.labelprovider.DefaultTreeModelLabelProvider;
import org.eclipse.vorto.perspective.listener.ChangeModelProjectListener;
import org.eclipse.vorto.perspective.listener.ProjectSelectionListener;
import org.eclipse.vorto.perspective.listener.RemoveModelProjectListener;
import org.eclipse.vorto.perspective.util.TreeViewerCallback;
import org.eclipse.vorto.perspective.util.TreeViewerTemplate;

public abstract class AbstractTreeViewPart extends ViewPart implements
		IModelContentProvider {

	protected TreeViewer treeViewer;

	protected IContentProvider contentProvider;
	protected ILabelProvider labelProvider;

	protected TreeViewerTemplate treeViewerUpdateTemplate;

	private IResourceChangeListener removeChangeListener = null;
	private IResourceChangeListener refreshAndExpandListener = null;

	private IProject selectedProject = null;

	public void createPartControl(Composite parent) {
		init();
		// Create the tree viewer as a child of the composite parent
		treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);

		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(labelProvider);

		treeViewer.setUseHashlookup(true);

		treeViewerUpdateTemplate = new TreeViewerTemplate(treeViewer);

		hookListeners();

		treeViewer.setInput(getContent());

		ColumnViewerToolTipSupport.enableFor(treeViewer);

		getSite().setSelectionProvider(treeViewer);
		initContextMenu();
	}

	protected void init() {
		DefaultTreeModelContentProvider contentProvider = new DefaultTreeModelContentProvider();
		this.contentProvider = contentProvider;
		this.labelProvider = new DefaultTreeModelLabelProvider();
	}

	@Override
	public void dispose() {
		super.dispose();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(removeChangeListener);
		workspace.removeResourceChangeListener(refreshAndExpandListener);
	}

	private void initContextMenu() {
		// initalize the context menu
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		Menu menu = menuMgr.createContextMenu(this.treeViewer.getTree());
		this.treeViewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.treeViewer);
	}

	protected void hookListeners() {
		addSelectionChangedEventListener(treeViewer);
		addWorkspaceChangeEventListenr();
		addProjectChangeListener();
	}

	private void addProjectChangeListener() {
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(ProjectSelectionViewPart.PROJECT_SELECT_VIEW_ID, new ISelectionListener() {
			
			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if(selection instanceof IStructuredSelection) {
					IStructuredSelection iSelection = (IStructuredSelection) selection;
					Object firstElement = iSelection.getFirstElement();
					if(firstElement instanceof IProject) {
						System.out.println("Its Project....");
						IProject project = (IProject) firstElement;
						System.out.println("Project>>>> " + project.getName());
						selectedProject = project;
						
						treeViewerUpdateTemplate.update(new TreeViewerCallback() {

							@Override
							public void doUpdate(TreeViewer treeViewer) {
								treeViewer.setInput(getContent());
							}
						} );
					}
						
					if(firstElement != null)
						System.out.println("ISElection Selected:::::" + firstElement.toString());
				}
			}
		});
		
	}
	
	public IProject getSelectedProject() {
		return selectedProject;
	}

	protected void addWorkspaceChangeEventListenr() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		this.removeChangeListener = new RemoveModelProjectListener(this,
				treeViewerUpdateTemplate);
		this.refreshAndExpandListener = new ChangeModelProjectListener(this,
				treeViewerUpdateTemplate, treeViewer);

		workspace.addResourceChangeListener(removeChangeListener,
				IResourceChangeEvent.PRE_DELETE);
		workspace.addResourceChangeListener(refreshAndExpandListener,
				IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public void setFocus() {
	}

	protected void addSelectionChangedEventListener(TreeViewer treeviewer) {
		treeviewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection().isEmpty()) {
					return;
				}
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event
							.getSelection();
					if (selection.size() > 0) {
						if (selection.getFirstElement() instanceof IModelElement) {
							IModelElement modelElement = (IModelElement) selection
									.getFirstElement();
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
