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

import java.util.Collection;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.ui.changeevent.ModelProjectChangeEvent;
import org.eclipse.vorto.core.ui.changeevent.ModelProjectDeleteEvent;
import org.eclipse.vorto.core.ui.changeevent.ModelProjectEventListener;
import org.eclipse.vorto.core.ui.changeevent.ModelProjectEventListenerRegistry;
import org.eclipse.vorto.core.ui.changeevent.NewModelProjectEvent;

public class DefaultTreeModelContentProvider implements ITreeContentProvider,
		ModelProjectEventListener {

	private TreeViewer treeViewer = null;

	private IModelContentProvider modelContentProvider;

	public DefaultTreeModelContentProvider(
			IModelContentProvider modelContentProvider) {
		this.modelContentProvider = modelContentProvider;
	}

	public void dispose() {
		ModelProjectEventListenerRegistry.getInstance().remove(this);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.treeViewer = (TreeViewer) viewer;
	}

	/*
	 * @see IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/*
	 * @see ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Collection) {
			return ((Collection<?>) parentElement).toArray();
		} else if (parentElement instanceof IModelElement) {
			IModelElement modelElement = (IModelElement) parentElement;
			return modelElement.getReferences().toArray();
		} else {
			return new Object[0];
		}
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Collection) {
			return true;
		} else if (element instanceof IModelElement) {
			IModelElement modelProject = (IModelElement) element;
			return !modelProject.getReferences().isEmpty();
		}
		return false;
	}

	@Override
	public void onProjectChanged(final ModelProjectChangeEvent event) {
		loadCompleteInputAndExpandProject(event.getModelProject());
	}

	@Override
	public void onProjectDeleted(ModelProjectDeleteEvent deletedEvent) {
		loadCompleteInput();
	}

	@Override
	public void onProjectAdded(NewModelProjectEvent addedEvent) {
		loadCompleteInputAndExpandProject(addedEvent.getModelProject());
	}

	public void refresh() {
		loadCompleteInput();
	}

	protected void loadCompleteInput() {
		loadCompleteInputAndExpandProject(null);
	}

	protected void loadCompleteInputAndExpandProject(final IModelProject modelProject) {
		if (!Display.getDefault().isDisposed()) { 
			Display.getDefault().syncExec(new Runnable() {

				public void run() {
					try {
						treeViewer.setInput(modelContentProvider.getContent());
						if (modelProject != null) {
							//Expand has to be invoked in syncExec block, otherwise invalid thread access exception will be thrown
							expandProject(modelProject);
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
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
}
