/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.editor.web.datatype

import com.google.inject.Injector
import javax.servlet.annotation.WebServlet
import org.eclipse.xtext.util.DisposableRegistry
import org.eclipse.xtext.web.servlet.HttpServiceContext
import org.eclipse.xtext.web.servlet.XtextServlet

@WebServlet(name = 'Datatype XtextServices', urlPatterns = '/datatype/xtext-service/*')
class DatatypeServlet extends XtextServlet {
	
	DisposableRegistry disposableRegistry
	
	Injector injector; 
		
	override init() {
		super.init()
		injector = new DatatypeWebSetup().createInjectorAndDoEMFRegistration()
		disposableRegistry = injector.getInstance(DisposableRegistry)
	}
	
	override destroy() {
		System.out.println("disposing datatype servlet")
		if (disposableRegistry !== null) {
			disposableRegistry.dispose()
			disposableRegistry = null
		}
		super.destroy()
	}
	
	override getInjector(HttpServiceContext serviceContext) {
		return injector
	}
	
}