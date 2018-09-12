#!/bin/bash
SPRING_APPLICATION_JSON=$(jq .repository /config/config.json | sed $'s/\r//' | tr -d '\n');
export SPRING_APPLICATION_JSON
if [[ -z ${USE_PROXY} ]]; then 
	java -jar infomodelrepository.jar;
else 
	HTTP_NON_PROXY_HOST=$(jq .http.nonProxyHosts < /code/config/config.json)
	HTTP_PROXY_HOST=$(jq .http.proxyHost < /code/config/config.json)
	HTTP_PROXY_PORT=$(jq .http.proxyPort < /code/config/config.json)
	HTTP_PROXY_USER=$(jq .http.proxyUser < /code/config/config.json)
	HTTP_PROXY_PASSWORD=$(jq .http.proxyPassword < /code/config/config.json)
	HTTPS_NON_PROXY_HOST=$(jq .https.nonProxyHosts < /code/config/config.json)
	HTTPS_PROXY_HOST=$(jq .https.proxyHost < /code/config/config.json)
	HTTPS_PROXY_PORT=$(jq .https.proxyPort < /code/config/config.json)
	HTTPS_PROXY_USER=$(jq .https.proxyUser < /code/config/config.json)
	HTTPS_PROXY_PASSWORD=$(jq .https.proxyPassword < /code/config/config.json)
	java -Dhttps.nonProxyHosts="${HTTPS_NON_PROXY_HOST}" -Dhttps.proxyHost="${HTTPS_PROXY_HOST}" -Dhttps.proxyPort="${HTTPS_PROXY_PORT}" -Dhttps.proxyUser="${HTTPS_PROXY_USER}" -Dhttps.proxyPassword="${HTTPS_PROXY_PASSWORD}" -Dhttp.nonProxyHosts="${HTTP_NON_PROXY_HOST}" -Dhttp.proxyHost="${HTTP_PROXY_HOST}" -Dhttp.proxyPort="${HTTP_PROXY_PORT}" -Dhttp.proxyUser="${HTTP_PROXY_USER}" -Dhttp.proxyPassword="${HTTP_PROXY_PASSWORD}" -jar /code/infomodelrepository.jar;
fi

