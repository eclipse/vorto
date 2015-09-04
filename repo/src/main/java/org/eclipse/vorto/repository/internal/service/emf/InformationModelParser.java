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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

public class InformationModelParser extends AbstractModelParser {
	
	private String fileName;

	public InformationModelParser(String fileName) {
		init();
		this.fileName = fileName;
	}
	
	protected Model doParse(InputStream is) {
		Injector injector = new InformationModelStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		 resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		 Resource resource = resourceSet.createResource(URI.createURI("dummy:/"+fileName));
		 try {
			resource.load(is, resourceSet.getLoadOptions());
		} catch (IOException e) {
			e.printStackTrace();
		}
         		
		return (Model)resource.getContents().get(0);
	}
		
	
	private void init() {
		InformationModelPackage.eINSTANCE.eClass();
	}
}

