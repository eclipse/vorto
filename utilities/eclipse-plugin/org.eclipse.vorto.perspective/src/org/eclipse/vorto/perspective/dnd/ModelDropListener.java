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
package org.eclipse.vorto.perspective.dnd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.perspective.view.ILocalModelWorkspace;
import org.eclipse.vorto.perspective.view.ModelProjectTreeViewer;

/**
 * A drop listener that can be configured with : - An allowed target : the
 * target class allowed for this drop listener - Drop actions that consist of a
 * validator and the actual action to be performed if the validator allows the
 * drop.
 *
 */

// -erle- : This needs to be refactored.
public class ModelDropListener extends ViewerDropAdapter {
	
	private ILocalModelWorkspace localModelBrowser;

	private Class<?> allowedTarget;

	private Collection<DropSourceValidatorAndAction> dropActors = new ArrayList<DropSourceValidatorAndAction>();

	public ModelDropListener(Viewer viewer, ILocalModelWorkspace localModelBrowser) {
		super(Objects.requireNonNull(viewer));
		this.localModelBrowser = Objects.requireNonNull(localModelBrowser);
	}

	public ModelDropListener setAllowedTarget(Class<?> allowedTarget) {
		this.allowedTarget = allowedTarget;
		return this;
	}

	public ModelDropListener addDropAction(IDropValidator validator, IDropAction action) {
		Objects.requireNonNull(validator);
		Objects.requireNonNull(action);
		dropActors.add(new DropSourceValidatorAndAction(validator, action));
		return this;
	}

	@SuppressWarnings("unchecked")
	public boolean performDrop(Object data) {
		Object target = getTarget();

		if (data instanceof IStructuredSelection) {
			Object droppedResource = ((IStructuredSelection) data).getFirstElement();
			IModelElement result = null;
			for (DropSourceValidatorAndAction dropActor : dropActors) {
				if (dropActor.validator.allow(target, droppedResource)) {
					result = dropActor.action.performDrop(target, droppedResource);
					break;
				}
			}

			if (target != null) {
				ModelProjectTreeViewer viewer = (ModelProjectTreeViewer) this.getViewer();
				viewer.getLocalModelWorkspace().refreshCurrent();
				
				if (target instanceof IModelElement) {
					IModelElement targetModelElement = findTarget((IModelElement) target,
							(Collection<IModelElement>) viewer.getInput());
					if (targetModelElement != null) {
						viewer.expandToLevel(targetModelElement, 1);
					}
				}
				
				return true;
			}

		}

		return false;
	}

	private IModelElement findTarget(IModelElement target, Collection<IModelElement> inputModelElements) {
		for(IModelElement e : inputModelElements) {
			if (e.getId().equals(target.getId())) {
				return e;
			}
		}
		return null;
	}

	private Object getTarget() {
		Object target = this.getCurrentTarget();

		if (target == null) {
			Viewer viewer = this.getViewer();
			if (viewer instanceof ModelProjectTreeViewer) {
				target = ((ModelProjectTreeViewer) viewer).getLocalModelWorkspace().getProjectBrowser().getSelectedProject();
			}
		} else if (target instanceof IModelElement) {
			// Get the latest version of this IModelElement
			IModelProject project = localModelBrowser.getProjectBrowser().getSelectedProject();
			target = project.getModelElementById(((IModelElement) target).getId());
		} else {
			throw new RuntimeException("Target is not an IModelElement");
		}

		return target;
	}

	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		return allowedTarget == null || allowedTarget.isInstance(target);
	}

	private class DropSourceValidatorAndAction {
		private IDropValidator validator;
		private IDropAction action;

		public DropSourceValidatorAndAction(IDropValidator validator, IDropAction action) {
			this.validator = validator;
			this.action = action;
		}
	}
}
