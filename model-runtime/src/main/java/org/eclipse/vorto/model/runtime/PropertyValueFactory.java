package org.eclipse.vorto.model.runtime;

import org.eclipse.vorto.model.ModelProperty;

public final class PropertyValueFactory {

  public static PropertyValue create(ModelProperty meta, Object value) {
    if (value instanceof EntityValue) {
      return new EntityPropertyValue(meta, (EntityValue)value);
    } else if (value instanceof EnumValue) {
      return new EnumPropertyValue(meta,(EnumValue)value);
    } else {
      return new PropertyValue(meta, value);
    }
  }
}
