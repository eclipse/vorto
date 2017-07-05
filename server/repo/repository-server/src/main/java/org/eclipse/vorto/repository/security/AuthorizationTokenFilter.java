package org.eclipse.vorto.repository.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.filter.GenericFilterBean;

import com.google.common.base.Strings;

public class AuthorizationTokenFilter extends GenericFilterBean {

	private static final String BEARER = "Bearer";
	private static final String AUTHORIZATION = "Authorization";
	private UserInfoTokenServices userInfoService;
	
	public AuthorizationTokenFilter(UserInfoTokenServices userInfoService) {
		this.userInfoService = userInfoService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			Optional<String> authToken = getBearerToken((HttpServletRequest) request);
			if (authToken.isPresent()) {
				try {
					Authentication authentication = userInfoService.loadAuthentication(authToken.get());
					if (authentication != null) {
						SecurityContextHolder.getContext().setAuthentication(authentication);
						chain.doFilter(request, response);
						SecurityContextHolder.getContext().setAuthentication(null);
						return;
					}
				} catch(InvalidTokenException e) {
					// Do nothing. This is totally expected if token is wrong.
				}
			}
		}
		
		chain.doFilter(request, response);
	}
	
	public Optional<String> getBearerToken(HttpServletRequest request) {
		String authToken = request.getHeader(AUTHORIZATION);
		if (!Strings.nullToEmpty(authToken).trim().isEmpty()) {
			String[] tokenComposite = authToken.split(" ");
			if (BEARER.equals(tokenComposite[0]) && 
					!Strings.nullToEmpty(tokenComposite[1]).isEmpty()) {
				return Optional.of(tokenComposite[1]);
			}
		}
		
		return Optional.empty();
	}

}
