package org.eclipse.vorto.mapping.engine.decoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONDeserializer implements IPayloadDeserializer {

  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
  
  @Override
  public Object deserialize(String source) {
    return gson.fromJson((String)source, Object.class); 
  }

}
