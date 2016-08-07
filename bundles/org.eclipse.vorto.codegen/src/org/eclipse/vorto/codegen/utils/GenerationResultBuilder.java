package org.eclipse.vorto.codegen.utils;

import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.ZipContentExtractCodeGeneratorTask;

public class GenerationResultBuilder {

	IGenerationResult result;
	
	private GenerationResultBuilder(IGenerationResult result) {
		this.result = result;
	}
	
	public static GenerationResultBuilder from(IGenerationResult result) {
		GenerationResultBuilder builder = new GenerationResultBuilder(result);
		return builder;
	}
	
	public GenerationResultBuilder append(IGenerationResult result) {
		if (result == null) {
			return this;
		}
		
		ZipContentExtractCodeGeneratorTask task = new ZipContentExtractCodeGeneratorTask(result.getContent());
		task.generate(null, InvocationContext.simpleInvocationContext(),(IGeneratedWriter)this.result);
		return this;
	}
	
	public IGenerationResult build() {
		return result;
	}
}
