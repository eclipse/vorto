package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Operation;
import org.eclipse.vorto.repository.api.content.Param;
import org.eclipse.vorto.repository.api.content.Stereotype;

public class SpecWithOperationMapping extends AbstractTestSpec {
	
	@Override
	protected void createFBSpec() {
		FunctionblockModel buttonModel = new FunctionblockModel(
				ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
		Operation operation = new Operation();
		operation.setName("press");
		Param param = new Param();
		param.setName("count");
		param.setType(ModelId.fromPrettyFormat("demo.types:Count:1.0.0"));
		param.setTargetPlatformKey("iotbutton");
		param.addStereotype(Stereotype.createOperationTarget("data/count", "type:convertInt(obj/count)"));
		operation.setParams(Arrays.asList(param));

		operation.setTargetPlatformKey("iotbutton");
		
		buttonModel.setOperations(Arrays.asList(operation));
		
		addFunctionblockProperty("button", buttonModel);
	}

}
