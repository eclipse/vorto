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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class ConfigurationElementLookup {

	private static final String BUNDLE_NAME = "Bundle-Name";
	private IExtensionRegistry EXTENSION_REGISTRY = Platform
			.getExtensionRegistry();
	private static ConfigurationElementLookup instance;

	private ConfigurationElementLookup() {

	}

	public static ConfigurationElementLookup getDefault() {
		if (instance == null)
			instance = new ConfigurationElementLookup();
		return instance;
	}

	public IConfigurationElement[] getAllConfigurationElementFor(
			String extensionPtId) {
		return EXTENSION_REGISTRY.getConfigurationElementsFor(extensionPtId);
	}

	public IConfigurationElement[] getSelectedConfigurationElementFor(
			String extensionPtId, String id) {
		IExtension extension = EXTENSION_REGISTRY.getExtension(extensionPtId,
				id);
		if (extension != null) {
			return extension.getConfigurationElements();
		} else {
			return new IConfigurationElement[0];
		}
	}
	
	public void setExtensionRegistry(IExtensionRegistry reg) {
		this.EXTENSION_REGISTRY = reg;
	}

}
