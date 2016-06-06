package org.eclipse.vorto.example;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

import org.eclipse.vorto.example.template.*;

/**
 * 
 * @author Ying Shaodong - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class CGenerator implements IVortoCodeGenerator {
		
	@Override
	public IGenerationResult generate(InformationModel model, InvocationContext context) {
		
		GenerationResultZip outputter = new GenerationResultZip(model, getServiceKey());
		for (FunctionblockProperty property : model.getProperties()) {
			ChainedCodeGeneratorTask<FunctionblockModel> generator = new ChainedCodeGeneratorTask<FunctionblockModel>();
			
			if (property.getType().getFunctionblock().getStatus() != null) {
				generator.addTask(new GeneratorTaskFromFileTemplate<>(new CHeaderTemplate()));
			}
			generator.generate(property.getType(),context, outputter);
		}
		return outputter;
	}
	
	@Override
	public String getServiceKey() {
		return "c";
	}

}
