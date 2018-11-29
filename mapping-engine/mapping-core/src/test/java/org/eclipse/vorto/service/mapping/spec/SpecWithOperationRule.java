package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;
import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.Operation;
import org.eclipse.vorto.model.Stereotype;

public class SpecWithOperationRule extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
    Operation operation = new Operation();
    operation.setName("press");
    operation.setTargetPlatformKey("iotbutton");


    operation.addStereotype(Stereotype.createOperationTarget("data/key", "Pressed"));

    buttonModel.setOperations(Arrays.asList(new Operation[] {operation}));


    addFunctionblockProperty("button", buttonModel);
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
    return new FunctionLibrary();
  }

}
