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
package org.eclipse.vorto.core.api.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * A factory method that returns the implementing services of the Repository
 */
public class ModelRepositoryFactory {
	private static final String CLASS = "class";
	private static final String MODEL_REPO_EXT_PT_ID = "org.eclipse.vorto.core.IModelRepository";
	
	public static IModelRepository getModelRepository() {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(MODEL_REPO_EXT_PT_ID);
		try {
			for (IConfigurationElement e : config) {
				final Object o = e.createExecutableExtension(CLASS);
				if (o instanceof IModelRepository) {
					return (IModelRepository) o;
				}
			}
		} catch (CoreException ex) {
			throw new RuntimeException(ex);
		}
		
		// TODO : Return null or throw exception if cannot find?
		return null;
	}
}
