package org.eclipse.vorto.service.mapping.spec;

import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;

public class SpecWithNestedEntity extends AbstractTestSpec  {

  @Override
  protected void createModel() {
    
    EntityModel sensorValueEntity = EntityModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:SensorValue:1.0.0"))
      .property(
          ModelProperty.Builder("value", PrimitiveType.FLOAT)
          .withXPathStereotype("/temperature", "iotbutton").build()).build();
    
    
    FunctionblockModel temperatureModel = FunctionblockModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Temperature:1.0.0"))
      .statusProperty(
          ModelProperty.Builder("value", sensorValueEntity)
          .build()
    ).build();

    infomodel.getFunctionblocks().add(ModelProperty.Builder("outdoorTemperature",temperatureModel).build());
  }

}
