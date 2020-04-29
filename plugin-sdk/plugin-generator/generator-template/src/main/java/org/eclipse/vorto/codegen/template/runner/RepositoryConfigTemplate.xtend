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
package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class RepositoryConfigTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'config.json'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner/docker'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		{
"generators":{
"vorto":{
	"serverUrl":"http://repository:8080/infomodelrepository"
},
"server":{
	"serviceUrl": "http://generators:8081/example-generators",
	"contextPath": "/example-generators",
	"port": 8081,
	"host": "generators",
	"config":{
    "generatorUser": "generator",
    "generatorPassword": 123
  }
}
},
"repository": {
"github_clientid":"your_client_id",
"github_clientSecret":"your_github_secret",
"server": {
  "port": 8080,
  "contextPath": "/infomodelrepository",
  "admin": "your_github_user",
  "config":{
    "authenticatedSearchMode": false,
    "generatorUser": "generator",
    "generatorPassword": 123
  }
},
"eidp_clientid": " ",
"eidp_secret": " ",
"https":{
	"proxyHost": "your_proxy_server",
	"proxyPort": 8080,
	"proxyUser": "your_proxy_user",
	"proxyPassword": "your_password",
	"nonProxyHosts": "localhost|generators|generators-example"
},
"http":{
	"proxyHost": "your_proxy_server",
	"proxyPort": 8080,
	"proxyUser": "your_proxy_user",
	"proxyPassword": "your_password",
	"nonProxyHosts": "localhost|generators|generators-example"
},
"spring":{
	"profiles":"local",
	"datasource":{
		"name": "vortoDB",
		"driverClassName":"org.h2.Driver",
		"initialize": true,
		"url":"jdbc:h2:file:./vortoDB;DB_CLOSE_DELAY=-1"
	},
	"jpa":{
		"hibernate":{
			"ddl-auto":"update"
		}
	}
}
}
}
		'''
	}
	
}
