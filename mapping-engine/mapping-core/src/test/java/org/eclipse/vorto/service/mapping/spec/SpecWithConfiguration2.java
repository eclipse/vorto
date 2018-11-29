package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;
import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;

public class SpecWithConfiguration2 extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);


    ModelProperty buttonEnableProperty = new ModelProperty();
    buttonEnableProperty.setMandatory(true);
    buttonEnableProperty.setName("enable");
    buttonEnableProperty.setType(PrimitiveType.BOOLEAN);

    buttonEnableProperty.setTargetPlatformKey("iotbutton");

    buttonEnableProperty.addStereotype(Stereotype.createWithXpath("/e"));

    buttonModel
        .setConfigurationProperties(Arrays.asList(new ModelProperty[] {buttonEnableProperty}));

    addFunctionblockProperty("button", buttonModel);
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
    return new FunctionLibrary();
  }

}
