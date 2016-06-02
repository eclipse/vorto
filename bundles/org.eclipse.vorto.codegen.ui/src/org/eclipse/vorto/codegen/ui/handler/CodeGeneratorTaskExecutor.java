/*******************************************************************************
 *  Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ui.handler;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;

/**
 * 
 * Executes a code generator as a separate eclipse job
 * 
 */
public class CodeGeneratorTaskExecutor {

	public static final String CODE_GEN_JOBFAMILY = "IoTGenerator";

	public static void execute(final InformationModel informationModel,
			final IVortoCodeGenerator codeGenerator, final InvocationContext invocationContext) {

		MessageDisplayFactory.getMessageDisplay().display(
				"Generating code using " + codeGenerator.getServiceKey());

		Job job = new Job("Generating Code using " + codeGenerator.getServiceKey()) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				monitor.beginTask(
						"Invoking generator [" + codeGenerator.getServiceKey() + "]",
						1);
				try {
					IGenerationResult generatedResult = ((IVortoCodeGenerator) codeGenerator).generate(informationModel,invocationContext);
					CodeGenerationHelper.createEclipseProject(ModelIdFactory.newInstance(informationModel), codeGenerator.getServiceKey(), generatedResult);
					monitor.worked(1);
					if (monitor.isCanceled()) {
						monitor.done();
						return Status.CANCEL_STATUS;
					}

				} catch (Exception e) {
					e.printStackTrace();
					return Status.CANCEL_STATUS;
				}
				monitor.done();
				return Status.OK_STATUS;
			}

			@Override
			public boolean belongsTo(Object family) {
				return family == CODE_GEN_JOBFAMILY;
			}

		};

		job.setUser(true);
		job.schedule();
	}
}
