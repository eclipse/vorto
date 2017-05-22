package org.eclipse.vorto.codegen.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
@Profile("cloud")
public class EnvironmentCloud extends Environment {

	@Value("${vorto.serverUrl}")
	private String vortoServerUrl;
	
	@Value("${server.serviceUrl}")
	private String applicationServiceUrl;
	
	public String getVortoRepoUrl() {
		String vortoRepoUrl = System.getenv("VORTO_REPO_URL");
		if (!Strings.nullToEmpty(vortoRepoUrl).trim().isEmpty()) {
			return vortoRepoUrl;
		}
		return vortoServerUrl;
	}

	public String getAppServiceUrl() {
		String serviceoUrl = System.getenv("APP_SERVICE_URL");
		if (!Strings.nullToEmpty(serviceoUrl).trim().isEmpty()) {
			return serviceoUrl;
		}
		return applicationServiceUrl;
	}
}
