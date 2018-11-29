package org.eclipse.vorto.mapping.engine.converter.binary;

import java.util.Arrays;
import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.service.mapping.spec.AbstractTestSpec;

public class SpecWithByteArrayConverter extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
    ModelProperty digitalInputStateProperty = new ModelProperty();
    digitalInputStateProperty.setMandatory(true);
    digitalInputStateProperty.setName("sensor_value");
    digitalInputStateProperty.setType(PrimitiveType.STRING);

    digitalInputStateProperty.setTargetPlatformKey("iotbutton");

    digitalInputStateProperty.addStereotype(Stereotype.createWithXpath(
        "button:convertSensorValue(vorto_conversion1:byteArrayToInt(vorto_conversion2:parseHexBinary(/data),vorto_array:length(vorto_conversion2:parseHexBinary(/data))-1,0,0,1))"));

    buttonModel.setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputStateProperty}));

    addFunctionblockProperty("button", buttonModel);
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
    FunctionLibrary library = new FunctionLibrary();
    IScriptEvaluator evaluator = evalProvider.createEvaluator("button");
    evaluator.addScriptFunction(new ScriptClassFunction("convertSensorValue",
        "function convertSensorValue(value) { return value; }"));
    library.addFunctions(evaluator.getFunctions());
    return library;
  }

}
