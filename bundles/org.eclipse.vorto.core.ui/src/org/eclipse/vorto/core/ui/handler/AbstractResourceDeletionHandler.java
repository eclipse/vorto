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
package org.eclipse.vorto.core.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Default handler that provide confirmation dialog to delete selected resource
 * It can be used in customized user perspective where resource deletion
 * function is not provided User might override the dialog title and detail
 * confirmation message
 */
public abstract class AbstractResourceDeletionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell();

		IResource resourceToDelete = getResource(event);

		final String confirmMsg = getConfirmMessage(resourceToDelete);

		final boolean isOKOrYes = MessageDialog.open(MessageDialog.QUESTION,
				shell, getDialogTitle(), confirmMsg, SWT.NONE);

		if (isOKOrYes) {
			deleteResource(shell, resourceToDelete);
		}

		return null;
	}

	protected abstract IResource getResource(ExecutionEvent event);

	private void deleteResource(Shell shell, final IResource selectedResource) {

		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
		try {
			progressDialog.run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(final IProgressMonitor monitor) {

					monitor.beginTask(getDialogTitle(), 100);

					try {
						selectedResource.delete(true, monitor);
					} catch (CoreException e) {
						throw new RuntimeException(e);
					} finally {
						handlePostDelete(selectedResource);
					}

					monitor.done();
				}

			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param resource
	 */
	protected abstract void handlePostDelete(IResource resource);

	/**
	 * @return title of confirm message dialog
	 */
	protected abstract String getDialogTitle();

	/**
	 * @return Deletion confirm message
	 */
	protected abstract String getConfirmMessage(IResource selectedResource);

}
