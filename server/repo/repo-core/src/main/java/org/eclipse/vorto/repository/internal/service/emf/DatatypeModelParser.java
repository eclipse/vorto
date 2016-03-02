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
package org.eclipse.vorto.repository.internal.service.emf;

import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;
import org.eclipse.vorto.editor.datatype.DatatypeStandaloneSetup;

import com.google.inject.Injector;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class DatatypeModelParser extends AbstractModelParser {
	
	public DatatypeModelParser(String fileName) {
		super(fileName);
		DatatypePackage.eINSTANCE.eClass();
	}		

	@Override
	protected Injector getInjector() {
		return new DatatypeStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
	
}
