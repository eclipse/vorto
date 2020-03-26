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
package org.eclipse.vorto.repository.server.config.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.vorto.repository.oauth.IOAuthFlowConfiguration;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.oauth.internal.filter.BearerTokenFilter;
import org.eclipse.vorto.repository.web.listeners.AuthenticationEntryPoint;
import org.eclipse.vorto.repository.web.tenant.TenantVerificationFilter;
import org.eclipse.vorto.repository.web.ui.AngularCsrfHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.filter.OrderedHttpPutFormContentFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CompositeFilter;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableOAuth2Client
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final Logger LOGGER = Logger.getLogger(SecurityConfiguration.class.getCanonicalName());
  /**
   * Regular expression defining Origin patterns that are too permissive for CORS.<br/>
   * This is by no means exhaustive, as some patterns e.g. [a-zA-Z0-9.]+, or [\\w.]+, etc. are just
   * as unsafe as the dot-based catch-all, but they are also very complex to represent. <br/>
   * Repository administrator discretion advised.
   */
  private static final Pattern UNSAFE_CORS_PATTERNS = Pattern.compile("\\.([+?*]+|\\{.+?,\\})");

  @Autowired
  private List<IOAuthFlowConfiguration> oauthProviders;

  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;

  @Value("${server.config.generatorUser}")
  private String generatorUser = "";

  @Value("${server.config.generatorPassword}")
  private String generatorUserPassword = "";

  @Value("${server.config.cors.allowedOrigins}")
  private String allowedOrigins;

  @Autowired
  private TenantVerificationFilter tenantVerificationFilter;

  @Autowired
  private IOAuthProviderRegistry oauthProviderRegistry;

  @Autowired
  private Environment env;

  private static final String ROLE_GENERATOR_PROVIDER = "GENERATOR_PROVIDER";

  @Bean
  public OrderedHttpPutFormContentFilter httpPutFormContentFilter() {
      return new OrderedHttpPutFormContentFilter() {
          @Override
          protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response,
                  FilterChain filterChain) throws ServletException, IOException {
              filterChain.doFilter(request, response);
          }
      };
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.GET, "/rest/**", "/api/**")
        .permitAll().antMatchers("/user/**").permitAll()
        .antMatchers(HttpMethod.PUT, "/rest/**", "/api/**").authenticated()
        .antMatchers(HttpMethod.POST, "/rest/**", "/api/**").authenticated()
        .antMatchers(HttpMethod.DELETE, "/rest/**", "/api/**").authenticated().and()
        .addFilterAfter(new AngularCsrfHeaderFilter(), CsrfFilter.class)
        .addFilterAfter(anonymousFilter(), AnonymousAuthenticationFilter.class)
        .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(bearerTokenFilter(), SecurityContextPersistenceFilter.class)
        .addFilterAfter(tenantVerificationFilter, SecurityContextPersistenceFilter.class).csrf()
        .csrfTokenRepository(csrfTokenRepository()).and().csrf().disable().logout()
        .logoutUrl("/logout").logoutSuccessUrl("/").and().headers().frameOptions().sameOrigin();

    if (isCloudProfile()) {
      http.requiresChannel().anyRequest().requiresSecure();
    }

    http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    http.cors().configurationSource(corsConfigurationSource());
  }

  /**
   * Provides a configuration bean to allow CORS for a specific set of origins, expressed in
   * configuration resources as regular expressions. <br/>
   * Overloads the logic in {@link CorsConfiguration} in order to allow evaluation of origins as
   * regex in case standard ignore-case equality checks fail.<br>
   * Logs a warning if a regular expression is found to be too permissive.
   * @return
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration() {
      @Override
      public String checkOrigin(String requestOrigin) {
        String result = super.checkOrigin(requestOrigin);
        if (result != null) {
          return result;
        }
        // if no luck with exact match, try a pattern-based approach
        else {
          for (String ao : super.getAllowedOrigins()) {
            try {
              if (Pattern.compile(ao).matcher(requestOrigin).find()) {
                return requestOrigin;
              }
            }
            // value cannot be interpreted as pattern - swallowing
            catch (PatternSyntaxException pse) {
              continue;
            }
          }
        }
        return null;
      }
    };
    // verifies allowed origins configured for this repository are not too permissive, logs a
    // warning if any found
    List<String> allowedOriginsAsList = Arrays.asList(allowedOrigins.split(",\\s*"));
    for (String origin: allowedOriginsAsList) {
      // matches against the whole expression
      if (UNSAFE_CORS_PATTERNS.matcher(origin).matches()) {
        LOGGER.warning(String.format("Unsafe CORS allowed origin [%s] - the Vorto repository may be insecure.", origin));
      }
    }
    configuration.setAllowedOrigins(allowedOriginsAsList);
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  private boolean isCloudProfile() {
    return env.acceptsProfiles("prod", "int");
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser(generatorUser).password(generatorUserPassword)
        .roles(ROLE_GENERATOR_PROVIDER);
  }

  @Bean
  public Filter anonymousFilter() {
    return new MyAnonymousAuthFilter();
  }

  @Bean
  public static PasswordEncoder encoder() {
    return new BCryptPasswordEncoder(11);
  }

  private CsrfTokenRepository csrfTokenRepository() {
    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
    repository.setHeaderName("X-XSRF-TOKEN");
    return repository;
  }

  @Bean
  public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(filter);
    registration.setOrder(-100);
    return registration;
  }

  private Filter bearerTokenFilter() {
    return new BearerTokenFilter(oauthProviderRegistry);
  }

  private Filter ssoFilter() {
    CompositeFilter filter = new CompositeFilter();
    List<Filter> ssoFilters = new ArrayList<Filter>(this.oauthProviders.size());  
    this.oauthProviders.forEach(provider -> ssoFilters.add(provider.createFilter()));   
    filter.setFilters(ssoFilters);
    return filter;
  }

}
