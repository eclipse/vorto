package org.eclipse.vorto.model.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelProperty;

public class ModelValueFactory {

  public static PropertyValue createFBPropertyValue(FunctionblockModel model, String name,
      Object value) {

    Optional<ModelProperty> property =
        getAllStateProperties(model).stream().filter(p -> p.getName().equals(name)).findAny();
    if (!property.isPresent()) {
      throw new IllegalArgumentException(
          "No property defined with this name in Function Block Model");
    }

    return new PropertyValue(property.get(), value);
  }

  private static List<ModelProperty> getAllStateProperties(FunctionblockModel model) {
    List<ModelProperty> properties = new ArrayList<ModelProperty>();
    properties.addAll(model.getConfigurationProperties());
    properties.addAll(model.getStatusProperties());
    return properties;
  }
}
