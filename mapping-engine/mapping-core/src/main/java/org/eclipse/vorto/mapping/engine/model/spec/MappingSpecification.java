package org.eclipse.vorto.mapping.engine.model.spec;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.Stereotype;

public class MappingSpecification implements IMappingSpecification {

  private Infomodel infoModel;

  private Map<String, FunctionblockModel> properties = new HashMap<String, FunctionblockModel>();

  public MappingSpecification(Infomodel infoModel, Map<String, FunctionblockModel> properties) {
    this();
    this.infoModel = infoModel;
    this.properties = properties;
  }

  public MappingSpecification() {}

  public void setInfoModel(Infomodel infoModel) {
    this.infoModel = infoModel;
  }

  public void setProperties(Map<String, FunctionblockModel> properties) {
    this.properties = properties;
  }

  public Map<String, FunctionblockModel> getProperties() {
    return properties;
  }

  @Override
  public Infomodel getInfoModel() {
    return infoModel;
  }

  @Override
  public FunctionblockModel getFunctionBlock(String propertyName) {
    return properties.get(propertyName);
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider factory) {
    FunctionLibrary library = new FunctionLibrary();
    for (String propertyKey : this.properties.keySet()) {
      FunctionblockModel fbm = this.properties.get(propertyKey);
      if (fbm.getStereotype("functions").isPresent()) {
        Stereotype functionsStereoType = fbm.getStereotype("functions").get();
        IScriptEvaluator evaluator = factory.createEvaluator(propertyKey.toLowerCase());
        functionsStereoType.getAttributes().keySet().stream()
            .filter(functionName -> !functionName.equalsIgnoreCase("_namespace")).forEach(
                functionName -> evaluator.addScriptFunction(new ScriptClassFunction(functionName,
                    functionsStereoType.getAttributes().get(functionName))));
        library.addFunctions(evaluator.getFunctions());
      }
    }
    return library;
  }
}
