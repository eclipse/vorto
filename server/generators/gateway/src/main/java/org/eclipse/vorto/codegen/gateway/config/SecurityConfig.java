package org.eclipse.vorto.codegen.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

	/*
	@PostConstruct
	public void ignoreSSLCertificateVerification() {
		SSLContext ctx = null;
        TrustManager[] trustAllCerts = new X509TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers(){return null;}
            
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            	// Do nothing
            }
            
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // Do nothing	
            }
        }};
        
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, trustAllCerts, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
        	LOGGER.error("Error in ignoring SSL Ceritificate Verification", e);
        }

        SSLContext.setDefault(ctx);
	}*/
}
