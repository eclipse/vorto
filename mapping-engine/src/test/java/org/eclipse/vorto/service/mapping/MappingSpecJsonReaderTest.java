package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.vorto.service.mapping.normalized.InfomodelData;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecificationReader;
import org.eclipse.vorto.service.mapping.spec.JsonMappingSpecificationReader;
import org.junit.Test;

public class MappingSpecJsonReaderTest {

	@Test
	public void testReadFromJson() {
		IMappingSpecificationReader reader = new JsonMappingSpecificationReader();
		IMappingSpecification spec = reader.read(MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json"));
		assertNotNull(spec);
		assertNotNull(spec.getInfoModel());
		
	}
	
	@Test
	public void testMappingFromJson() {
		IMappingSpecificationReader reader = new JsonMappingSpecificationReader();
		IMappingSpecification spec = reader.read(MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json"));
		
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec).build();

		String json = "{\"state\" : false, \"count\": 50}";

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		assertEquals(false,mappedOutput.get("button").getStatus().get("digitalInputState"));
		assertEquals(25.0,mappedOutput.get("button").getStatus().get("digitalInputStateCount"));
	}
}
