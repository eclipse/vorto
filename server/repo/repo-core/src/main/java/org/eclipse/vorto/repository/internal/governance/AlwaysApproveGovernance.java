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
package org.eclipse.vorto.repository.internal.governance;

import org.eclipse.vorto.repository.governance.GovernanceResult;
import org.eclipse.vorto.repository.governance.IGovernance;
import org.eclipse.vorto.repository.governance.IGovernanceCallback;
import org.eclipse.vorto.repository.model.ModelUploadHandle;

/**
 * Simple governance which always invokes the client callback handler immediately for further processing.
 * 
 * @author Alexander Edelmann
 *
 */
public class AlwaysApproveGovernance implements IGovernance {

	@Override
	public void start(ModelUploadHandle modelUpload, IGovernanceCallback callback) {
		callback.processUploadResult(new GovernanceResult(modelUpload, true));
	}

}
