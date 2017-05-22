package org.eclipse.vorto.repository.security.eidp;

import java.util.Map;

import org.eclipse.vorto.repository.service.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class IsEidpUserRegisteredInterceptor implements Interceptor {

	@Autowired
    private IUserRepository userRepository;
	
	@SuppressWarnings("unchecked")
	@Override
	public void intercept(OAuth2RestOperations restTemplate, OAuth2Authentication authentication, String accessToken) {
		Map<String, String> userDetails = ((Map<String, String>) authentication.getUserAuthentication().getDetails());
		
		if (userRepository.findByEmail(userDetails.get("email")) == null) {
			userDetails.put("isRegistered", "false");
		} else {
			userDetails.put("isRegistered", "true");
		}
	}

}
