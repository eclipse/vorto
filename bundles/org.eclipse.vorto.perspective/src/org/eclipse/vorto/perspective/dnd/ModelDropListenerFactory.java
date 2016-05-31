/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.perspective.dnd;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.ui.model.DatatypeModelElement;
import org.eclipse.vorto.core.ui.model.FunctionblockModelElement;
import org.eclipse.vorto.core.ui.model.InformationModelElement;
import org.eclipse.vorto.core.ui.model.VortoModelProject;
import org.eclipse.vorto.perspective.dnd.dropaction.AddLocalReferenceDropAction;
import org.eclipse.vorto.perspective.dnd.dropaction.AddSharedReferenceDropAction;
import org.eclipse.vorto.perspective.dnd.dropaction.RepositoryResourceDropAction;
import org.eclipse.vorto.perspective.dnd.dropvalidator.DatatypeValidator;
import org.eclipse.vorto.perspective.dnd.dropvalidator.TargetClassModelTypeValidator;
import org.eclipse.vorto.perspective.dnd.dropvalidator.TargetClassSourceClassValidator;

public class ModelDropListenerFactory {

	public static DropTargetListener datatypeViewPartDropListener(Viewer viewer) {
		return new ModelDropListener(viewer)
				.addDropAction(
						new DatatypeValidator(
								DatatypeModelElement.class,
								DatatypeModelElement.class),
						new AddLocalReferenceDropAction(DatatypeModelElement.class))
				.addDropAction(
						new TargetClassModelTypeValidator(
								VortoModelProject.class, ModelType.Datatype),
						new RepositoryResourceDropAction())
				.addDropAction(
						new TargetClassSourceClassValidator(
								DatatypeModelElement.class,
								ModelResource.class),
						new AddSharedReferenceDropAction(ModelResource.class));
	}
	
	public static DropTargetListener functionblockViewPartDropListener(Viewer viewer) {
		return new ModelDropListener(viewer)
				.addDropAction(
						new TargetClassSourceClassValidator(
								FunctionblockModelElement.class,
								DatatypeModelElement.class),
						new AddLocalReferenceDropAction(DatatypeModelElement.class))
				.addDropAction(
						new TargetClassModelTypeValidator(
								VortoModelProject.class, ModelType.Functionblock),
						new RepositoryResourceDropAction())
				.addDropAction(
						new TargetClassSourceClassValidator(
								FunctionblockModelElement.class,
								ModelResource.class),
						new AddSharedReferenceDropAction(ModelResource.class));
	}
	
	public static DropTargetListener infomodelViewPartDropListener(Viewer viewer) {
		return new ModelDropListener(viewer)
				.addDropAction(
						new TargetClassSourceClassValidator(
								InformationModelElement.class,
								FunctionblockModelElement.class),
						new AddLocalReferenceDropAction(FunctionblockModelElement.class))
				.addDropAction(
						new TargetClassModelTypeValidator(
								VortoModelProject.class, ModelType.InformationModel),
						new RepositoryResourceDropAction())
				.addDropAction(
						new TargetClassSourceClassValidator(
								InformationModelElement.class,
								ModelResource.class),
						new AddSharedReferenceDropAction(ModelResource.class));
	}
}
