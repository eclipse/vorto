package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;

public class SpecWithCondition extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
    ModelProperty digitalInputStateProperty = new ModelProperty();
    digitalInputStateProperty.setMandatory(true);
    digitalInputStateProperty.setName("sensor_value");
    digitalInputStateProperty.setType(PrimitiveType.FLOAT);

    ModelProperty digitalInputStateProperty2 = new ModelProperty();
    digitalInputStateProperty2.setMandatory(true);
    digitalInputStateProperty2.setName("sensor_value2");
    digitalInputStateProperty2.setType(PrimitiveType.FLOAT);

    digitalInputStateProperty.setTargetPlatformKey("iotbutton");
    digitalInputStateProperty
        .addStereotype(Stereotype.createWithConditionalXpath("count == 0", "/count"));

    digitalInputStateProperty2.setTargetPlatformKey("iotbutton");
    digitalInputStateProperty2
        .addStereotype(Stereotype.createWithConditionalXpath("count > 1", "/count"));

    buttonModel.setStatusProperties(
        Arrays.asList(new ModelProperty[] {digitalInputStateProperty, digitalInputStateProperty2}));

    addFunctionblockProperty("button", buttonModel);
  }

}
