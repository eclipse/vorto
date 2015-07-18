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
package org.eclipse.vorto.perspective.dnd;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.vorto.core.model.DatatypeModelProject;
import org.eclipse.vorto.core.model.FunctionblockModelProject;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;

public class DatatypeProjectDropListener extends ViewerDropAdapter {

	public DatatypeProjectDropListener(Viewer viewer) {
		super(viewer);
	}

	@Override
	public boolean performDrop(Object data) {

		IModelProject targetProject = (IModelProject) this.getCurrentTarget();

		IModelProject dataTypeModelProject = (IModelProject) ((IStructuredSelection)data).getFirstElement();
		
		if (dataTypeModelProject instanceof DatatypeModelProject && !targetProject.equals(dataTypeModelProject) ) {
			targetProject.addReference(dataTypeModelProject);
			ModelProjectServiceFactory.getDefault().save(targetProject);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		return target instanceof FunctionblockModelProject
				|| target instanceof DatatypeModelProject;
	}
}
