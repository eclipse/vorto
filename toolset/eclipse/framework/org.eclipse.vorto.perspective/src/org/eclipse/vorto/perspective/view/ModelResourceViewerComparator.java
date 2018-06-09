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
package org.eclipse.vorto.perspective.view;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.vorto.core.api.repository.ModelResource;

class ModelResourceViewerComparator extends ViewerComparator {

	private String columnName;
	private int direction;

	public ModelResourceViewerComparator() {
		columnName = AbstractModelRepositoryViewPart.NAMESPACE;
	}

	public void setColumn(TableColumn column) {
		if (column.getText() != columnName) {
			columnName = column.getText();
			direction = SWT.DOWN;
			return;
		}
		direction = SWT.UP == direction ? SWT.DOWN : SWT.UP;
	}

	public int getSortDirection() {
		return direction;
	};

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int ret = -1;
		ModelResource p1 = (ModelResource) e1;
		ModelResource p2 = (ModelResource) e2;

		switch (columnName) {
		case AbstractModelRepositoryViewPart.NAMESPACE:
			ret = p1.getId().getNamespace().compareTo(p2.getId().getNamespace());
			break;
		case AbstractModelRepositoryViewPart.NAME:
			ret = p1.getDisplayName().compareTo(p2.getDisplayName());
			break;
		case AbstractModelRepositoryViewPart.VERSION:
			ret = p1.getId().getVersion().compareTo(p2.getId().getVersion());
			break;
		case AbstractModelRepositoryViewPart.DESCRIPTION:
			ret = p1.getDescription().compareTo(p2.getDescription());
			break;
		default:
			ret = p1.getId().getModelType().compareTo(p2.getId().getModelType());
			break;
		}

		if (direction == SWT.UP) {
			ret = -ret;
		}
		return ret;
	}
}