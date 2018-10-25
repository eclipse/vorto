package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.normalized.InfomodelData;
import org.eclipse.vorto.mapping.engine.spec.IMappingSpecification;
import org.junit.Test;

import com.google.gson.GsonBuilder;

public class MappingSpecJsonReaderTest {

	@Test
	public void testReadFromJson() {
		IMappingSpecification spec = IMappingSpecification.newBuilder().fromInputStream(MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json")).build();
		assertNotNull(spec);
		assertNotNull(spec.getInfoModel());
		
	}
	
	@Test
	public void testMappingFromJson() {
		IMappingSpecification spec = IMappingSpecification.newBuilder().fromInputStream(MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json")).build();
		
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec).build();

		String json = "{\"state\" : false, \"count\": 50}";

		InfomodelData mappedOutput = mapper.mapSource(new GsonBuilder().create().fromJson(json, Object.class));
		assertEquals(false,mappedOutput.get("button").getStatus().get("digitalInputState"));
		assertEquals(50.0,mappedOutput.get("button").getStatus().get("digitalInputStateCount"));
	}
}
