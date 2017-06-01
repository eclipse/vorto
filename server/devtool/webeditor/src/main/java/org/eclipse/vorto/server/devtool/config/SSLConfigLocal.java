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
package org.eclipse.vorto.server.devtool.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Disables SSL Verification for local development / testing
 *
 */
@Configuration
@Profile("local")
public class SSLConfigLocal {

	@PostConstruct
	public void ignoreSSLCertificateVerification() {
		SSLContext ctx = null;
		TrustManager[] trustAllCerts = new X509TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		try {
			ctx = SSLContext.getInstance("SSL");
			ctx.init(null, trustAllCerts, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}

		SSLContext.setDefault(ctx);
	}
}
