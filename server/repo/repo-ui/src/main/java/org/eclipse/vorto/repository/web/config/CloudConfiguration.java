package org.eclipse.vorto.repository.web.config;

import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

@Configuration
@Profile("cloud")
public class CloudConfiguration {

	@Bean
	public RepositoryConfiguration repoConfiguration() throws Exception {
		return RepositoryConfiguration.read(new ClassPathResource("vorto-repository-config-h2.json").getURL());
	}
}
