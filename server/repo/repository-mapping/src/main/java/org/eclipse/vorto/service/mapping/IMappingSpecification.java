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
package org.eclipse.vorto.service.mapping;

import java.util.Optional;

import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;

public interface IMappingSpecification {

	/**
	 * Information Model of the device
	 * @return
	 */
	Infomodel getInfoModel();
	
	/**
	 * Resolves the referenced function block by the given modelID
	 * @param modelId
	 * @return
	 */
	FunctionblockModel getFunctionBlock(ModelId modelId);
	
	/**
	 * Gets all custom Mapping conversion functions
	 * @return
	 */
	Optional<Functions> getCustomFunctions();
	
	static MappingSpecificationBuilder newBuilder() {
		return new MappingSpecificationBuilder();
	}
}
