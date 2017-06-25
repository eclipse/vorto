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
package org.eclipse.vorto.codegen.kura.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class ManifestTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''MANIFEST.MF'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.basePath»/META-INF'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		Manifest-Version: 1.0
		Bundle-ManifestVersion: 2
		Bundle-Name: «element.name»
		Bundle-SymbolicName: «Utils.javaPackage»
		Bundle-Version: 1.0.0.qualifier
		Bundle-Vendor: Eclipse Vorto
		Bundle-RequiredExecutionEnvironment: JavaSE-1.8
		Service-Component: OSGI-INF/*.xml
		«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
		Bundle-ClassPath: secret/,
		 .
		«ENDIF»
		Export-Package: «Utils.javaPackage»
		Import-Package: org.slf4j;version="1.7.21",
		 «IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
		 com.bosch.cr.integration;version="3.3.0",
		 com.bosch.cr.integration.client;version="2.4.1",
		 com.bosch.cr.integration.client.configuration;version="2.4.1",
		 com.bosch.cr.integration.things;version="3.3.0",
		 com.bosch.cr.json;version="1.8.1",
		 com.bosch.cr.model.json;version="3.3.0",
		 com.bosch.cr.model.things;version="3.3.0",
		 «ELSE»
		 org.eclipse.kura.cloud;version="1.1.0",
		 org.eclipse.kura.message;version="1.1.1",
		 «ENDIF»
		 «IF context.configurationProperties.getOrDefault("bluetooth","false").equalsIgnoreCase("true")»
		 org.eclipse.kura.bluetooth;version="1.4.0",
		 org.eclipse.kura.bluetooth.listener;version="1.0.1",
		 «ENDIF»
		 org.eclipse.kura;version="1.3.0",
		 org.eclipse.kura.configuration;version="1.1.2",
		 org.osgi.service.component
		'''
	}
	
}