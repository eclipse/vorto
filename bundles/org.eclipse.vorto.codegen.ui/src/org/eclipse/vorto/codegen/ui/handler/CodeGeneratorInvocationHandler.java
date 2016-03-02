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
package org.eclipse.vorto.codegen.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.vorto.codegen.api.DefaultMappingContext;
import org.eclipse.vorto.codegen.api.IMappingContext;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.codegen.ui.utils.PlatformUtils;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;

/**
 * 
 * Iterates over all code generator extensions and executes them as a eclipse
 * job
 * 
 */
public class CodeGeneratorInvocationHandler extends AbstractHandler {

	private static final String GENERATOR_ID = IVortoCodeGenerator.GENERATOR_ID;
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final String generatorIdentifier = event.getParameter("org.eclipse.vorto.codegen.generator.commandParameter");
		evaluate(generatorIdentifier);
		PlatformUtils.switchPerspective(PlatformUtils.JAVA_PERSPECTIVE);
		return null;
	}

	private void evaluate(String generatorName) {
		final IModelProject selectedProject = ModelProjectServiceFactory.getDefault().getProjectFromSelection();

		final InformationModel informationModel = (InformationModel) selectedProject.getModel();
		
		final String targetPlatform = ConfigurationElementLookup.getDefault().getExtensionSimpleIdentifier(GENERATOR_ID, generatorName);
		
		final IConfigurationElement[] configElements = getUserSelectedGenerators(generatorName);
		for (IConfigurationElement e : configElements) {
			try {
				final Object codeGenerator = e.createExecutableExtension("class");

				if (!(codeGenerator instanceof IVortoCodeGenerator)) {
					continue; // interested only in code generators
				}

				IVortoCodeGenerator informationModelCodeGenerator = (IVortoCodeGenerator) codeGenerator;

				CodeGeneratorTaskExecutor.execute(informationModel, informationModelCodeGenerator, createMappingContext(selectedProject, targetPlatform));

			} catch (Exception e1) {
				MessageDisplayFactory.getMessageDisplay().displayError(e1);
				throw new RuntimeException("Something went wrong during code generation", e1);
			}
		}
	}

	private IMappingContext createMappingContext(IModelProject project, String targetPlatform) {
		DefaultMappingContext mappingContext = new DefaultMappingContext();
		
		for(MappingModel mappingModel : project.getMapping(targetPlatform)) {
			mappingContext.addMappingModel(mappingModel);
		}
		
		return mappingContext;
	}
	
	private IConfigurationElement[] getUserSelectedGenerators(String generatorIdentifier) {

		IConfigurationElement[] configurationElements;
		ConfigurationElementLookup elementLookup = ConfigurationElementLookup.getDefault();
		configurationElements = elementLookup.getSelectedConfigurationElementFor(GENERATOR_ID, generatorIdentifier);
		
		return configurationElements;
	}

}
