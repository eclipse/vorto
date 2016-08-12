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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.vorto.codegen.api.IGeneratorLookup;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.core.ui.MessageDisplayFactory;

public class GeneratorLookupLocal implements IGeneratorLookup {
	
	private static final String GENERATOR_ID = IVortoCodeGenerator.GENERATOR_ID;
	private static final String CLASS = "class";
	
	private static List<IGeneratorResolver> resolvers = new ArrayList<>();
	
	static {
		resolvers.add(new GeneratorExtensionIdResolver());
		resolvers.add(new GeneratorServiceKeyResolver());
	}

	@Override
	public IVortoCodeGenerator lookupByKey(String key) {
		
		for (IGeneratorResolver resolver : resolvers) {
			IVortoCodeGenerator generator = resolver.resolve(key);
			if (generator != null) {
				return generator;
			}
		}
		return null;
	}
	
	private interface IGeneratorResolver {
		IVortoCodeGenerator resolve(String key);
	}
	
	static class GeneratorExtensionIdResolver implements IGeneratorResolver {

		@Override
		public IVortoCodeGenerator resolve(String key) {
			IConfigurationElement[] configurationElements = ConfigurationElementLookup.getDefault().getSelectedConfigurationElementFor(GENERATOR_ID, key);
			for (IConfigurationElement configElement : configurationElements) {
				try {
					final Object codeGenerator = configElement.createExecutableExtension(CLASS);
					if (codeGenerator instanceof IVortoCodeGenerator) {
						return (IVortoCodeGenerator)codeGenerator;
					}
				} catch (CoreException e) {
					MessageDisplayFactory.getMessageDisplay().displayError(e);
				}
			}
			
			return null;
		}
	}
	
	static class GeneratorServiceKeyResolver implements IGeneratorResolver {

		@Override
		public IVortoCodeGenerator resolve(String key) {
			IConfigurationElement[] configurationElements = ConfigurationElementLookup.getDefault().getAllConfigurationElementFor(GENERATOR_ID);
			for (IConfigurationElement configElement : configurationElements) {
				try {
					final Object codeGenerator = configElement.createExecutableExtension(CLASS);
					if (codeGenerator instanceof IVortoCodeGenerator && ((IVortoCodeGenerator)codeGenerator).getServiceKey().equals(key)) {
						return (IVortoCodeGenerator)codeGenerator;
					}
				} catch (CoreException e) {
					MessageDisplayFactory.getMessageDisplay().displayError(e);
				}
			}
			
			return null;
		}
		
	}
}
