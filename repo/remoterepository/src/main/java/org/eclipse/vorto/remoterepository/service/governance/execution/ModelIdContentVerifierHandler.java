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


package org.eclipse.vorto.remoterepository.service.governance.execution;

import org.eclipse.vorto.remoterepository.model.ModelAuthor;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.service.converter.IModelConverterService;
import org.eclipse.vorto.remoterepository.service.governance.IModelGovernanceHandler;
import org.eclipse.vorto.remoterepository.service.governance.exception.MalModelIdentifierContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component 
@Order(value=1)
public class ModelIdContentVerifierHandler implements IModelGovernanceHandler {

	@Autowired
	IModelConverterService modelConverterService;
	
	@Override
	public void handleModel(ModelContent modelContent, ModelAuthor author) {
		
		ModelId modelId0 = modelContent.getModelId();
		ModelView modelView = modelConverterService.convert(modelContent);
		ModelId modelId1 = modelView.getModelId();
		if(!modelId0.equals(modelId1)){
			throw new MalModelIdentifierContentException();
		}
	}

}
