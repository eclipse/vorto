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
package org.eclipse.vorto.core.internal.model;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.vorto.core.parser.IModelParser;

public class ModelParserFactory {
	private static ModelParserFactory singleton = null;

	private ModelParserFactory() {
	}

	public static ModelParserFactory getInstance() {
		if (singleton == null) {
			singleton = new ModelParserFactory();
		}
		return singleton;
	}

	public IModelParser getModelParser() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint(IModelParser.EXTENSIONPOINT_ID);
		IExtension[] modelParserExtensions = extensionPoint.getExtensions();
		if (modelParserExtensions.length == 0) {
			throw new RuntimeException("No Vorto Model Parser found!");
		} else if (modelParserExtensions.length > 1) {
			throw new RuntimeException("More than 1 Vorto Model Parser found. Cannot decide which one to bind. ");
		}
		try {
			return (IModelParser) modelParserExtensions[0].getConfigurationElements()[0]
					.createExecutableExtension("class");
		} catch (Exception e) {
			throw new RuntimeException("Problem binding Vorto Model Parser", e);
		}
	}
}
