/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 
 package org.eclipse.vorto.codegen.ui.tasks;

import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IProject
import org.eclipse.vorto.codegen.api.Generated
import org.eclipse.vorto.codegen.api.IGeneratedWriter
import org.eclipse.vorto.codegen.ui.filewrite.FileWriteContext
import org.eclipse.vorto.codegen.ui.filewrite.FileWritingStrategyFactory
import org.eclipse.vorto.codegen.ui.filewrite.IFileWritingStrategy
import org.eclipse.vorto.codegen.ui.progressmonitor.VortoCodeGenInfrastructureProgressMonitorFactory
import org.eclipse.vorto.core.ui.MessageDisplayFactory

/**
 * Eclipse Project Outputter strategy, which writes {@link Generated} content as part of an Eclipse Project
 * 
 */
class ProjectFileOutputter implements IGeneratedWriter {
	
	IProject project = null;
	IFileWritingStrategy strategy;

	new(IProject project) {
		this.project = project;
	}

	override void write(Generated generated) {

		try {
			var IFolder folder = null;
			if (generated.getFolderPath() != null) { // make sure that folders are created before file
				folder = createProjectFolders(generated.folderPath);
			}

			if (generated.fileName != null) {
				
				var IFile generatedFile = null;
				if (folder != null) {
					generatedFile = folder.getFile(generated.fileName);
				} else {
					generatedFile = project.getFile(generated.getFileName());
				}
				
				getFileWritingStrategy.writeFile(new FileWriteContext(new String(generated.getContent(),"UTF-8")),generatedFile);
				VortoCodeGenInfrastructureProgressMonitorFactory.codeGenInfraStatusReporter.monitorInfrastructureInfo("Generated file " + generatedFile.location.toFile.path);
				// MessageDisplayFactory.getMessageDisplay().display("Generated file " + generatedFile.location.toFile.path);
			}
		} catch (Exception ioEx) {
			MessageDisplayFactory.getMessageDisplay.displayError("File Generation failed !" + ioEx.message);
			throw new RuntimeException("Generated resource " + generated + " could not be written", ioEx);
		}
	}

	private def IFolder createProjectFolders(String folderPath) {
		var folder = project.getFolder(folderPath);
		if (!folder.exists()) {
			val foldersCreated = folder.getLocation().toFile().mkdirs()
			if(!foldersCreated) {
				VortoCodeGenInfrastructureProgressMonitorFactory.codeGenInfraStatusReporter.monitorInfrastructureWarning("Folders not created at " + folder.getLocation().toFile().path);
				//MessageDisplayFactory.getMessageDisplay.displayWarning("Folders not created at " + folder.getLocation().toFile().path);
			} else {
				VortoCodeGenInfrastructureProgressMonitorFactory.codeGenInfraStatusReporter.monitorInfrastructureInfo("Folders created at " + folder.getLocation().toFile().path);
				//MessageDisplayFactory.getMessageDisplay.display("Folders created at " + folder.getLocation().toFile().path);
			}
			folder.refreshLocal(1, null);
			folder = project.getFolder(folderPath);
		}
		return folder;
	}
		
	public def IFileWritingStrategy getFileWritingStrategy(){
		if(this.strategy == null){
			this.strategy = FileWritingStrategyFactory.getInstance.genFileStrategy;
		}
		this.strategy
	}
	
	
}
