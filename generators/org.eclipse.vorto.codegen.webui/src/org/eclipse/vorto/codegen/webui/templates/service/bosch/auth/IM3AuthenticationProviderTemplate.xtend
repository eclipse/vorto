package org.eclipse.vorto.codegen.webui.templates.service.bosch.auth

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class IM3AuthenticationProviderTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''IM3AuthenticationProvider.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/permissions'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.permissions;
		
		import java.util.ArrayList;
		import java.util.List;
		import java.util.concurrent.CompletableFuture;
		import java.util.concurrent.ExecutionException;
		import java.util.concurrent.TimeUnit;
		import java.util.concurrent.TimeoutException;
		import java.util.function.Function;
		
		import javax.annotation.PostConstruct;
		
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		import org.springframework.security.authentication.AuthenticationProvider;
		import org.springframework.security.authentication.BadCredentialsException;
		import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
		import org.springframework.security.core.Authentication;
		import org.springframework.security.core.AuthenticationException;
		import org.springframework.security.core.GrantedAuthority;
		import org.springframework.security.core.authority.SimpleGrantedAuthority;
		import org.springframework.security.core.context.SecurityContextHolder;
		
		import com.bosch.im.api2.Scope;
		import com.bosch.im.api2.client.IClient;
		import com.bosch.im.api2.client.IClientBuilder;
		import com.bosch.im.api2.client.exception.server.AuthenticationDeniedException;
		import com.bosch.im.api2.dto.AuthenticationDto;
		import com.bosch.im.api2.dto.AuthorizationDto;
		import com.bosch.im.api2.jwt.IAuthorizationToken;
		
		public class IM3AuthenticationProvider implements AuthenticationProvider {
			private Logger logger = LoggerFactory.getLogger(IM3AuthenticationProvider.class);
		
			private String serviceUrl;
			
			private String clientId;
			
			private String clientSecret;
			
			private int expireTimeInSeconds = 60 * 60 * 2;
			
			private IClient client;
			
			private Function<AuthenticationDto, CompletableFuture<AuthorizationDto>> createAuthorization;
			private Function<AuthorizationDto, CompletableFuture<IAuthorizationToken>> parseAuthorization;
			
			@PostConstruct
			private void init() {
				logger.debug("ServiceUrl [" + serviceUrl + "] ClientID [" + clientId + "]");
				
				client = IClientBuilder.newInstance()
							.serviceUrl(serviceUrl)
							.clientId(clientId)
							.clientSecret(clientSecret)
							.build();
				
				createAuthorization = (AuthenticationDto dto) -> client.authorize()
					.idToken(dto.getIdToken())
					.userTenantId(dto.getUserTid())
					.scopes(Scope.pn, Scope.rn)
					.applications("Vorto")
					.execute();
		
				parseAuthorization = (AuthorizationDto dto) -> client.parseToAuthorizationToken()
					.authorizationToken(dto.getAuthorizationToken())
					.execute();
			}
		
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {		
				if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
					throw new BadCredentialsException("Cannot use the IM3 provider for this type of authentication");
				}
				
				UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
				
				Credential credential = getCredential(token);
		
				try {
					
					logger.debug("Logging IN user [" + credential.tenant + "\\" + credential.username + "]");
					
					UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationRequest(credential)
							.thenCompose(createAuthorization)
							.thenCompose(parseAuthorization)
							.thenApply(createToken(token, credential))
							.get(30, TimeUnit.SECONDS);
					
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					
					logger.debug("Log-in successful for user [" + credential.tenant + "\\" + credential.username + "]");
					
					return authenticationToken;
					
				} catch(InterruptedException | ExecutionException authEx) {
					logger.error("Internal Server Error", authEx);
					throw new RuntimeException("Internal server error while requesting IM permissions", authEx);
				} catch (TimeoutException authEx) {
					logger.error("Credential Request to IM timed out.", authEx);
					throw new RuntimeException("Credential Request Timed-out", authEx);
				} catch (AuthenticationDeniedException authEx) {
					logger.error("Wrong Credentials for user : " + token.getName(), authEx);
					throw new BadCredentialsException("Wrong Credentials");
				}
			}
			
			private Credential getCredential(UsernamePasswordAuthenticationToken token) {
				assert(token != null);
				assert(token.getName() != null);
				
				final String[] tenantAndUsername = token.getName().split("\\\\");
				
				if (tenantAndUsername.length != 2) {
					throw new BadCredentialsException("Either no tenant or no username");
				}
				
				return new Credential(tenantAndUsername[0], tenantAndUsername[1], token.getCredentials().toString());
			}
			
			public CompletableFuture<AuthenticationDto> createAuthenticationRequest(Credential credential) {
				return client.authenticate()
						.userTenantIdOrName(credential.tenant)
					  	.userName(credential.username)
					  	.password(credential.password)
					  	.expiresIn(expireTimeInSeconds)
					  	.execute();
			}
			
		    private Function<IAuthorizationToken, UsernamePasswordAuthenticationToken> createToken(UsernamePasswordAuthenticationToken token, 
		    		Credential credential) {
		    	return (IAuthorizationToken authToken) -> {
		    		if (authToken == null) {
						throw new BadCredentialsException("Wrong Credentials");
					}
		    		
		    		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();		
		    		
		    		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		    		
		    		UsernamePasswordAuthenticationToken imToken = new UsernamePasswordAuthenticationToken(token.getPrincipal(),
		    				credential.password, authorities);
		    		
		    		imToken.setDetails(new IMUserInfo(credential.username, 
		    				           credential.password, 
		    				           credential.tenant, 
		    				           authToken));
		    		
		    		return imToken;
		    	};
		    }
		    
			public String getServiceUrl() {
				return serviceUrl;
			}
		
			public void setServiceUrl(String serviceUrl) {
				this.serviceUrl = serviceUrl;
			}
		
			public String getClientId() {
				return clientId;
			}
		
			public void setClientId(String clientId) {
				this.clientId = clientId;
			}
		
			public String getClientSecret() {
				return clientSecret;
			}
		
			public void setClientSecret(String clientSecret) {
				this.clientSecret = clientSecret;
			}
		
			public int getExpireTimeInSeconds() {
				return expireTimeInSeconds;
			}
		
			public void setExpireTimeInSeconds(int expireTimeInSeconds) {
				this.expireTimeInSeconds = expireTimeInSeconds;
			}
		
			public boolean supports(Class<?> authentication) {
				return authentication.equals(UsernamePasswordAuthenticationToken.class);
			}
			
			private class Credential {
				private String tenant;
				private String username;
				private String password;
				
				public Credential(String tenant, String username, String password) {
					this.tenant = tenant;
					this.username = username;
					this.password = password;
				}
			}
		}

		
		'''
	}
	
}
