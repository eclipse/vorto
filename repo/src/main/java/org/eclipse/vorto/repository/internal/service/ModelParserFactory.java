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
package org.eclipse.vorto.repository.internal.service;

import org.eclipse.vorto.repository.internal.service.emf.DatatypeModelParser;
import org.eclipse.vorto.repository.internal.service.emf.FunctionblockModelParser;
import org.eclipse.vorto.repository.internal.service.emf.InformationModelParser;
import org.eclipse.vorto.repository.internal.service.emf.MappingModelParser;
import org.eclipse.vorto.repository.model.ModelType;

public class ModelParserFactory {
	
	public static IModelParser getParser(String fileName) {
		if (fileName.endsWith(ModelType.Datatype.getExtension())) {
			return new DatatypeModelParser(fileName);
		} else if (fileName.endsWith(ModelType.Functionblock.getExtension())) {
			return new FunctionblockModelParser(fileName);
		} else if (fileName.endsWith(ModelType.InformationModel.getExtension())) {
			return new InformationModelParser(fileName);
		} else if (fileName.endsWith(ModelType.Mapping.getExtension())) {
			return new MappingModelParser(fileName);
		}else {
			throw new UnsupportedOperationException("File cannot be parsed, because it is not supported");
		}
	}
}
