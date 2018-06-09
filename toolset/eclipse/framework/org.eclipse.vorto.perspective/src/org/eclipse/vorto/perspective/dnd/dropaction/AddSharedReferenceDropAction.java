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
package org.eclipse.vorto.perspective.dnd.dropaction;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
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
 * A drop action for dropping an IModelProject to another IModelProject
 *
 */
public class AddSharedReferenceDropAction implements IDropAction<IModelElement, ModelResource> {

	private Class<?> droppedObjectClass = null;

	private IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();

	public AddSharedReferenceDropAction(Class<?> droppedObjectClass) {
		this.droppedObjectClass = droppedObjectClass;
	}

	@Override
	public IModelElement performDrop(IModelElement receivingModelElement, ModelResource modelElementToBeDropped) {

		if (droppedObjectClass.isInstance(modelElementToBeDropped)
				&& !receivingModelElement.equals(modelElementToBeDropped)) {
			Optional<IModelElement> modelToAddAsReference = downloadAndSaveModel(receivingModelElement.getProject(),
					modelElementToBeDropped.getId());

			if (modelToAddAsReference.isPresent() && canAddAsReference(receivingModelElement.getModel(), modelToAddAsReference.get().getModel())) {
				receivingModelElement.addModelReference(modelToAddAsReference.get().getId());
			}
			
			receivingModelElement.save();
			return receivingModelElement;
		}

		return null;
	}

	private boolean canAddAsReference(Model receivingModel, Model modelToAdd) {
		if (receivingModel instanceof Entity || receivingModel instanceof FunctionblockModel) {
			return modelToAdd instanceof Enum || modelToAdd instanceof Entity;
		}
		
		if (receivingModel instanceof InformationModel) {
			return modelToAdd instanceof FunctionblockModel;
		}
		return false;
	}

	// Download and save model from repository to local project.
	// It also recursively do the same for the model references.
	private Optional<IModelElement> downloadAndSaveModel(IModelProject project, ModelId modelId) {
		try {
			ModelResource model = modelRepo.getModel(modelId);
			if (model != null) {
				for (ModelId reference : model.getReferences()) {
					downloadAndSaveModel(project, reference);
				}
				MessageDisplayFactory.getMessageDisplay().display("Downloading " + modelId.toString());
				byte[] modelContent = modelRepo.downloadContent(model.getId());
				return Optional.of(project.addModelElement(model.getId(), new ByteArrayInputStream(modelContent)));
			} else {
				MessageDisplayFactory.getMessageDisplay()
						.displayError("Model " + modelId.toString() + " not found in repository.");
			}
		} catch (RepositoryException e) {
			ExceptionHandlerFactory.getHandler().handle(e);
		}
		
		return Optional.empty();
	}
}
