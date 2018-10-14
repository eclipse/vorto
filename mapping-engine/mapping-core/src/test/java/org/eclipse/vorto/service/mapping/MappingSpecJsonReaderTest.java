package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.vorto.mapping.engine.DataInput;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.MappingContext;
import org.eclipse.vorto.mapping.engine.normalized.InfomodelData;
import org.eclipse.vorto.mapping.engine.spec.IMappingSpecification;
import org.junit.Test;

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

		InfomodelData mappedOutput = mapper.map(DataInput.newInstance().fromJson(json), MappingContext.empty());
		assertEquals(false,mappedOutput.get("button").getStatus().get("digitalInputState"));
		assertEquals(50,mappedOutput.get("button").getStatus().get("digitalInputStateCount"));
	}
}
