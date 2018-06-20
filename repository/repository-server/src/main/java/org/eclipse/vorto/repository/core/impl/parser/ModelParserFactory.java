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
package org.eclipse.vorto.repository.core.impl.parser;

import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.model.ModelType;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelParserFactory {
	
	static {
		DatatypePackage.eINSTANCE.eClass();
		FunctionblockPackage.eINSTANCE.eClass();
		InformationModelPackage.eINSTANCE.eClass();
		MappingPackage.eINSTANCE.eClass();
	}
	

	public static IModelParser getParser(String fileName) {
		if (fileName.endsWith(ModelType.Datatype.getExtension())) {
			return new DatatypeModelParser(fileName);
		} else if (fileName.endsWith(ModelType.Functionblock.getExtension())) {
			return new FunctionblockModelParser(fileName);
		} else if (fileName.endsWith(ModelType.InformationModel.getExtension())) {
			return new InformationModelParser(fileName);
		} else if (fileName.endsWith(ModelType.Mapping.getExtension())) {
			return new MappingModelParser(fileName);
		} else {
			throw new UnsupportedOperationException("File cannot be parsed, because it is not supported");
		}
	}
	
	public static boolean hasParserFor(String fileName) {
		return fileName.endsWith(ModelType.Datatype.getExtension()) || fileName.endsWith(ModelType.Functionblock.getExtension()) 
				|| fileName.endsWith(ModelType.InformationModel.getExtension()) || fileName.endsWith(ModelType.Mapping.getExtension());
	}
}
