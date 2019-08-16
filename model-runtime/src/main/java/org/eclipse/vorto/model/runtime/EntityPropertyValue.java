package org.eclipse.vorto.model.runtime;

import org.eclipse.vorto.model.ModelProperty;

public class EntityPropertyValue extends PropertyValue {

  public EntityPropertyValue(ModelProperty meta, EntityValue value) {
    super(meta, value);
  }

  @Override
  public EntityValue getValue() {
    return (EntityValue)super.getValue();
  }
}
