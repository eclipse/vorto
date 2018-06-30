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
package org.eclipse.vorto.perspective.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.ui.model.IModelElement;

public class DefaultTreeModelLabelProvider extends ColumnLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Image getImage(Object element) {
		IModelElement e = (IModelElement) element;
		if (e.getDiagnostics() != null && e.getDiagnostics().size() > 0) {
			return e.getErrorImage();
		}
		
		return e.getImage();
	}

	@Override
	public String getText(Object element) {
		IModelElement modelElement = (IModelElement) element;
		ModelId modelId = modelElement.getId();
		// return filename if model is invalid
		return modelId != null ? modelId.getName() : modelElement.getModelFile().getName();
	}

	@Override
	public String getToolTipText(Object element) {
		return ((IModelElement) element).getDescription();
	}
}
