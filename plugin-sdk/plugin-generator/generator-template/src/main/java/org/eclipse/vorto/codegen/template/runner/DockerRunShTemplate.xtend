package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class PomTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'run.sh'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner/docker'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		#!/bin/bash
SPRING_APPLICATION_JSON=$(jq .repository /code/config/config.json | sed $'s/\r//' | tr -d '\n');
export SPRING_APPLICATION_JSON
if [[ -z ${USE_PROXY} ]]; then
	java -jar infomodelrepository.jar;
else
	echo "USING PROXY"
	HTTP_NON_PROXY_HOST=$(jq .repository.http.nonProxyHosts < /code/config/config.json)
	export HTTP_NON_PROXY_HOST
	HTTP_PROXY_HOST=$(jq .repository.http.proxyHost < /code/config/config.json)
	export HTTP_PROXY_HOST
	HTTP_PROXY_PORT=$(jq .repository.http.proxyPort < /code/config/config.json)
	export HTTP_PROXY_PORT
	HTTP_PROXY_USER=$(jq .repository.http.proxyUser < /code/config/config.json)
	export HTTP_PROXY_USER
	HTTP_PROXY_PASSWORD=$(jq .repository.http.proxyPassword < /code/config/config.json)
	export HTTP_PROXY_PASSWORD
	HTTPS_NON_PROXY_HOST=$(jq .repository.https.nonProxyHosts < /code/config/config.json)
	export HTTPS_NON_PROXY_HOST
	HTTPS_PROXY_HOST=$(jq .repository.https.proxyHost < /code/config/config.json)
	export HTTPS_PROXY_HOST
	HTTPS_PROXY_PORT=$(jq .repository.https.proxyPort < /code/config/config.json)
	export HTTPS_PROXY_PORT
	HTTPS_PROXY_USER=$(jq .repository.https.proxyUser < /code/config/config.json)
	export HTTPS_PROXY_USER
	HTTPS_PROXY_PASSWORD=$(jq .repository.https.proxyPassword < /code/config/config.json)
	export HTTPS_PROXY_PASSWORD
	cmd="java -Dhttps.nonProxyHosts=$HTTPS_NON_PROXY_HOST -Dhttps.proxyHost=$HTTPS_PROXY_HOST -Dhttps.proxyPort=$HTTPS_PROXY_PORT -Dhttps.proxyUser=$HTTPS_PROXY_USER -Dhttps.proxyPassword=$HTTPS_PROXY_PASSWORD -Dhttp.nonProxyHosts=$HTTP_NON_PROXY_HOST -Dhttp.proxyHost=$HTTP_PROXY_HOST -Dhttp.proxyPort=$HTTP_PROXY_PORT -Dhttp.proxyUser=$HTTP_PROXY_USER -Dhttp.proxyPassword=$HTTP_PROXY_PASSWORD -jar /code/infomodelrepository.jar"
	eval $cmd
fi
		'''
	}
	
}