package org.eclipse.vorto.codegen.gateway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.ServiceClassifier;
import org.eclipse.vorto.codegen.gateway.exception.NotFoundException;
import org.eclipse.vorto.codegen.gateway.exception.PropertyLoadingException;
import org.eclipse.vorto.codegen.gateway.model.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
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
			throw new PropertyLoadingException("Error in loading configFile to Properties", e);
		}
		
		return serviceInfo;
	};
	
	public static String encodeToBase64(String filename) {
		InputStream ioStream = null;
		try {
			ioStream = new ClassPathResource(filename).getInputStream();
		} catch(IOException e) {
			try {
				ioStream = new FileSystemResource(filename).getInputStream();
			} catch(IOException e2) {
				LOGGER.error("Cannot convert '" + filename + "' to either a ClassPathResource or FileSystemResource", e2);
				return null;
			}
		}
		
		try {
			return Base64Utils.encodeToString(StreamUtils.copyToByteArray(ioStream));
		} catch(IOException ex) {
			LOGGER.error("Could not encode image " + filename, ex);
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
	
	public static Consumer<Generator> checkEnvModifications(Environment env) {
		return gen -> {
			String img32ModificationProperty = String.format("vorto.service.%s.image32x32", gen.getInstance().getServiceKey());
			if (env.containsProperty(img32ModificationProperty)) {
				LOGGER.info(String.format("Overriding 32x32 image for %s with %s", gen.getInstance().getServiceKey(), env.getProperty(img32ModificationProperty)));
				gen.getInfo().setImage32x32(encodeToBase64(env.getProperty(img32ModificationProperty)));
			}
			String img144ModificationProperty = String.format("vorto.service.%s.image144x144", gen.getInstance().getServiceKey());
			if (env.containsProperty(img144ModificationProperty)) {
				LOGGER.info(String.format("Overriding 144x144 image for %s with %s", gen.getInstance().getServiceKey(), env.getProperty(img144ModificationProperty)));
				gen.getInfo().setImage144x144(encodeToBase64(env.getProperty(img144ModificationProperty)));
			}
		};
	}
}
