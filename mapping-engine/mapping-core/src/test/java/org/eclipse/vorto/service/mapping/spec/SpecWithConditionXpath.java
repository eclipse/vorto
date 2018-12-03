package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;

public class SpecWithConditionXpath extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
    ModelProperty digitalInputStateProperty = new ModelProperty();
    digitalInputStateProperty.setMandatory(true);
    digitalInputStateProperty.setName("sensor_value");
    digitalInputStateProperty.setType(PrimitiveType.FLOAT);

    digitalInputStateProperty.setTargetPlatformKey("iotbutton");
    digitalInputStateProperty.addStereotype(Stereotype
        .createWithConditionalXpath("xpath:eval('data[@id = 100]/value',this) == 'x'", "100"));

    buttonModel.setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputStateProperty}));

    addFunctionblockProperty("button", buttonModel);
  }

}
