package org.eclipse.vorto.mapping.engine.converter.date;

import java.util.Arrays;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.service.mapping.spec.AbstractTestSpec;

public class SpecWithTimestamp extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
    ModelProperty digitalInputStateProperty = new ModelProperty();
    digitalInputStateProperty.setMandatory(true);
    digitalInputStateProperty.setName("sensor_value");
    digitalInputStateProperty.setType(PrimitiveType.DATETIME);

    digitalInputStateProperty.setTargetPlatformKey("iotbutton");
    digitalInputStateProperty.addStereotype(Stereotype.createWithXpath("vorto_date:format(time)"));

    buttonModel.setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputStateProperty}));

    addFunctionblockProperty("button", buttonModel);
  }

}
