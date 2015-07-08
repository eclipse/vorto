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


public class ExtensionMetaData {

	private String generatorPluginId;
	private String className;
	private String extensionIdentifier;
	private String menuLabel;
	private String iconPath;

	public String getGeneratorPluginId() {
		return generatorPluginId;
	}

	public void setGeneratorPluginId(String generatorPluginId) {
		this.generatorPluginId = generatorPluginId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getExtensionIdentifier() {
		return extensionIdentifier;
	}

	public void setExtensionIdentifier(String extensionIdentifier) {
		this.extensionIdentifier = extensionIdentifier;
	}

	public String getMenuLabel() {
		return menuLabel;
	}

	public void setMenuLabel(String menuLabel) {
		this.menuLabel = menuLabel;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

}
