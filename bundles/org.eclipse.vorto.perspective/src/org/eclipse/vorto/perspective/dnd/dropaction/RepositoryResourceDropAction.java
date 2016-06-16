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
package org.eclipse.vorto.perspective.dnd.dropaction;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.api.repository.RepositoryException;
import org.eclipse.vorto.core.ui.MessageDisplayFactory;
import org.eclipse.vorto.core.ui.exception.ExceptionHandlerFactory;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.perspective.dnd.IDropAction;

/**
 * A drop action for dropping a Model Resource from Repository view to an
 * IModelProject
 *
 */
public class RepositoryResourceDropAction implements IDropAction<IModelProject,ModelResource> {

	private IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();
	
	private ResourceAttributes readOnlyAttribute =new ResourceAttributes();
	
	public RepositoryResourceDropAction() {
		readOnlyAttribute.setReadOnly(false);
	}

	@Override
	public IModelElement performDrop(IModelProject receivingModelElement, ModelResource modelResource) {
		Objects.requireNonNull(receivingModelElement, "receivingModelElement shouldn't be null.");
		Objects.requireNonNull(modelResource, "droppedObject shouldn't be null.");


		return downloadAndSaveModel(receivingModelElement, modelResource.getId());
	}
	
	// Download and save model from repository to local project.
	// It also recursively do the same for the model references.
	private IModelElement downloadAndSaveModel(IModelProject modelProject, ModelId modelId) {
		IModelElement modelElement = null;
		try {
			ModelResource model = modelRepo.getModel(modelId);
			if (model != null) {
				if (!modelProject.exists(modelId)) {
					for (ModelId reference : model.getReferences()) {
						downloadAndSaveModel(modelProject, reference);
					}
					MessageDisplayFactory.getMessageDisplay().display("Downloading " + modelId.toString());
					byte[] modelContent = modelRepo.downloadContent(model.getId());
					modelElement = saveToProject(modelProject, modelContent, modelId);
				} else {
					modelElement = modelProject.getModelElementById(modelId);
				}
			} else {
				MessageDisplayFactory.getMessageDisplay().displayError(
						"Model " + modelId.toString() + " not found in repository.");
			}
		} catch (RepositoryException e) {
			ExceptionHandlerFactory.getHandler().handle(e);
		}

		return modelElement;
	}

	private IModelElement saveToProject(IModelProject project, byte[] modelContent, ModelId modelId) {			
		return project.addModelElement(modelId,new ByteArrayInputStream(modelContent));
	}
}
