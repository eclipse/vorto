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
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class EclipseClasspathTemplate implements IFileTemplate<InformationModel> {
		
    override getFileName(InformationModel context) {
        ".classpath"
    }
    
    override getPath(InformationModel context) {
        '''«Utils.getBasePath(context)»'''
    }
    
    override getContent(InformationModel context,InvocationContext invocationContext) {
        '''
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
    <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.8"/>
    <classpathentry kind="con" path="org.eclipse.pde.core.requiredPlugins"/>
    <classpathentry kind="src" path="src"/>
    <classpathentry exported="true" kind="lib" path="secret" sourcepath="secret"/>
	<classpathentry exported="true" kind="lib" path="lib/async-http-client-2.0.0.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/cr-integration-api-3.3.0.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/cr-integration-client-2.4.1.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/cr-integration-client-osgi-2.4.1.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/cr-json-1.6.0.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/cr-model-3.3.0.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/javassist-3.20.0-GA.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-buffer-4.0.36.Final.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-codec-4.0.36.Final.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-codec-dns-2.0.0.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-codec-http-4.0.36.Final.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-common-4.0.36.Final.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-handler-4.0.36.Final.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-reactive-streams-1.0.4.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-resolver-2.0.0.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-resolver-dns-2.0.0.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/netty-transport-4.0.36.Final.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/reactive-streams-1.0.0.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/reactor-bus-2.0.7.RELEASE.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/reactor-core-2.0.7.RELEASE.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/stomp-client-2.4.1.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/stomp-common-2.4.1.jar"/>
	<classpathentry exported="true" kind="lib" path="lib/things-model-2.4.1.jar"/>
    <classpathentry kind="output" path="bin"/>
</classpath>
        '''
    }
	
}
