/*******************************************************************************
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

package org.eclipse.vorto.core.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;

public class ModelIdFactory {

	public static ModelId newInstance(ModelType modelType, String namespace,
			String version, String name) {
		return new ModelId(modelType, namespace, version, name);
	}

	public static ModelId newInstance(Model model) {
		return new ModelId(getModelType(model), model.getName(),
				model.getNamespace(), model.getVersion());
	}

	private static ModelType getModelType(Model model) {
		if (model instanceof InformationModel) {
			return ModelType.INFORMATIONMODEL;
		} else if (model instanceof FunctionblockModel) {
			return ModelType.FUNCTIONBLOCK;
		} else {
			return ModelType.DATATYPE;
		}
	}

	public static ModelId newInstance(ModelType modelType, Model model) {
		return new ModelId(modelType, model.getName(), model.getNamespace(),
				model.getVersion());
	}

	public static ModelId newInstance(ModelType modelType,
			ModelReference modelReference) {
		String importStr = modelReference.getImportedNamespace();
		int lastDot = importStr.lastIndexOf(".");
		String namespacePart = importStr.substring(0, lastDot);
		String namePart = importStr.substring(lastDot + 1);
		return new ModelId(modelType, namePart, namespacePart,
				modelReference.getVersion());
	}

	public static ModelId newInstance(String serializeStr) {
		String[] tokens = serializeStr.split(",");
		Map<String, String> map = new HashMap<String, String>();
		for (String token : tokens) {
			String[] pair = token.split("=");
			map.put(pair[0], pair[1]);
		}
		ModelType modelType = ModelType.valueOf(map.get("modelType"));
		return new ModelId(modelType, map.get("name"), map.get("namespace"),
				map.get("version"));
	}
}
