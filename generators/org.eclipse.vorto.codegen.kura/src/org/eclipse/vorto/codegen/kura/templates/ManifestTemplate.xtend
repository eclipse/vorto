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
		'''«Utils.getBasePath(context)»/META-INF'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
Manifest-Version: 1.0
Bundle-ManifestVersion: 2
Bundle-Name: «element.name»
Bundle-SymbolicName: «Utils.getJavaPackage(element)»
Bundle-Version: 1.0.0.qualifier
Bundle-Vendor: Eclipse Vorto
Bundle-RequiredExecutionEnvironment: JavaSE-1.8
Service-Component: OSGI-INF/*.xml
Bundle-ActivationPolicy: lazy
«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
Bundle-ClassPath: lib/async-http-client-2.0.0.jar,
 lib/cr-integration-api-3.3.0.jar,
 lib/cr-integration-client-2.4.1.jar,
 lib/cr-integration-client-osgi-2.4.1.jar,
 lib/cr-json-1.6.0.jar,
 lib/cr-model-3.3.0.jar,
 lib/javassist-3.20.0-GA.jar,
 lib/netty-buffer-4.0.36.Final.jar,
 lib/netty-codec-4.0.36.Final.jar,
 lib/netty-codec-dns-2.0.0.jar,
 lib/netty-codec-http-4.0.36.Final.jar,
 lib/netty-common-4.0.36.Final.jar,
 lib/netty-handler-4.0.36.Final.jar,
 lib/netty-reactive-streams-1.0.4.jar,
 lib/netty-resolver-2.0.0.jar,
 lib/netty-resolver-dns-2.0.0.jar,
 lib/netty-transport-4.0.36.Final.jar,
 lib/reactive-streams-1.0.0.jar,
 lib/reactor-bus-2.0.7.RELEASE.jar,
 lib/reactor-core-2.0.7.RELEASE.jar,
 lib/stomp-client-2.4.1.jar,
 lib/stomp-common-2.4.1.jar,
 lib/things-model-2.4.1.jar,
 secret/,
 .
«ELSE»«IF context.configurationProperties.getOrDefault("boschhub","false").equalsIgnoreCase("true")»
Bundle-ClassPath: lib/org.eclipse.paho.client.mqttv3-1.0.2.jar,
 lib/gson-2.8.1.jar,
 .
«ENDIF»
«ENDIF»
Import-Package: org.slf4j;version="1.7.21",
 org.eclipse.kura;version="1.3.0",
 org.eclipse.kura.configuration;version="1.1.2",
«IF context.configurationProperties.getOrDefault("bluetooth","false").equalsIgnoreCase("true")» org.eclipse.kura.bluetooth;version="1.4.0",
 org.eclipse.kura.bluetooth.listener;version="1.0.1",
«ENDIF»
«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")» org.osgi.service.component,
 com.eclipsesource.json;version="0.9.4",
 javax.net.ssl,
 javax.security.cert
«ELSE»
«IF context.configurationProperties.getOrDefault("boschhub","false").equalsIgnoreCase("true")» javax.net,
 javax.net.ssl,
 org.osgi.service.component
«ELSE» org.osgi.service.component,
 org.eclipse.kura.cloud;version="1.1.0",
 org.eclipse.kura.message;version="1.1.1"
«ENDIF»
«ENDIF»
'''
	}	
}