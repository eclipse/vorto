package org.eclipse.vorto.service.mapping.spec;

import java.util.Arrays;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;

public class SpecWithSameFunctionblock extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
    ModelProperty digitalInputStateProperty = new ModelProperty();
    digitalInputStateProperty.setMandatory(true);
    digitalInputStateProperty.setName("sensor_value");
    digitalInputStateProperty.setType(PrimitiveType.DATETIME);

    digitalInputStateProperty.setTargetPlatformKey("iotbutton");
    digitalInputStateProperty.addStereotype(Stereotype.createWithXpath("/@btnvalue1"));

    buttonModel.setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputStateProperty}));

    addFunctionblockProperty("btn1", buttonModel);

    FunctionblockModel buttonModel2 = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
    ModelProperty digitalInputStateProperty2 = new ModelProperty();
    digitalInputStateProperty2.setMandatory(true);
    digitalInputStateProperty2.setName("sensor_value");
    digitalInputStateProperty2.setType(PrimitiveType.DATETIME);

    digitalInputStateProperty2.setTargetPlatformKey("iotbutton");
    digitalInputStateProperty2.addStereotype(Stereotype.createWithXpath("/@btnvalue2"));

    buttonModel2
        .setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputStateProperty2}));

    addFunctionblockProperty("btn2", buttonModel2);
  }

}
