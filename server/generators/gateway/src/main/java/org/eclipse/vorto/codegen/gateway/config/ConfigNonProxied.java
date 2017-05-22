package org.eclipse.vorto.codegen.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile({"cloud", "nonproxied"})
public class ConfigNonProxied {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
