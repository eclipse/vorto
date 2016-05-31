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
package org.eclipse.vorto.perspective.contentprovider;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.vorto.core.ui.model.IModelElement;

public class DefaultTreeModelContentProvider implements ITreeContentProvider{

	private Viewer treeViewer = null;
	

	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.treeViewer =  viewer;
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
}
