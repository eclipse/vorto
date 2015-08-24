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
package org.eclipse.vorto.codegen.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.mapping.IMappingRulesAware;
import org.eclipse.vorto.codegen.internal.mapping.DefaultMappingRules;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.codegen.utils.PlatformUtils;
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

	private static final String GENERATOR_ID = ICodeGenerator.GENERATOR_ID;
	private static final String JAVA_PERSPECTIVE = "org.eclipse.jdt.ui.JavaPerspective";

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final String generatorIdentifier = event
				.getParameter("org.eclipse.vorto.codegen.generator.commandParameter");
		evaluate(generatorIdentifier);
		PlatformUtils.switchPerspective(JAVA_PERSPECTIVE);
		return null;
	}

	@SuppressWarnings("unchecked")
	private void evaluate(String generatorName) {

		final IConfigurationElement[] configElements = getUserSelectedGenerators(generatorName);
		IModelProject selectedProject = ModelProjectServiceFactory.getDefault()
				.getProjectFromSelection();

		InformationModel informationModel = (InformationModel) selectedProject
				.getModel();

		for (IConfigurationElement e : configElements) {
			try {
				final Object codeGenerator = e
						.createExecutableExtension("class");

				if (!(codeGenerator instanceof ICodeGenerator)) {
					continue; // interested only in code generators
				}

				ICodeGenerator<InformationModel> informationModelCodeGenerator = (ICodeGenerator<InformationModel>) codeGenerator;

				/**
				 * read mapping model and inject Mapping Rules into code
				 * generator
				 */
				if (informationModelCodeGenerator instanceof IMappingRulesAware) {
					IMappingRulesAware mappingRulesAwareGenerator = ((IMappingRulesAware) informationModelCodeGenerator);
					MappingModel mappingModel = selectedProject
							.getMapping(mappingRulesAwareGenerator
									.getMappingRulesFileName());
					mappingRulesAwareGenerator
							.setMappingRules(null);

				}

				CodeGeneratorTaskExecutor.execute(informationModel,
						informationModelCodeGenerator);

			} catch (Exception e1) {
				MessageDisplayFactory.getMessageDisplay().displayError(e1);
				throw new RuntimeException(
						"Something went wrong during code generation", e1);
			}
		}
	}

	private IConfigurationElement[] getUserSelectedGenerators(
			String generatorIdentifier) {

		IConfigurationElement[] configurationElements;
		ConfigurationElementLookup elementLookup = ConfigurationElementLookup
				.getDefault();
		if (PopulateGeneratorsMenu.GENERATE_ALL.equals(generatorIdentifier)) {
			configurationElements = elementLookup
					.getAllConfigurationElementFor(GENERATOR_ID);
		} else {
			configurationElements = elementLookup
					.getSelectedConfigurationElementFor(GENERATOR_ID,
							generatorIdentifier);
		}
		return configurationElements;
	}

}
