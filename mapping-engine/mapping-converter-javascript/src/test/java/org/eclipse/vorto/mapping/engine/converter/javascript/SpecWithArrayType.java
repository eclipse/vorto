package org.eclipse.vorto.mapping.engine.converter.javascript;

import java.util.Arrays;
import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.service.mapping.spec.AbstractTestSpec;

public class SpecWithArrayType extends AbstractTestSpec {

  @Override
  protected void createModel() {
    
    FunctionblockModel buttonModel = createButtonFb();
    infomodel.getFunctionblocks().add(ModelProperty.Builder("button",buttonModel).build());

  }

  private FunctionblockModel createButtonFb() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"));

    ModelProperty digitalInputCount = new ModelProperty();
    digitalInputCount.setMandatory(true);
    digitalInputCount.setName("flag");
    digitalInputCount.setType(PrimitiveType.STRING);

    digitalInputCount.setTargetPlatformKey("iotbutton");
    digitalInputCount
        .addStereotype(Stereotype.createWithXpath("button:convertArray(/data)"));

    buttonModel.setStatusProperties(
        Arrays.asList(new ModelProperty[] {digitalInputCount}));

    return buttonModel;
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
    FunctionLibrary library = new FunctionLibrary();
    IScriptEvaluator evaluator = evalProvider.createEvaluator("button");
    evaluator.addScriptFunction(new ScriptClassFunction("convertArray",
        "function convertArray(arrayValue) {  return String.fromCharCode(arrayValue[0])}"));
    library.addFunctions(evaluator.getFunctions());

    return library;
  }


}

