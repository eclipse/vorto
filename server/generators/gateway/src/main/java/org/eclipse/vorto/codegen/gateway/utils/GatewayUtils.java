package org.eclipse.vorto.codegen.gateway.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.ServiceClassifier;
import org.eclipse.vorto.codegen.gateway.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;

public class GatewayUtils {
	
	private static final String VORTO_SERVICE_TAGS = "vorto.service.tags";
	private static final String VORTO_SERVICE_CLASSIFIER = "vorto.service.classifier";
	private static final String VORTO_SERVICE_NAME = "vorto.service.name";
	private static final String VORTO_SERVICE_IMAGE32X32 = "vorto.service.image32x32";
	private static final String VORTO_SERVICE_IMAGE144X144 = "vorto.service.image144x144";
	private static final String VORTO_SERVICE_DOCUMENTATION_URL = "vorto.service.documentationUrl";
	private static final String VORTO_SERVICE_DESCRIPTION = "vorto.service.description";
	private static final String VORTO_SERVICE_CREATOR = "vorto.service.creator";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayUtils.class);
	
	public static GeneratorServiceInfo generatorInfoFromFile(String configFile, IVortoCodeGenerator generator) {
		Objects.requireNonNull(configFile);
		Objects.requireNonNull(generator);
		
		GeneratorServiceInfo serviceInfo = new GeneratorServiceInfo();
		try {
			Properties properties = new Properties();
			properties.load(new ClassPathResource(configFile).getInputStream());
			
			serviceInfo.setCreator(properties.getProperty(VORTO_SERVICE_CREATOR));
			serviceInfo.setDescription(properties.getProperty(VORTO_SERVICE_DESCRIPTION));
			serviceInfo.setDocumentationUrl(properties.getProperty(VORTO_SERVICE_DOCUMENTATION_URL));
			serviceInfo.setImage144x144(encodeToBase64(properties.getProperty(VORTO_SERVICE_IMAGE144X144)));
			serviceInfo.setImage32x32(encodeToBase64(properties.getProperty(VORTO_SERVICE_IMAGE32X32)));
			serviceInfo.setKey(generator.getServiceKey());
			serviceInfo.setName(properties.getProperty(VORTO_SERVICE_NAME));
			serviceInfo.setClassifier(ServiceClassifier.valueOf(properties.getProperty(VORTO_SERVICE_CLASSIFIER)));
			serviceInfo.setTags(properties.getProperty(VORTO_SERVICE_TAGS,"").split(","));
			
		} catch (IOException e) {
			throw new RuntimeException("Error in loading configFile to Properties", e);
		}
		
		return serviceInfo;
	};
	
	public static String encodeToBase64(String image) {
		try {
			return Base64Utils.encodeToString(StreamUtils.copyToByteArray(new ClassPathResource(image).getInputStream()));
		} catch(IOException ex) {
			LOGGER.error("Could not encode image {}", image, ex);
			return null;
		}
	}
	
	public static Map<String, String> mapFromRequest(final HttpServletRequest request) {
		Map<String,String> requestParams = new HashMap<>();
		request.getParameterMap().entrySet().stream().forEach(x -> requestParams.put(x.getKey(), x.getValue()[0]));
		return requestParams;
	}
	
	public static Supplier<NotFoundException> notFound(String subject) {
		return () -> new NotFoundException(subject + " Not Found.");
	}
}
