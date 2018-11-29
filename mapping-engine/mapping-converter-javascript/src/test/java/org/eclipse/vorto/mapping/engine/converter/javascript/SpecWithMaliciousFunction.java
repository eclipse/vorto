package org.eclipse.vorto.mapping.engine.converter.javascript;

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

public abstract class SpecWithMaliciousFunction extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    addFunctionblockProperty("button", createButtonFb());
  }

  private FunctionblockModel createButtonFb() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
    ModelProperty digitalInputCount = new ModelProperty();
    digitalInputCount.setMandatory(true);
    digitalInputCount.setName("digital_input_count");
    digitalInputCount.setType(PrimitiveType.INT);

    digitalInputCount.setTargetPlatformKey("iotbutton");
    digitalInputCount.addStereotype(Stereotype.createWithXpath("button:convert(clickType)"));

    buttonModel.setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputCount}));

    return buttonModel;
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
    FunctionLibrary library = new FunctionLibrary();
    IScriptEvaluator evaluator = evalProvider.createEvaluator("button");
    evaluator.addScriptFunction(new ScriptClassFunction("convert",
        "function convert(value) { " + getMaliciousFunctionBody() + "}"));
    library.addFunctions(evaluator.getFunctions());

    return library;
  }

  protected abstract String getMaliciousFunctionBody();


}
