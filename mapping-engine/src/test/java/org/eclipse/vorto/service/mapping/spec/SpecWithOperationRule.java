package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Operation;
import org.eclipse.vorto.repository.api.content.Stereotype;

public class SpecWithOperationRule extends AbstractTestSpec {

	@Override
	protected void createFBSpec() {
		FunctionblockModel buttonModel = new FunctionblockModel(
				ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
		Operation operation = new Operation();
		operation.setName("press");
		operation.setTargetPlatformKey("iotbutton");

		
		operation.addStereotype(Stereotype.createOperationTarget("data/key","Pressed"));

		buttonModel.setOperations(
				Arrays.asList(new Operation[] { operation }));
		
		
		addFunctionblockProperty("button", buttonModel);
	}
	
	@Override
	public Optional<Functions> getCustomFunctions() {
		return Optional.empty();
	}

}
