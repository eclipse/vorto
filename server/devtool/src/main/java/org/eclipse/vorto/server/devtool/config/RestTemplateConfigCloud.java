package org.eclipse.vorto.server.devtool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("cloud")
public class RestTemplateConfigCloud {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
