package org.eclipse.vorto.perspective.command;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.core.api.repository.CheckInModelException;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.model.IModelProject;

import com.google.common.io.ByteStreams;

public class CheckinModelHandler extends AbstractHandler {

	private IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IModelProject project = getProject(event);

		try {
			modelRepo.saveModel(project.getModelFile().getName(),
					ByteStreams.toByteArray(project.getModelFile().getContents()));
			MessageDisplayFactory.getMessageDisplay().display("Model " + project.getModelFile().getName() + " saved to repository.");
		} catch (CheckInModelException | IOException | CoreException e) {
			MessageDisplayFactory.getMessageDisplay().displayError("Error uploading file to repository");
			MessageDisplayFactory.getMessageDisplay().displayError(e);
		}

		return project;
	}

	private IModelProject getProject(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (@SuppressWarnings("unchecked")
			Iterator<Object> iterator = strucSelection.iterator(); iterator.hasNext();) {
				Object element = iterator.next();
				return (IModelProject) element;
			}
		}
		return null;
	}
}
