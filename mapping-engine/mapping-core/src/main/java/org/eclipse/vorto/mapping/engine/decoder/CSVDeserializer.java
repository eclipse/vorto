package org.eclipse.vorto.mapping.engine.decoder;

public class CSVDeserializer implements IPayloadDeserializer {

  @Override
  public Object deserialize(String source) {
    return ((String)source).split(",");

  }

}
