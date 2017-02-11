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
package org.eclipse.vorto.codegen.ui.progresstask;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ProgressTaskExecutionService {

	private static ProgressTaskExecutionService service;

	public static ProgressTaskExecutionService getProgressTaskExecutionService() {
		if (service == null) {
			service = new ProgressTaskExecutionService();
		}
		return service;
	}

	public void syncRun(final IProgressTask task) {

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();

				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(
						shell);

				try {
					progressDialog.run(false, true, task);
				} catch (Exception ex) {
					ex.printStackTrace();
					MessageDialog.openError(shell, task.getErrorMessage(),
							getExceptionMsg(ex, "Error creating project"));
				}

			}
		});

	}

	private String getExceptionMsg(Exception e, String defaultMsg) {
		String errorMsg = "";
		if (!StringUtils.isEmpty(e.getMessage())) {
			errorMsg = e.getMessage();
		} else if (e.getCause() != null
				&& !StringUtils.isEmpty(e.getCause().getMessage())) {
			errorMsg = e.getCause().getMessage();
		}
		return errorMsg;
	}

}
