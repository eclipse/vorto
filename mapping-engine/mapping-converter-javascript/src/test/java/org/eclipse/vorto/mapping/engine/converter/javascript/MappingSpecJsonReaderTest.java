package org.eclipse.vorto.mapping.engine.converter.javascript;

import static org.junit.Assert.assertEquals;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.converter.JavascriptEvalProvider;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MappingSpecJsonReaderTest {

  private static Gson gson = new GsonBuilder().create();

  @Test
  public void testMappingFromJson() {
    IMappingSpecification spec = IMappingSpecification.newBuilder().fromInputStream(
        MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json"))
        .build();

    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec)
        .registerScriptEvalProvider(new JavascriptEvalProvider()).build();

    String json = "{\"state\" : false, \"count\": 50}";

    InfomodelValue mappedOutput = mapper.mapSource(gson.fromJson(json, Object.class));
    assertEquals(false,
        mappedOutput.get("button").getStatusProperty("digitalInputState").get().getValue());
    assertEquals(25.0,
        mappedOutput.get("button").getStatusProperty("digitalInputStateCount").get().getValue());
  }
}
