package org.eclipse.vorto.codegen.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class Environment {
	@Value("${vorto.serverUrl}")
	private String vortoServerUrl;
	
	@Value("${server.serviceUrl}")
	private String applicationServiceUrl;

	public String getVortoRepoUrl() {
		return vortoServerUrl;
	}

	public void setVortoRepoUrl(String vortoServerUrl) {
		this.vortoServerUrl = vortoServerUrl;
	}

	public String getAppServiceUrl() {
		return applicationServiceUrl;
	}

	public void setAppServiceUrl(String applicationServiceUrl) {
		this.applicationServiceUrl = applicationServiceUrl;
	}
}
