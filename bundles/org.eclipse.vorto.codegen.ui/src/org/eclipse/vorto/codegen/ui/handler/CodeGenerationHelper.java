/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ui.handler;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.SingleFileContentCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.ZipContentExtractCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.ui.tasks.CopyResourceTask;
import org.eclipse.vorto.codegen.ui.tasks.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.ui.tasks.natures.MavenNature;
import org.eclipse.vorto.core.api.model.model.ModelId;

public class CodeGenerationHelper {
	
	public static void createEclipseProject(ModelId modelId, String serviceKey, IGenerationResult generatedResult) {
		EclipseProjectGenerator<ModelId> generator = new EclipseProjectGenerator<>(modelId.getName()+"_"+serviceKey+"_generated");
		ZipContentExtractCodeGeneratorTask zipContentTask = new ZipContentExtractCodeGeneratorTask(generatedResult.getContent());
		if (generatedResult.getFileName().endsWith(".zip")) {
			generator.addTask(zipContentTask);
		} else {
			generator.addTask(new SingleFileContentCodeGeneratorTask(generatedResult.getFileName(),generatedResult.getContent()));
		}
		generator.generate(modelId,InvocationContext.simpleInvocationContext(), new NullProgressMonitor());
		
		if (zipContentTask.isMavenContent()) {
			createMavenProjectFromGeneratedCode(generator);
		}
	}
	
	private static void createMavenProjectFromGeneratedCode(EclipseProjectGenerator<ModelId> generator) {
		final IProject generatedTempProject = generator.getProject();
		try {
			for (IResource folderResource : generatedTempProject.members(IResource.FOLDER)) {
				if (!(folderResource instanceof IFolder)) { //it could still return eclipse project metadata files :(
					continue;
				}
				EclipseProjectGenerator<IResource> projectGenerator = new EclipseProjectGenerator<>(folderResource.getName());
				projectGenerator.addNature(MavenNature.MAVEN_NATURE_STRING);
				projectGenerator.addTask(new CopyResourceTask<IResource>(folderResource.getLocationURI().toURL(), ""));
				projectGenerator.generate(folderResource,InvocationContext.simpleInvocationContext(), new NullProgressMonitor());
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not postprocess downloaded generated files",e);
		} finally {
			deleteTemporaryDownloadedProject(generatedTempProject);
		}
	}
	
	

	private static void deleteTemporaryDownloadedProject(IProject generatedTempProject) {
		try {
			generatedTempProject.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			throw new RuntimeException("Problem deleting temp project",e);
		}
	}
}
