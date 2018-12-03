package org.eclipse.vorto.mapping.engine.converter.types;

import static org.junit.Assert.assertEquals;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.converter.types.TypeFunctionFactory;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConvertTest {

  private static Gson gson = new GsonBuilder().create();

  @Test
  public void testMappingTypeConversion() throws Exception {

    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(new SpecWithTypeConversion())
        .registerConverterFunction(TypeFunctionFactory.createFunctions()).build();

    String json = "[{\"lng\" : 0.002322},{\"lng\" : 0.002222}]";

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));

    FunctionblockValue buttonFunctionblockData = mappedOutput.get("button");

    assertEquals("0.002322",
        buttonFunctionblockData.getStatusProperty("sensor_value").get().getValue());

    System.out.println(mappedOutput);

  }
}
