package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.vorto.mapping.engine.DataMapperBuilder;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.mapping.engine.model.spec.MappingSpecBuilder;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.Test;

import com.google.gson.GsonBuilder;

public class MappingSpecJsonReaderTest {

	@Test
	public void testReadFromJson() {
		IMappingSpecification spec = new MappingSpecBuilder().fromInputStream(MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json")).build();
		assertNotNull(spec);
		assertNotNull(spec.getInfoModel());
		
	}
	
	@Test
	public void testMappingFromJson() {
		IMappingSpecification spec = new MappingSpecBuilder().fromInputStream(MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json")).build();
		
		IDataMapper mapper = new DataMapperBuilder().withSpecification(spec).build();

		String json = "{\"state\" : false, \"count\": 50}";

		InfomodelValue mappedOutput = mapper.mapSource(new GsonBuilder().create().fromJson(json, Object.class));
		assertEquals(false,mappedOutput.get("button").getStatusProperty("digitalInputState").getValue());
		assertEquals(50.0,mappedOutput.get("button").getStatusProperty("digitalInputStateCount").getValue());
	}
}
