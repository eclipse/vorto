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
package org.eclipse.vorto.repository;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.vorto.core.ui.MessageDisplayFactory;
import org.eclipse.vorto.repository.preferences.ConnectionInfoFactory;

public class RestModelRepositoryExtensionFactory implements
		IExecutableExtensionFactory {

	@Override
	public Object create() throws CoreException {	
		RestModelRepository modelRepository = new RestModelRepository(ConnectionInfoFactory.getConnectionInfo());
		modelRepository.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				MessageDisplayFactory.getMessageDisplay().display(
						"Upload Status : " + arg.toString());
			}			
		});
		
		return modelRepository;
	}

}
