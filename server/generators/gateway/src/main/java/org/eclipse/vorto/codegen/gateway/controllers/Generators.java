package org.eclipse.vorto.codegen.gateway.controllers;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.gateway.config.Environment;
import org.eclipse.vorto.codegen.gateway.model.Generator;
import org.eclipse.vorto.codegen.gateway.repository.GeneratorRepository;
import org.eclipse.vorto.codegen.gateway.service.VortoService;
import org.eclipse.vorto.codegen.gateway.utils.GatewayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class Generators {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private VortoService vorto;
	
	@Autowired
	private GeneratorRepository repo;
	
	@RequestMapping(value = "/generators", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<GeneratorServiceInfo> list() {
		return repo.list().stream().map(generator -> generator.getInfo()).collect(Collectors.toList());
	}
	
	@RequestMapping(value = "/generators/{key}/generate/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratorServiceInfo info(final @PathVariable String key) {
		Generator generator = repo.get(key).orElseThrow(GatewayUtils.notFound(String.format("[Generator %s]", key)));
		return generator.getInfo();
	}
	
	@RequestMapping(value = "/generators/{key}/generate/{namespace}/{name}/{version:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InputStreamResource> generate(final @PathVariable String key, @PathVariable String namespace,
			@PathVariable String name, @PathVariable String version, final HttpServletRequest request) {
		return responseFromResult(vorto.generate(key, namespace, name, version, GatewayUtils.mapFromRequest(request)));
	}
	
	private ResponseEntity<InputStreamResource> responseFromResult(IGenerationResult result) {
		return ResponseEntity.ok().contentLength(result.getContent().length)
				.header("content-disposition", "attachment; filename = " + result.getFileName())
				.contentType(MediaType.parseMediaType(result.getMediatype()))
				.body(new InputStreamResource(new ByteArrayInputStream(result.getContent())));
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> reRegister() {
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			repo.list().stream().forEach(vorto::register);
			map.put("result", "OK");
		} catch(Exception e) {
			map.put("result", "ERROR");
			map.put("errorMessage", e.getMessage());
		}
		
		map.put("vortoServerUrl", env.getVortoRepoUrl());
		map.put("applicationServiceUrl", env.getAppServiceUrl());
		map.put("numGenerators", Integer.toString(repo.list().size()));
		
		return map;
	}
}
