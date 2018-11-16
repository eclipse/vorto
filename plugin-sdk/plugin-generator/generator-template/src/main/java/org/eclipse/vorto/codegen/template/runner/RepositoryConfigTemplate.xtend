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
	"profiles":"local",
	"serviceUrl": "http://generators:8081/example-generators",
	"contextPath": "/example-generators",
	"port": 8081,
	"host": "generators"
}
},
"generators-example": {
"vorto":{
	"serverUrl":"http://repository:8080/infomodelrepository"
},
"server":{
	"profiles":"local",
	"serviceUrl": "http://generators-example:8082/example-generators",
	"contextPath": "/example-generators",
	"port": 8082,
	"host": "generators-example"
}
},
"repository": {
"github_clientid":"your_client_id",
"github_secret":"your_github_secret",
"server": {
  "admin": "your_github_user"
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
