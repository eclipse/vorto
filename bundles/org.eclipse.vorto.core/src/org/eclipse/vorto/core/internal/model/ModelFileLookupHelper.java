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
package org.eclipse.vorto.core.internal.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.vorto.core.model.DatatypeModelProject;
import org.eclipse.vorto.core.model.FunctionblockModelProject;
import org.eclipse.vorto.core.model.InformationModelProject;
import org.eclipse.vorto.core.model.ModelType;

public class ModelFileLookupHelper {

	private IProject project = null;

	public ModelFileLookupHelper(IProject project) {
		this.project = project;
	}

	/**
	 * @param modelExtension
	 * @return model file
	 */
	public IFile getModelFileByExtension(String modelExtension) {

		IFolder folder = project.getFolder("src/models");
		IFile modelFile = this.getFileByExtension(folder, modelExtension);

		if (modelFile == null) {
			throw new ModelNotFoundException("No valid model file found",
					project);
		}

		return modelFile;
	}

	private IFile getFileByExtension(IFolder folder, String modelExtension) {
		IResource[] members;
		try {
			members = folder.members();
			for (IResource member : members) {
				if (member instanceof IFile
						&& (member.getFileExtension()
								.equalsIgnoreCase(modelExtension))) {
					return (IFile) member;
				}
			}
		} catch (CoreException e) {
			throw new ModelNotFoundException("Invalid model file found", e,
					project);
		}
		return null;
	}
	
	public IFile[] getSharedFilesByModelType(ModelType type) {
		return getSharedFilesByExtension(getExtension(type));
	}
	
	private String getExtension(ModelType modelType) {
		if (modelType == ModelType.Functionblock) {
			return FunctionblockModelProject.FBMODEL;
		} else if (modelType == ModelType.InformationModel){
			return InformationModelProject.INFOMODEL;
		} else {
			return DatatypeModelProject.DATATYPE;
		}
	}

	public IFile[] getSharedFilesByExtension(String modelExtension) {
		List<IFile> foundFiles = new ArrayList<>();

		foundFiles.addAll(getFilesByExtension(
				project.getFolder("src/shared_models"), modelExtension));

		return foundFiles.toArray(new IFile[foundFiles.size()]);
	}

	public List<IFile> getFilesByExtension(IFolder folder,
			String modelExtension) {
		List<IFile> foundFiles = new ArrayList<>();

		if (!folder.exists()) {
			return new ArrayList<>();
		}

		IResource[] members;
		try {
			members = folder.members();
			for (IResource member : members) {
				if (member instanceof IFile
						&& (member.getFileExtension()
								.equalsIgnoreCase(modelExtension))) {
					foundFiles.add((IFile) member);
				}
			}
		} catch (CoreException e) {
			throw new ModelNotFoundException("Invalid model file found", e,
					project);
		}
		return foundFiles;
	}

	/**
	 * Get model file from src folder
	 * 
	 * @return model file
	 */
	public IFile getModelFile(String modelFileName) {
		IFolder folder = project.getFolder("src/models");
		IFile modelFile = this.getFile(folder, modelFileName);

		if (modelFile == null) {
			throw new ModelNotFoundException("No  model file with name "
					+ modelFileName + " found.", project);
		}
		return modelFile;
	}

	/**
	 * Get model file from shared folder
	 * 
	 * @param modelFileName
	 * @return model file
	 */
	public IFile getSharedFile(String modelFileName) {

		IFolder folder = project.getFolder("src/shared_models");
		return this.getFile(folder, modelFileName);
	}

	/**
	 * 
	 * @param folder
	 * @param modelFileName
	 * @return model file
	 */
	private IFile getFile(IFolder folder, String modelFileName) {
		if (!folder.exists()) {
			return null;
		}

		IResource[] members;
		try {
			members = folder.members();
			for (IResource member : members) {
				if (member instanceof IFile
						&& (member.getName().equalsIgnoreCase(modelFileName))) {
					return (IFile) member;
				}
			}
		} catch (CoreException e) {
			throw new ModelNotFoundException("Invalid model file found", e,
					project);
		}
		return null;
	}
}
