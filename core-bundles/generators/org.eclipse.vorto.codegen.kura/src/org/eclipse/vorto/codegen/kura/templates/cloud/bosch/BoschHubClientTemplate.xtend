package org.eclipse.vorto.codegen.kura.templates.cloud.bosch

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class BoschHubClientTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''BoschHubClient.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»/cloud/bosch'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.getJavaPackage(element)».cloud.bosch;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Objects;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoschHubClient {
	private static final Logger logger = LoggerFactory.getLogger(BoschHubClient.class);
	
	private static final URL CERTIFICATE_LOCATION = BoschHubClient.class.getResource("/secret/iothub.crt");

	private String mqttHostUrl;
	private String clientId;
	private MqttClient mqqtClient = null;
	private MqttConnectOptions connOpts = null;

	public BoschHubClient(String mqttHostUrl, String clientId) {
		this.mqttHostUrl = Objects.requireNonNull(mqttHostUrl);
		this.clientId = Objects.requireNonNull(clientId);
		
		try {
			logger.info(String.format("Creating Hub Client for [%s] with Client Id [%s]", mqttHostUrl, clientId));
			mqqtClient = new MqttClient(mqttHostUrl, clientId, new MemoryPersistence());
			
			connOpts = new MqttConnectOptions();
			connOpts.setSocketFactory(buildSslSocketFactory(CERTIFICATE_LOCATION));
		} catch (MqttException e) {
			logger.error("Exception connecting to Hono", e);
			throw new RuntimeException(e);
		}
	}

	public void connect() {
		try {
			logger.info("Connecting to Hub.");
			IMqttToken token = mqqtClient.connectWithResult(connOpts);
			
			token.setActionCallback(new IMqttActionListener() {
				@Override
				public void onFailure(IMqttToken arg0, Throwable arg1) {
					logger.error("Failed to connect to Hono [" + mqttHostUrl + ", " + clientId + "]", arg1);
				}

				@Override
				public void onSuccess(IMqttToken arg0) {
					logger.info("Connected to Hono [" + mqttHostUrl + ", " + clientId + "]");
				}
			});
			logger.info("Hub Client CONNECTED.");
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void send(String topic, String message) {
		if (mqqtClient != null) {
			if (mqqtClient.isConnected()) {
				try {
					logger.info(String.format("Sending topic [%s] message [%s]", topic, message));
					mqqtClient.publish(topic, message.getBytes(), 0, false);
					logger.info(String.format("Send done."));
				} catch (MqttException e) {
					logger.error("Mqqt Client has error in sending message.", e);
				}
			} else {
				logger.error("Mqqt Client is not connected.");
				throw new RuntimeException("MQQT Client is not connected.");
			}
		} else {
			logger.error("Mqqt Client is not initialized.");
			throw new RuntimeException("MQQT Client is not initialized.");
		}
	}
	
	public boolean isConnected() {
		return mqqtClient != null && mqqtClient.isConnected();
	}

	private static SSLSocketFactory buildSslSocketFactory(URL certificateUrl) {
		try {

			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

			InputStream certificateInputstream = new BufferedInputStream(Objects.requireNonNull(certificateUrl).openStream());
			Certificate certificate;
			try {
				certificate = certFactory.generateCertificate(certificateInputstream);
			} finally {
				certificateInputstream.close();
			}

			// Create a KeyStore containing our trusted CAs
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", certificate);

			// Create a TrustManager that trusts the CAs in our KeyStore
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);

			// Create an SSLContext that uses our TrustManager
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, tmf.getTrustManagers(), null);
			return context.getSocketFactory();

		} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException | IOException e) {
			logger.error("Error building ssl socket factory", e);
		}
		
		return null;
	}
}
'''
	}
	
}