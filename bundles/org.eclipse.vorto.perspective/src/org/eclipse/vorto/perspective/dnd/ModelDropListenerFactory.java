/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
import org.eclipse.vorto.core.model.DatatypeModelProject;
import org.eclipse.vorto.core.model.FunctionblockModelProject;
import org.eclipse.vorto.core.model.InformationModelProject;
import org.eclipse.vorto.perspective.dnd.dropaction.CreateProjectDropAction;
import org.eclipse.vorto.perspective.dnd.dropaction.ModelProjectDropAction;
import org.eclipse.vorto.perspective.dnd.dropaction.RepositoryResourceDropAction;
import org.eclipse.vorto.perspective.dnd.dropvalidator.DatatypeValidator;
import org.eclipse.vorto.perspective.dnd.dropvalidator.ModelTypeValidator;
import org.eclipse.vorto.perspective.dnd.dropvalidator.TargetClassModelTypeValidator;
import org.eclipse.vorto.perspective.dnd.dropvalidator.TargetClassSourceClassValidator;

public class ModelDropListenerFactory {

	public static DropTargetListener datatypeViewPartDropListener(Viewer viewer) {
		return new ModelDropListener(viewer)
				.addDropAction(
						new DatatypeValidator(
								DatatypeModelProject.class,
								DatatypeModelProject.class),
						new ModelProjectDropAction(DatatypeModelProject.class))
				.addDropAction(
						new TargetClassModelTypeValidator(
								DatatypeModelProject.class, ModelType.Datatype),
						new RepositoryResourceDropAction())
				.addDropAction(
						new ModelTypeValidator(ModelType.Datatype),
						new CreateProjectDropAction());
	}
	
	public static DropTargetListener functionblockViewPartDropListener(Viewer viewer) {
		return new ModelDropListener(viewer)
				.addDropAction(
						new TargetClassSourceClassValidator(
								FunctionblockModelProject.class,
								DatatypeModelProject.class),
						new ModelProjectDropAction(DatatypeModelProject.class))
				.addDropAction(
						new TargetClassModelTypeValidator(
								FunctionblockModelProject.class, ModelType.Datatype),
						new RepositoryResourceDropAction())
				.addDropAction(
						new ModelTypeValidator(ModelType.Functionblock),
						new CreateProjectDropAction());
	}
	
	public static DropTargetListener infomodelViewPartDropListener(Viewer viewer) {
		return new ModelDropListener(viewer)
				.addDropAction(
						new TargetClassSourceClassValidator(
								InformationModelProject.class,
								FunctionblockModelProject.class),
						new ModelProjectDropAction(FunctionblockModelProject.class))
				.addDropAction(
						new TargetClassModelTypeValidator(
								InformationModelProject.class, ModelType.Functionblock),
						new RepositoryResourceDropAction())
				.addDropAction(
						new ModelTypeValidator(ModelType.InformationModel),
						new CreateProjectDropAction());
	}
}
