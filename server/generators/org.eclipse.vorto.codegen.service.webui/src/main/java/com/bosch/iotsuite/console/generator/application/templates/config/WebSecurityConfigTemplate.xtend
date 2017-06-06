package com.bosch.iotsuite.console.generator.application.templates.config

import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class WebSecurityConfigTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''WebSecurityConfig.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/config'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».config;
		
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.beans.factory.annotation.Value;
		import org.springframework.context.annotation.Bean;
		import org.springframework.context.annotation.Configuration;
		import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
		import org.springframework.security.config.annotation.web.builders.HttpSecurity;
		import org.springframework.security.config.annotation.web.builders.WebSecurity;
		import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
		import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
		import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
		
		import com.bosch.iotsuite.data.permissions.IM3AuthenticationProvider;
		
		@Configuration
		@EnableWebSecurity
		public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
			
			@Value("${bosch.permissions.clientId}")
			private String clientId;
			
			@Value("${bosch.permissions.clientSecret}")
			private String clientSecret;
			
			@Value("${bosch.permissions.endpointUrl}")
			private String endpointUrl;
		
			@Autowired
			private IM3AuthenticationProvider authenticationProvider;
			
			@Override
			public void configure(WebSecurity web) throws Exception {
				web.ignoring().antMatchers("/webjars/**","/css/**","/js/**","/dist/**");
			}
			
			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http.authorizeRequests().antMatchers("/rest/identities/user/**","/rest/devices/**").authenticated().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index.html");
				http.formLogin().loginPage("/login");
				http.csrf().disable();
		
			}
			
			@Autowired
			public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
				auth.authenticationProvider(authenticationProvider);
			}
			
			@Bean
			public IM3AuthenticationProvider authenticationProvider() {
				IM3AuthenticationProvider auth = new IM3AuthenticationProvider();
				auth.setClientId(clientId);
				auth.setClientSecret(clientSecret);
				auth.setServiceUrl(endpointUrl);
				return auth;
			}
				
		}
		'''
	}
	
}