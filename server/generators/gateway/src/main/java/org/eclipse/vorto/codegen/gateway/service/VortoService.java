package org.eclipse.vorto.codegen.gateway.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.vorto.codegen.api.Generated;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.gateway.config.Environment;
import org.eclipse.vorto.codegen.gateway.exception.NotFoundException;
import org.eclipse.vorto.codegen.gateway.model.Generator;
import org.eclipse.vorto.codegen.gateway.repository.GeneratorRepository;
import org.eclipse.vorto.codegen.gateway.utils.GatewayUtils;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.server.commons.MappingZipFileExtractor;
import org.eclipse.vorto.server.commons.ModelZipFileExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;

@Component
public class VortoService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VortoService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private GeneratorRepository repo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public IGenerationResult generate(String key, String namespace, String name, String version, Map<String, String> parameters) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generating for Platform [%s] and Model [%s.%s:%s]", key, namespace, name, version);
		}
		
		Generator generator = repo.get(key).orElseThrow(GatewayUtils.notFound(String.format("[Generator %s]", key)));
		
		InformationModel model = getModel(namespace, name, version).orElseThrow(GatewayUtils.notFound(String.format("[Model %s.%s:%s]", namespace, name, version)));
		
		List<MappingModel> mappings = getMappings(key, namespace, name, version);
		
		InvocationContext invocationContext = new InvocationContext(mappings, repo.newGeneratorLookup(), parameters);
		
		return generate(generator.getInstance(), model, invocationContext);
	}

	private IGenerationResult generate(IVortoCodeGenerator generator, InformationModel model, InvocationContext invocationContext) {
		try {
			return generator.generate(model, invocationContext, null);
		} catch(Exception e) {
			GenerationResultZip output = new GenerationResultZip(model, generator.getServiceKey());
			Generated generated = new Generated("generation_error.log", "/generated", e.getMessage());
			output.write(generated);
			return output;
		}
	}
	
	public Optional<InformationModel> getModel(String namespace, String name, String version) {
		Optional<byte[]> modelResources = downloadUrl(urlForModel(namespace, name, version));
		
		if (!modelResources.isPresent()) {
			return Optional.empty();
		}
		
		ModelZipFileExtractor extractor = new ModelZipFileExtractor(modelResources.get());
		
		return toInformationModel(extractor.extract(name));
	}
	
	private String urlForModel(String namespace, String name, String version) {
		return String.format("%s/rest/model/file/%s/%s/%s?output=DSL&includeDependencies=true", 
				env.vortoServerUrl, namespace, name, version);
	}
	
	private Optional<InformationModel> toInformationModel(Model model) {
		if (model instanceof InformationModel) {
			return Optional.of((InformationModel) model);
		} else if (model instanceof FunctionblockModel) {
			return Optional.of(Utils.wrapFunctionBlock((FunctionblockModel) model));
		}
		
		throw new NotFoundException(String.format("[Model %s.%s:%s] is not an Information Model or a Function Block", 
				model.getNamespace(), model.getName(), model.getVersion()));
	}
	
	public List<MappingModel> getMappings(String generatorKey, String namespace, String name, String version) {
		Optional<byte[]> mappingResources = downloadUrl(urlForMapping(generatorKey, namespace, name, version));
		
		if (mappingResources.isPresent()) {
			return new MappingZipFileExtractor(mappingResources.get()).extract();
		} else {
			return Collections.emptyList();
		}
	}
	
	private String urlForMapping(String targetPlatform, String namespace, String name, String version) {
		return String.format("%s/rest/model/mapping/zip/%s/%s/%s/%s", 
				env.vortoServerUrl, namespace, name, version, targetPlatform);
	}
	
	private Optional<byte[]> downloadUrl(String url) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Downloading " + url);
			}
			
			return Optional.of(restTemplate.getForObject(url, byte[].class));
		} catch (RestClientException e) {
			LOGGER.error(e.getMessage());
			return Optional.empty();
		}
	}
	
	public void register(Generator generator) {
		deregister(generator);
		LOGGER.info("Registering Generator[" + generator.getInstance().getServiceKey() + "]");
		restTemplate.put(env.vortoServerUrl + "/rest/generation-router/register/{serviceKey}/{classifier}", getEntity(getServiceUrl(generator)), 
				generator.getInstance().getServiceKey(), generator.getInfo().getClassifier().name());
	}
	
	public void deregister(Generator generator) {
		LOGGER.info("Deregistering Generator[" + generator.getInstance().getServiceKey() + "]");
		restTemplate.put(env.vortoServerUrl + "/rest/generation-router/deregister/{serviceKey}", String.class, generator.getInstance().getServiceKey());
	}
	
	private HttpEntity<String> getEntity(String serviceUrl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<String>(serviceUrl, headers);
	}
	
	private String getServiceUrl(Generator generator) {
		return String.format("http://%s:%d%s/rest/generators/%s/generate", env.serverHost, env.serverPort, Strings.nullToEmpty(env.serverContextPath).trim(), 
				generator.getInstance().getServiceKey());
	}
}
