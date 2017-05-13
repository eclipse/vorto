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
package org.eclipse.vorto.server.devtool;

import org.eclipse.vorto.server.devtool.service.IServletInitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@SpringBootApplication
@EnableJpaRepositories
public class DevToolServer extends SpringBootServletInitializer {

	private static IServletInitializerService serveletInitializerService;

	public static void main(String... args) {
		SpringApplication.run(DevToolServer.class, args);
		serveletInitializerService.initializeXtextServlets();
	}

	@Autowired
	public void setXtextServletInitializer(IServletInitializerService serveletInitializerService) {
		DevToolServer.serveletInitializerService = serveletInitializerService;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DevToolServer.class);
	}

	@Bean
	public static PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Configuration
	@EnableWebSecurity
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired
		private PasswordEncoder passwordEncoder;

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/webjars/**").antMatchers("/dist/**").antMatchers("/css/**")
					.antMatchers("/images/**").antMatchers("/index.html");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/rest/context/user", "/project/**", "/editor/**", "/publish/**")
					.authenticated().and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessUrl("/index.html");
			http.formLogin().loginProcessingUrl("/j_spring_security_check");
			http.csrf().disable();

		}

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.eraseCredentials(false).userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		}
	}
}
