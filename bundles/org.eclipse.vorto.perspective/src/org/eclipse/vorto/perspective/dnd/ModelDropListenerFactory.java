package org.eclipse.vorto.perspective.dnd;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.vorto.core.model.DatatypeModelProject;
import org.eclipse.vorto.core.model.FunctionblockModelProject;
import org.eclipse.vorto.core.model.InformationModelProject;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.perspective.dnd.dropaction.CreateProjectDropAction;
import org.eclipse.vorto.perspective.dnd.dropaction.ModelProjectDropAction;
import org.eclipse.vorto.perspective.dnd.dropaction.RepositoryResourceDropAction;
import org.eclipse.vorto.perspective.dnd.dropvalidator.ModelTypeValidator;
import org.eclipse.vorto.perspective.dnd.dropvalidator.TargetClassModelTypeValidator;
import org.eclipse.vorto.perspective.dnd.dropvalidator.TargetClassSourceClassValidator;

public class ModelDropListenerFactory {

	public static DropTargetListener datatypeViewPartDropListener(Viewer viewer) {
		return new ModelDropListener(viewer)
				.addDropAction(
						new TargetClassSourceClassValidator(
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
