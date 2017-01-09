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
package org.eclipse.vorto.codegen.ui.handler;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IGeneratorLookup;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.ui.progressmonitor.VortoCodeGenProgressMonitorFactory;
import org.eclipse.vorto.codegen.ui.progressmonitor.VortoProgressMonitor;
import org.eclipse.vorto.codegen.ui.utils.PlatformUtils;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.ui.MessageDisplayFactory;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.core.ui.model.ModelProjectFactory;

/**
 * 
 * Iterates over all code generator extensions and executes them as a eclipse
 * job
 * 
 */
public class CodeGeneratorInvocationHandler extends AbstractHandler {

	private static final IGeneratorLookup lookupService = new GeneratorLookupLocal();

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final String generatorServiceKey = event.getParameter("org.eclipse.vorto.codegen.generator.commandParameter");
		if (evaluate(generatorServiceKey)) {
			PlatformUtils.switchPerspective(PlatformUtils.JAVA_PERSPECTIVE);
		}

		return null;
	}

	private boolean evaluate(String generatorServiceKey) {

		IModelElement selectedElement = ModelProjectFactory.getInstance().getModelElementFromSelection();
		if (selectedElement == null) {
			MessageDisplayFactory.getMessageDisplay()
					.displayWarning("Model was not properly selected. Please try again.");
			return false;
		}

		if (!hasNoErrors(selectedElement)) {
			MessageDisplayFactory.getMessageDisplay().displayError("Model has errors. Cannot generate.");
			return false;
		}

		InformationModel informationModel = getInformationModel(selectedElement.getModel());

		try {
			IVortoCodeGenerator generator = lookupService.lookupByKey(generatorServiceKey);
			IGenerationResult result = generator.generate(informationModel,
					createInvocationContext(selectedElement.getProject(), generator.getServiceKey()),
					VortoCodeGenProgressMonitorFactory.getCodeGenStatusReporter());
			CodeGenerationHelper.createEclipseProject(ModelIdFactory.newInstance(informationModel),
					generator.getServiceKey(), result);
			VortoProgressMonitor.getDefault().display();
		} catch (Exception e) {
			MessageDisplayFactory.getMessageDisplay().displayError(e);
		}

		return true;
	}

	private InformationModel getInformationModel(Model model) {
		if (model instanceof InformationModel) {
			return (InformationModel) model;
		} else if (model instanceof FunctionblockModel) {
			return Utils.wrapFunctionBlock((FunctionblockModel) model);
		}
		throw new IllegalArgumentException("Cannot generate from selected model");
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
				if (property.getType() != null && property.getType().eResource() != null && 
						property.getType().eResource().getErrors() != null && 
						property.getType().eResource().getErrors().size() > 0) {
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
		}
		return Collections.emptyList();
	}

	private InvocationContext createInvocationContext(IModelProject project, String targetPlatform) {
		return new InvocationContext(project.getMapping(targetPlatform), lookupService,
				Collections.<String, String>emptyMap()); // TODO: read
															// configuration
															// properties from
															// Eclipse
	}
}
