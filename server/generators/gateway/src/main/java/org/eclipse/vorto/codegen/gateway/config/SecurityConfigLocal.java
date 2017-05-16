package org.eclipse.vorto.codegen.gateway.config;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class SecurityConfigLocal {
	
	private static final String LOCALHOST = "localhost";

	@PostConstruct
	public void ignoreHostnameVerification() {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession sslSession) {
				if (hostname.equals(LOCALHOST)) {
					return true;
				}
				return false;
			}
		});
	}
}
