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
package org.eclipse.vorto.codegen.webui.templates.resources

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext

class ApplicationConfigTemplate implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		'''application.yml'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		spring:
		  jackson:
		    serialization:
		      write-dates-as-timestamps: false
		      
		server:
		  port: 8080
		  contextPath: /
		«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
		bosch:
		  things:
		    alias: CR
		    alias.password:
		    endpointUrl : https://things.apps.bosch-iot-cloud.com
		    wsEndpointUrl : wss://events.apps.bosch-iot-cloud.com
		    apiToken: 
		    keystoreLocation : /secure/CRClient.jks
		    trustStoreLocation : /secure/bosch-iot-cloud.jks
		    trustStorePassword : jks
		    solutionid: 
		    keystore:
		      password: 
		«ENDIF»
		http:
		  proxyUser: 
		  proxyPassword:
		  proxyHost: 
		  proxyPort: 8080
		  
		google:
		  oauth2:
		    client:
		      clientId: 
		      clientSecret: 
		      accessTokenUri: https://www.googleapis.com/oauth2/v4/token
		      userAuthorizationUri: https://accounts.google.com/o/oauth2/v2/auth
		      clientAuthenticationScheme: form
		      scope:
		        - openid
		        - email
		        - profile
		    resource:
		      userInfoUri: https://www.googleapis.com/oauth2/v3/userinfo
		      preferTokenInfo: true
		'''
	}
	
}