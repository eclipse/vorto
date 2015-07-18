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
package org.eclipse.vorto.perspective;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.perspective.util.TreeViewerCallback;
import org.eclipse.vorto.perspective.util.TreeViewerTemplate;

public abstract class AbstractTreeViewPart extends ViewPart implements
		IModelContentProvider, IResourceChangeListener {

	protected TreeViewer treeViewer;

	protected IContentProvider contentProvider;
	protected ILabelProvider labelProvider;
	
	protected TreeViewerTemplate treeViewerUpdateTemplate;

	public void createPartControl(Composite parent) {
		init();
		// Create the tree viewer as a child of the composite parent
		treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);

		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(labelProvider);

		treeViewer.setUseHashlookup(true);

		hookListeners();

		treeViewer.setInput(getContent());
		treeViewerUpdateTemplate = new TreeViewerTemplate(treeViewer);
		
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
		workspace.removeResourceChangeListener(this);
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
	}
	
	public void resourceChanged(IResourceChangeEvent event) {
		if (isResourceRemoved(event)) {
			IModelProject project = getModelProjectOrNull(event.getResource());
			final Set<IModelElement> input = getContent();
			input.remove(project);
			treeViewerUpdateTemplate.update(new TreeViewerCallback() {
				
				@Override
				public void doUpdate(TreeViewer treeViewer) {
					treeViewer.setInput(input);
				}
			});		
		} else if (isAddedOrChanged(event)) {
			final Set<IModelElement> input = getContent();
			final IModelProject project = getModelProjectOrNull(event.getDelta().getAffectedChildren()[0].getResource());

			treeViewerUpdateTemplate.update(new TreeViewerCallback() {
				
				@Override
				public void doUpdate(TreeViewer treeViewer) {
					treeViewer.setInput(input);
					expandProject(project);
				}
			});	
		}

	}
	
	private IModelProject getModelProjectOrNull(IResource resource) {
		if (resource instanceof IProject) {
			try {
				return ModelProjectServiceFactory.getDefault().getProjectFromEclipseProject((IProject)resource);
			} catch(IllegalArgumentException ex) {
				return null;
			}
			} else {
			return null;
		}
	}
	
	private boolean isResourceRemoved(IResourceChangeEvent event) {
		return event.getType() == IResourceChangeEvent.PRE_DELETE;
	}

	private boolean isAddedOrChanged(IResourceChangeEvent event) {
		return event.getDelta() != null
				&& event.getDelta().getAffectedChildren() != null && isModelProject(event.getDelta().getAffectedChildren()[0]);
	}
	
	private boolean isModelProject(IResourceDelta delta) {
		try {
			return getModelProjectOrNull(delta.getResource()) != null;
		} catch(IllegalArgumentException ex) {
			return false;
		}
	}

	protected void addWorkspaceChangeEventListenr() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(this);
	}
	
	@Override
	public void setFocus() {
	}
	
	protected void expandProject(IModelProject modelProject) {
		IModelProject inputModelProject = this
				.getProjectFromTreeViewer(modelProject.getId());
		if (inputModelProject != null) {
			treeViewer.expandToLevel(inputModelProject, 2);
		}
	}

	@SuppressWarnings("rawtypes")
	private IModelProject getProjectFromTreeViewer(ModelId modelId) {
		Set inputs = (Set) treeViewer.getInput();
		for (Object input : inputs) {
			if (input instanceof IModelProject) {
				if (((IModelProject) input).getId().getName()
						.equals(modelId.getName())) {
					return ((IModelProject) input);
				}
			}
		}
		return null;
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
