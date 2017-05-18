package org.eclipse.vorto.codegen.gateway.config;

import org.eclipse.vorto.codegen.gateway.utils.GatewayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class EnvironmentConfig {

	@Autowired
	private Environment env;
	
	public String getVortoRepoUrl() {
		String vortoRepoUrl = System.getenv("VORTO_REPO_URL");
		if (!Strings.nullToEmpty(vortoRepoUrl).trim().isEmpty()) {
			return vortoRepoUrl;
		}
		
		String vortoServerUrl = env.getProperty("vorto.serverUrl");
		if (!Strings.nullToEmpty(vortoServerUrl).trim().isEmpty()) {
			return vortoServerUrl;
		}
		
		throw GatewayUtils.notFound("Not able to get the Vorto Server URL both from the " + 
				"Environment variable VORTO_REPO_URL or the config setting 'vorto.serverUrl'").get();
	}

	public String getAppServiceUrl() {
		String serviceoUrl = System.getenv("APP_SERVICE_URL");
		if (!Strings.nullToEmpty(serviceoUrl).trim().isEmpty()) {
			return serviceoUrl;
		}
		
		String applicationServiceUrl = env.getProperty("server.serviceUrl");
		if (!Strings.nullToEmpty(applicationServiceUrl).trim().isEmpty()) {
			return applicationServiceUrl;
		}
		
		throw GatewayUtils.notFound("Not able to get the Application Service URL both from the " + 
				"Environment variable APP_SERVICE_URL or the config setting 'server.serviceUrl'").get();
	}
}
