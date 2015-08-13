package org.eclipse.vorto.perspective.command;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.model.IModelProject;

public class CheckinModelHandler extends AbstractHandler {

	private IModelRepository modelRepo = ModelRepositoryFactory
			.getModelRepository();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IModelProject project = getProject(event);
		
		modelRepo.saveModel(project.getModel());

		return project;
	}

	private IModelProject getProject(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();

		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (@SuppressWarnings("unchecked")
			Iterator<Object> iterator = strucSelection.iterator(); iterator
					.hasNext();) {
				Object element = iterator.next();
				return (IModelProject) element;
			}
		}
		return null;
	}
}
