package org.eclipse.vorto.repository.api;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.eclipse.vorto.repository.api.generation.GeneratedOutput;
import org.eclipse.vorto.repository.api.generation.GeneratorInfo;

public interface IModelGeneration {

	CompletableFuture<Set<String>> getAvailableGenerators();
	
	CompletableFuture<GeneratorInfo> getMoreInfo(String generatorKey);
	
	CompletableFuture<GeneratedOutput> generate(ModelId modelId, String generatorKey);
}
