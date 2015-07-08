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

import org.eclipse.vorto.remoterepository.model.ModelAuthor;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.service.governance.execution.DuplicatedModelIdCheckHandler;
import org.eclipse.vorto.remoterepository.service.governance.execution.ModelIdContentVerifierHandler;

/**
 * A ModelGovernanceHandler is an object that performs governance tasks on
 * {@link ModelContent} in the handleModel method. <br>
 * Handlers are registered in the DefaultGovernanceService as a chain.
 * <p>
 * Implemented by:
 * <li>{@link ModelIdContentVerifierHandler}
 * <li>{@link DuplicatedModelIdCheckHandler}
 * 
 *
 */
public interface IModelGovernanceHandler {
	void handleModel(ModelContent modelContent, ModelAuthor author);
}
