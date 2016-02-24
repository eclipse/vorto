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
package org.eclipse.vorto.codegen.ui.utils;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;


public class WizardUtil {
	public static IConfigurationElement getWizardConfigurationElement(
			String wizardId) {

		IExtensionRegistry er = Platform.getExtensionRegistry();
		IConfigurationElement[] elems = er
				.getConfigurationElementsFor("org.eclipse.ui.newWizards");

		for (IConfigurationElement configurationElement : elems) {
			IConfigurationElement config = getConfigFor(configurationElement,
					wizardId);
			if (config != null) {
				return config;
			}
		}
		return null;
	}

	private static IConfigurationElement getConfigFor(
			IConfigurationElement elem, String wizardId) {

		for (String attribute : elem.getAttributeNames()) {
			String value = elem.getAttribute(attribute);
			if ("id".equals(attribute) && wizardId.equals(value)) {
				return elem;
			}

		}

		IConfigurationElement[] elemsc = elem.getChildren();
		for (IConfigurationElement childElem : elemsc) {
			IConfigurationElement configurationElement = getConfigFor(
					childElem, wizardId);
			if (configurationElement != null) {
				return configurationElement;
			}
		}
		return null;
	}
}
