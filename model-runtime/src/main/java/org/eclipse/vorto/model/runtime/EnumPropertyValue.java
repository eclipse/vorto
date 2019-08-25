package org.eclipse.vorto.model.runtime;

import org.eclipse.vorto.model.ModelProperty;

public class EnumPropertyValue extends PropertyValue {

  public EnumPropertyValue(ModelProperty meta, EnumValue value) {
    super(meta, value);
  }

  @Override
  public EnumValue getValue() {
    return (EnumValue)super.getValue();
  }
}
