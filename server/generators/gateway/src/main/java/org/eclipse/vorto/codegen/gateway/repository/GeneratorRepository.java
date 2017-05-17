package org.eclipse.vorto.codegen.gateway.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.eclipse.vorto.codegen.api.IGeneratorLookup;
import org.eclipse.vorto.codegen.gateway.model.Generator;
import org.eclipse.vorto.codegen.gateway.utils.GatewayUtils;
import org.springframework.stereotype.Component;


@Component
public class GeneratorRepository {
	
	private Collection<Generator> generators = new ArrayList<Generator>();
	
	public Collection<Generator> list() {
		return generators;
	}
	
	public Optional<Generator> get(String key) {
		return generators.stream().filter(generator -> generator.getInfo().getKey().equals(key)).findFirst();
	}
	
	public void add(Generator generator) {
		generators.add(generator);
	}
	
	public IGeneratorLookup newGeneratorLookup() {
		return (key) -> {
			Generator generator = get(key).orElseThrow(GatewayUtils.notFound(String.format("[Generator %s]", key)));
			return generator.getInstance();
		};
	}
	
}
	