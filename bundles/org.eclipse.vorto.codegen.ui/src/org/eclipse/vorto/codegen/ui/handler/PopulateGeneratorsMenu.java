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
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.ui.Activator;
import org.osgi.framework.Bundle;

public class PopulateGeneratorsMenu extends CompoundContributionItem {

	private static final String CLASS = "class";
	private static final String MENU_LABEL = "menuLabel";

	public PopulateGeneratorsMenu() {
	}

	public PopulateGeneratorsMenu(String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		List<CommandContributionItem> contributionItems = new ArrayList<CommandContributionItem>();
		List<IConfigurationElement> registeredGenerators = getAllRegisteredGeneratorNames();
		for (IConfigurationElement aGenerator : registeredGenerators) {
			CommandContributionItem commandForGenerator = constructCommandForGenerator(aGenerator);
			if (commandForGenerator != null) {
				contributionItems.add(commandForGenerator);
			}
		}
		
		return contributionItems
				.toArray(new IContributionItem[contributionItems.size()]);
	}

	private CommandContributionItem constructCommandForGenerator(
			IConfigurationElement aGenerator) {
		return constructCommandForGenerator(populateExtensionMetaData(aGenerator));
	}

	@SuppressWarnings("unchecked")
	private CommandContributionItem constructCommandForGenerator(
			ExtensionMetaData extensionMetaData) {
		String extensionIdentifier = extensionMetaData.getExtensionIdentifier();
		if (StringUtils.isNotEmpty(extensionIdentifier)) {
			CommandContributionItemParameter generatorCommand = new CommandContributionItemParameter(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow(),
					"org.eclipse.vorto.codegen.dynamic.menu.contribution.item.id",
					"org.eclipse.vorto.codegen.generator.command.id", SWT.NONE);
			CommandContributionItem generatorContributionItem;
			generatorCommand.label = extensionMetaData.getMenuLabel();
			String iconPath = extensionMetaData.getIconPath();
			Bundle bundle = Platform.getBundle(extensionMetaData
					.getGeneratorPluginId());
			if (iconPath == null) {
				// Load the defaults
				iconPath = "icons/generate.gif";
				bundle = Platform.getBundle(Activator.PLUGIN_ID);
			}
			IPath path = new Path(iconPath);
			generatorCommand.icon = ImageDescriptor.createFromURL(FileLocator
					.find(bundle, path, null));
			generatorCommand.parameters = new HashMap<String, String>();
			generatorCommand.parameters.put(
					"org.eclipse.vorto.codegen.generator.commandParameter",
					extensionIdentifier);
			generatorContributionItem = new CommandContributionItem(
					generatorCommand);
			return generatorContributionItem;
		}
		return null;
	}

	private ExtensionMetaData populateExtensionMetaData(
			IConfigurationElement confiurationElement) {
		ExtensionMetaData extensionMetaData = new ExtensionMetaData();
		extensionMetaData.setGeneratorPluginId(confiurationElement
				.getContributor().getName());
		extensionMetaData.setClassName(confiurationElement.getAttribute(CLASS));
		extensionMetaData.setExtensionIdentifier(confiurationElement
				.getDeclaringExtension().getUniqueIdentifier());
		extensionMetaData.setMenuLabel(confiurationElement
				.getAttribute(MENU_LABEL));
		extensionMetaData.setIconPath(confiurationElement.getAttribute("icon"));
		return extensionMetaData;
	}

	private List<IConfigurationElement> getAllRegisteredGeneratorNames() {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		List<IConfigurationElement> registersGenerators = new ArrayList<IConfigurationElement>();
		final IConfigurationElement[] generators = extensionRegistry
				.getConfigurationElementsFor(IVortoCodeGenerator.GENERATOR_ID);
		for (IConfigurationElement e : generators) {
			registersGenerators.add(e);
		}
		return registersGenerators;
	}
}
