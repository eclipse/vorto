package org.eclipse.vorto.codegen.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Environment {
	
	@Value("${server.host}")
	public String serverHost;
	
	@Value("${server.port}")
	public int serverPort;
	
	@Value("${server.contextPath}")
	public String serverContextPath;
	
	@Value("${vorto.serverUrl}")
	public String vortoServerUrl;
	
}
