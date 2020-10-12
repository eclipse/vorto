/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.server.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Security configuration for UI Testing. Bypass the oauth2 authentication by enabling a simple form login and a Mock
 * authentication provider.
 */
@Profile({"local-ui-test", "local-benchmark-test"})
@Configuration
public class MockApplicationSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProviderMock authenticationProviderMock;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // enable the simple form login and keep the rest as in the real authentication configuration.
        http.formLogin();
        http.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.GET, "/rest/**", "/api/**")
                .permitAll().antMatchers("/user/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/rest/**", "/api/**").authenticated()
                .antMatchers(HttpMethod.POST, "/rest/**", "/api/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/rest/**", "/api/**").authenticated().and()
                .csrf()
                .csrfTokenRepository(csrfTokenRepository()).and().csrf().disable().logout()
                .logoutUrl("/logout").logoutSuccessUrl("/").and().headers().frameOptions().sameOrigin();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProviderMock);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }


}