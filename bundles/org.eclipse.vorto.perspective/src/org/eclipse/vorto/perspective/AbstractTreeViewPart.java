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

import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
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
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.core.ui.changeevent.ModelProjectChangeEvent;
import org.eclipse.vorto.core.ui.changeevent.ModelProjectDeleteEvent;
import org.eclipse.vorto.core.ui.changeevent.ModelProjectEventListenerRegistry;
import org.eclipse.vorto.core.ui.changeevent.NewModelProjectEvent;

public abstract class AbstractTreeViewPart extends ViewPart implements
		IModelContentProvider {

	protected TreeViewer treeViewer;

	protected IContentProvider contentProvider;
	protected ILabelProvider labelProvider;

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
		ColumnViewerToolTipSupport.enableFor(treeViewer);

		getSite().setSelectionProvider(treeViewer);
		initContextMenu();
	}

	protected void init() {
		DefaultTreeModelContentProvider contentProvider = new DefaultTreeModelContentProvider(
				this);
		ModelProjectEventListenerRegistry.getInstance().add(contentProvider);
		this.contentProvider = contentProvider;
		this.labelProvider = new DefaultTreeModelLabelProvider();
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

	public ImageDescriptor getImage(String imageFileName) {

		URL url;
		try {
			url = new URL(
					"platform:/plugin/org.eclipse.vorto.fbeditor.ui/icons/"
							+ imageFileName);
			return ImageDescriptor.createFromURL(url);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected void addWorkspaceChangeEventListenr() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		IResourceChangeListener rcl = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				if (hasChanges(event)) {
					processChanges(event.getDelta());
				}

			}

			private boolean hasChanges(IResourceChangeEvent event) {
				return event.getDelta() != null
						&& event.getDelta().getAffectedChildren() != null;
			}
		};
		workspace.addResourceChangeListener(rcl);
	}

	private void processChanges(IResourceDelta changes) {
		for (IResourceDelta change : changes.getAffectedChildren()) {
			IProject project = change.getResource().getProject();
			IModelChangeProcessor processor = ModelChangeProcessorFactory
					.getModelChangeProcessor(change.getKind());
			processor.processChange(project);

		}
	}

	private static class ModelChangeProcessorFactory {
		private static final IModelChangeProcessor MODEL_DELETE_PROCESSOR = new ModelDeleteProcessor();
		private static final IModelChangeProcessor NEW_MODEL_PROCESSOR = new NewModelProcessor();
		private static final IModelChangeProcessor MODEL_CHANGE_PROCESSOR = new ModelChangeProcessor();

		public static IModelChangeProcessor getModelChangeProcessor(
				int changeKind) {
			if (changeKind == IResourceDelta.REMOVED
					|| changeKind == IResourceDelta.REMOVED_PHANTOM) {
				return MODEL_DELETE_PROCESSOR;
			} else if (changeKind == IResourceDelta.ADDED
					|| changeKind == IResourceDelta.ADDED_PHANTOM) {
				return NEW_MODEL_PROCESSOR;
			} else {
				return MODEL_CHANGE_PROCESSOR;
			}
		}
	}

	/**
	 * Provide operation to handle model project change
	 */
	private static interface IModelChangeProcessor {
		/**
		 * Process model change based give project
		 * 
		 * @param project
		 *            : Project that has been changed
		 */
		void processChange(IProject project);
	}

	private final static class ModelDeleteProcessor implements
			IModelChangeProcessor {

		public ModelDeleteProcessor() {

		}

		@Override
		public void processChange(IProject project) {
			ModelProjectEventListenerRegistry.getInstance().sendDeleteEvent(
					new ModelProjectDeleteEvent(project.getName()));
		}

	}

	private final static class NewModelProcessor implements
			IModelChangeProcessor {

		public NewModelProcessor() {

		}

		@Override
		public void processChange(IProject project) {
			if (isModelProject(project)) {
				IModelProject modelProject = getModelProject(project);
				ModelProjectEventListenerRegistry.getInstance().sendAddedEvent(
						new NewModelProjectEvent(modelProject));
			}
		}

	}

	private final static class ModelChangeProcessor implements
			IModelChangeProcessor {

		public ModelChangeProcessor() {

		}

		@Override
		public void processChange(IProject project) {
			if (isModelProject(project)) {
				IModelProject modelProject = getModelProject(project);
				ModelProjectEventListenerRegistry.getInstance()
						.sendChangeEvent(
								new ModelProjectChangeEvent(modelProject));
			}
		}

	}

	private static boolean isModelProject(IProject project) {
		IModelProject modelProject = getModelProject(project);
		return modelProject != null;
	}

	private static IModelProject getModelProject(IProject project) {

		try {
			return ModelProjectServiceFactory.getDefault()
					.getProjectFromEclipseProject(project);
		} catch (IllegalArgumentException e) {
			// ingore model parsing exception due to model not found

		}
		return null;
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
