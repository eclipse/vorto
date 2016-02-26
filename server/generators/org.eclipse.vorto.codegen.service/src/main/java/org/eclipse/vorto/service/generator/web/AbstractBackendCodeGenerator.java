package org.eclipse.vorto.service.generator.web;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.ServiceClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = { "org.eclipse.vorto.service.generator" })
@EnableConfigurationProperties
public class AbstractBackendCodeGenerator extends SpringBootServletInitializer {

	private RestTemplate restTemplate;
	
	@Value("${vorto.service.repositoryUrl}") 
	private String repositoryBasePath;
	
	@Value("${vorto.service.classifier}")
	private ServiceClassifier classifier;
		
	@Value("${server.contextPath}")
	private String contextPath;
	
	@Autowired
	private IVortoCodeGenerator platformGenerator;
	
	@Value("${server.host}") 
	private String serviceUrl = "localhost";
	
	@Value("${server.port}") 
	private int port;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBackendCodeGenerator.class);
		
	@PostConstruct
	public void start() {
		this.restTemplate = new RestTemplate();
		register(platformGenerator.getServiceKey());
	}
	
	@PreDestroy
	public void shutdown() {
		deRegister(platformGenerator.getServiceKey());
	}
	
	public void register(String serviceKey){
		final String serviceUrl = "http://"+this.serviceUrl+":"+port+contextPath+"/rest/generation";
		LOGGER.info("Registering {} with service url {}",serviceKey,serviceUrl);
		LOGGER.info("Repository Server Url: {}",repositoryBasePath);
		deRegister(serviceKey);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(serviceUrl, headers);
		
		restTemplate.put(repositoryBasePath + "/generation-router/register/{serviceKey}/{classifier}",entity,serviceKey,classifier.name());
	}
	
	public void deRegister(String serviceKey){
		restTemplate.put(repositoryBasePath + "/generation-router/deregister/{serviceKey}", String.class, serviceKey);
	}
}

