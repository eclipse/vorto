package org.eclipse.vorto.repository.security;

import java.util.Map;

import org.eclipse.vorto.repository.service.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class InterceptedUserInfoTokenServices extends UserInfoTokenServices {

	@Autowired
    private IUserRepository userRepository;
	
	@Autowired
	public InterceptedUserInfoTokenServices(
			@Value("${github.oauth2.resource.userInfoUri}") String userInfoEndpointUrl, 
			@Value("${github.oauth2.client.clientId}") String clientId) {
		super(userInfoEndpointUrl, clientId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		OAuth2Authentication auth = super.loadAuthentication(accessToken);
		
		if (auth != null) {
			Map<String, Object> userDetails = ((Map<String, Object>) auth.getUserAuthentication().getDetails());
			
			if (userRepository.findByEmail((String) userDetails.get("email")) == null) {
				userDetails.put("isRegistered", "false");
			} else {
				userDetails.put("isRegistered", "true");
			}
			
			userDetails.put(SecurityConfiguration.LOGIN_TYPE, "github");
		}
		
		return auth;
	}
}
