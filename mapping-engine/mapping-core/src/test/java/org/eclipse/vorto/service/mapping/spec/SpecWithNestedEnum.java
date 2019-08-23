package org.eclipse.vorto.service.mapping.spec;

import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;

public class SpecWithNestedEnum extends AbstractTestSpec  {

  @Override
  protected void createModel() {
    
    EnumModel unitEnum = EnumModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Enum:1.0.0"))
      .literal("Celcius", null)
      .literal("Fahrenheit", null)
      .build();
    
    
    FunctionblockModel temperatureModel = FunctionblockModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Temperature:1.0.0"))
      .statusProperty(
          ModelProperty.Builder("value", PrimitiveType.FLOAT)
          .withXPathStereotype("/temperature", "iotbutton")
          .build()
       )
      .statusProperty(
          ModelProperty.Builder("unit", unitEnum.getId())
          .withXPathStereotype("'Celcius'", "iotbutton")
          .build()
       )
      .build();

    infomodel.getFunctionblocks().add(ModelProperty.Builder("outdoorTemperature",temperatureModel).build());
  }

}
