 package org.eclipse.vorto.perspective.contentprovider;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.vorto.core.api.repository.ModelResource;

public class ModelRepositoryContentProvider implements IStructuredContentProvider {	
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement != null && inputElement instanceof Collection) {
			Collection<ModelResource> modelResources = (Collection<ModelResource>) inputElement;
			return modelResources.toArray();
		} else {
			return new Object[] {};
		}
	}

}
