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


package org.eclipse.vorto.remoterepository.service.governance;

import java.util.List;

import org.eclipse.vorto.remoterepository.model.ModelAuthor;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultGovernanceService implements IGovernanceService {
	
	@Autowired(required = false)
	List<IModelGovernanceHandler> buildinHandlers;
		
	public void init(){
		
	}
	
	public void start(ModelContent modelContent, ModelAuthor author){
		for(IModelGovernanceHandler handler:buildinHandlers){
			handler.handleModel(modelContent, author);
		}
	}
}
