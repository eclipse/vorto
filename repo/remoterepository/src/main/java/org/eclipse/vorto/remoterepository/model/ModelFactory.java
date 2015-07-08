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
package org.eclipse.vorto.remoterepository.model;

/**
 * Convenience factory for generating the various models that we have
 * 
 * @author
 *
 */
public class ModelFactory {
	public static ModelId newModelId(ModelType type, String namespace,
			String version, String name) {
		return new ModelId(type, namespace, version, name);
	}

	public static ModelId newFunctionBlockId(String namespace, String version,
			String name) {
		return new ModelId(ModelType.FUNCTIONBLOCK, namespace, version, name);
	}

	public static ModelId newInformationModelId(String namespace,
			String version, String name) {
		return new ModelId(ModelType.INFORMATIONMODEL, namespace, version, name);
	}

	public static ModelId newDataTypeId(String namespace, String version,
			String name) {
		return new ModelId(ModelType.DATATYPE, namespace, version, name);
	}

	public static ModelView newModelView(ModelId modelId, String description) {
		return new ModelView(modelId, description);
	}

	public static ModelView newFunctionBlockView(String namespace,
			String version, String name, String description) {
		return newModelView(newFunctionBlockId(namespace, version, name),
				description);
	}

	public static ModelView newInformationModelView(String namespace,
			String version, String name, String description) {
		return newModelView(newInformationModelId(namespace, version, name),
				description);
	}

	public static ModelView newDataTypeView(String namespace, String version,
			String name, String description) {
		return newModelView(newDataTypeId(namespace, version, name),
				description);
	}

}
