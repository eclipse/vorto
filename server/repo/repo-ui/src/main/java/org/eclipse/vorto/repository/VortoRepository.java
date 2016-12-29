/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository;

import static com.google.common.base.Predicates.or;

import org.eclipse.vorto.repository.internal.service.ITemporaryStorage;
import org.eclipse.vorto.repository.internal.service.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.web.AngularCsrfHeaderFilter;
import org.eclipse.vorto.repository.web.listeners.RESTAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Configuration
@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories
@EnableScheduling
public class VortoRepository {

	public static void main(String[] args) {
		SpringApplication.run(VortoRepository.class, args);
	}
	
	@Bean
	public ITemporaryStorage createTempStorage() {
		return new InMemoryTemporaryStorage();
	}

	@Bean
	public Docket vortoApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).useDefaultResponseMessages(false)
				.select().paths(paths()).build();

	}

	@SuppressWarnings("unchecked")
	private Predicate<String> paths() {
		return or(	PathSelectors.regex("/rest/secure.*"),
					PathSelectors.regex("/rest/model.*"),
					PathSelectors.regex("/rest/resolver.*"),
					PathSelectors.regex("/rest/generation-router.*"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Vorto",
				"Vorto is an open source tool that allows to create and manage technology agnostic, abstract device descriptions, so called information models. <br/>"
						+ "Information models describe the attributes and the capabilities of real world devices. <br/>"
						+ "These information models can be managed and shared within the Vorto Information Model Repository. <br/>"
						+ " Code Generators for Information Models let you integrate devices into different platforms."
						+ "<br/>",
				"1.0.0", "", "Eclipse Vorto Team", "EPL", "https://eclipse.org/org/documents/epl-v10.php");
	}

	@Bean
	public static PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(securedEnabled = true)
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired
		private RESTAuthenticationEntryPoint authenticationEntryPoint;

		@Autowired
		private PasswordEncoder passwordEncoder;

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.httpBasic().and().authorizeRequests()
					.antMatchers(HttpMethod.GET, "/rest/**").permitAll()
					.antMatchers("/user/**").permitAll()
					.antMatchers(HttpMethod.PUT, "/rest/**").permitAll()
					.antMatchers(HttpMethod.POST, "/rest/secure/**").authenticated()
					.antMatchers(HttpMethod.DELETE, "/rest/**").authenticated()
					.and()
					.addFilterAfter(new AngularCsrfHeaderFilter(), CsrfFilter.class).csrf()
					.csrfTokenRepository(csrfTokenRepository()).and().csrf().disable().logout().logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.and()
					.headers()
					.frameOptions().sameOrigin()
					.httpStrictTransportSecurity().disable();

			http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		}

		private CsrfTokenRepository csrfTokenRepository() {
			HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
			repository.setHeaderName("X-XSRF-TOKEN");
			return repository;
		}

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		}
	}
	
	@Service
	public static class ScheduleTask {
	
		@Autowired
		private ITemporaryStorage storage;
	
		@Scheduled(fixedRate = 1000 * 60 * 60)
		public void clearExpiredStorageItems() {
			this.storage.clearExpired();
		}
	
	}
}