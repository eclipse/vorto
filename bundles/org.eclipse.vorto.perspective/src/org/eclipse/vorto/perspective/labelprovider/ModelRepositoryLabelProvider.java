/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.perspective.labelprovider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.perspective.util.ImageUtil;

public class ModelRepositoryLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public String getColumnText(Object obj, int index) {
		ModelResource resource = (ModelResource) obj;
		switch (index) {
		case 0:
			return "";
		case 1:
			return resource.getId().getNamespace();
		case 2:
			return resource.getId().getName();
		case 3:
			return resource.getId().getVersion();
		case 4:
			return resource.getDescription();
		default:
			return "";
		}
	}

	public Image getColumnImage(Object obj, int index) {
		if (index == 0) {
			ModelResource resource = (ModelResource) obj;
			return ImageUtil.getImageForModelType(resource.getId().getModelType());
		}
		return null;
	}

	public Image getImage(Object obj) {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}
}
