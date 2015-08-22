package org.eclipse.vorto.perspective.dnd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.vorto.core.model.IModelProject;

/**
 * A drop listener that can be configured with :
 * - An allowed target : the target class allowed for this drop listener
 * - Drop actions that consist of a validator and the actual action to be performed if 
 * the validator allows the drop.
 *
 */
public class ModelDropListener extends ViewerDropAdapter {

	private Class<?> allowedTarget;
	
	private Collection<DropSourceValidatorAndAction> dropActors = new ArrayList<DropSourceValidatorAndAction>();
	
	public ModelDropListener(Viewer viewer) {
		super(viewer);
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

	public boolean performDrop(Object data) {
		IModelProject targetProject = (IModelProject) this.getCurrentTarget();

		if (data instanceof IStructuredSelection) {
			Object droppedResource = ((IStructuredSelection) data)
					.getFirstElement();
			for(DropSourceValidatorAndAction dropActor : dropActors) {
				if (dropActor.validator.allow(targetProject, droppedResource)) {
					dropActor.action.performDrop(targetProject, droppedResource);
				} else {
					System.out.println("Not allowed.");
				}
			}
		}

		return false;
	}

	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		return allowedTarget == null || allowedTarget.isInstance(target);
	}

	private class DropSourceValidatorAndAction {
		IDropValidator validator;
		IDropAction action;
		public DropSourceValidatorAndAction(IDropValidator validator,
				IDropAction action) {
			this.validator = validator;
			this.action = action;
		}
	}
}
