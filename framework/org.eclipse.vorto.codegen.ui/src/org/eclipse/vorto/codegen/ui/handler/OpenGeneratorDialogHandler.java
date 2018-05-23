package org.eclipse.vorto.codegen.ui.handler;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.ui.PlatformUI;
import org.eclipse.vorto.codegen.api.IGeneratorLookup;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.ui.MessageDisplayFactory;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.core.ui.model.ModelProjectFactory;

public class OpenGeneratorDialogHandler extends AbstractHandler {

	private static final IGeneratorLookup lookupService = new GeneratorLookupLocal();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final String generatorServiceKey = event.getParameter("org.eclipse.vorto.codegen.generator.commandParameter");
		
		IModelElement selectedElement = ModelProjectFactory.getInstance().getModelElementFromSelection();
		if (selectedElement == null) {
			selectedElement = ModelProjectFactory.getInstance().getSelectedModel();
		}
		if (selectedElement == null) {
			MessageDisplayFactory.getMessageDisplay()
					.displayWarning("Model was not properly selected. Please try again.");
			return null;
		}

		if (!hasNoErrors(selectedElement)) {
			MessageDisplayFactory.getMessageDisplay().displayError("Model has errors. Please correct the model and try again.");
			return null;
		}

		GeneratorDialog dialog = new GeneratorDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), lookupService.lookupByKey(generatorServiceKey),lookupService);
		dialog.create();
		return dialog.open();
	}
	
	private boolean hasNoErrors(IModelElement selectedElement) {
		return selectedElement.getDiagnostics().size() <= 0 && noLinkingErrors(selectedElement);
	}

	// check if model has no syntax errors
	private boolean noLinkingErrors(IModelElement modelElement) {
		Model model = modelElement.getModel();

		if (model instanceof InformationModel) {
			InformationModel infoModel = (InformationModel) model;
			for (FunctionblockProperty property : infoModel.getProperties()) {
				// for syntax and parsing errors
				if (property.getType() != null && property.getType().eResource() != null
						&& property.getType().eResource().getErrors() != null
						&& property.getType().eResource().getErrors().size() > 0) {
					return false;
				}

				// for linking errors
				if (getLinkingErrors(property.getType()).size() > 0) {
					return false;
				}
			}
		}
		return true;
	}

	private Collection<Diagnostic> getLinkingErrors(EObject model) {
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(model);
		switch (diagnostic.getSeverity()) {
		case Diagnostic.ERROR:
			return diagnostic.getChildren();
		default:
			return Collections.emptyList();
		}
	}

}
